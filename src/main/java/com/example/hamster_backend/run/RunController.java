package com.example.hamster_backend.run;

import com.example.hamster_backend.hamsterEvaluation.workbench.Workbench;
import com.example.hamster_backend.user.User;
import com.example.hamster_backend.user.UserService;
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
    UserService userService;

    @Autowired
    RunService runService;

    private final Workbench wb = Workbench.getWorkbench();

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/runProgram/{program_id}/{terrain_id}")
    @ResponseBody
    public ResponseEntity<?> runProgram(@PathVariable("program_id") long programId, @PathVariable("terrain_id") long terrainId, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        ProgramRunFilePaths runFilePaths = runService.getCompiledRunFilePaths(programId, terrainId, user);
        return new ResponseEntity<>(wb.startProgram(runFilePaths.mainMethodContainingPath, runFilePaths.terrainPath), HttpStatus.OK);
    }
}
