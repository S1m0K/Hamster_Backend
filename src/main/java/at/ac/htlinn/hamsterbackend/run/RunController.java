package at.ac.htlinn.hamsterbackend.run;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Workbench;
import at.ac.htlinn.hamsterbackend.user.UserService;
import at.ac.htlinn.hamsterbackend.user.model.User;

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
