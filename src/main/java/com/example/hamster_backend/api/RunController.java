package com.example.hamster_backend.api;

import com.example.hamster_backend.hamsterEvaluation.workbench.Workbench;
import com.example.hamster_backend.model.entities.ProgramRunFiles;
import com.example.hamster_backend.model.entities.User;
import com.example.hamster_backend.service.ProgramService;
import com.example.hamster_backend.service.TerrainObjectService;
import com.example.hamster_backend.service.UserService;
import com.example.hamster_backend.service.RunService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/run")
public class RunController {
    @Autowired
    TerrainObjectService terrainObjectService;

    @Autowired
    ProgramService programService;

    @Autowired
    UserService userService;

    @Autowired
    RunService runService;

    private final Workbench wb = Workbench.getWorkbench();

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/runProgram/{program_id}/{terrain_id}")
    @ResponseBody
    public ResponseEntity<?> runProgram(@PathVariable("program_id") long programId, @PathVariable("terrain_id") long terrainId, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        ProgramRunFiles runFiles = runService.getRunFiles(programId, terrainId, user);
        return new ResponseEntity<>(wb.startProgram(runFiles.mainMethodContainingPath, runFiles.terrainPath), HttpStatus.OK);
    }
}
