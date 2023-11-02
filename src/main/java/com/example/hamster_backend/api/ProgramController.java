package com.example.hamster_backend.api;

import com.example.hamster_backend.model.entities.Program;
import com.example.hamster_backend.model.entities.User;
import com.example.hamster_backend.service.ProgramService;
import com.example.hamster_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("/program")
public class ProgramController {
    @Autowired
    ProgramService programService;

    @Autowired
    UserService userService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = "save")
    public void saveProgram(@RequestBody Program program) {
        programService.save(program);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = "updatePath")
    public void updatePath(@RequestBody Program program) {
        programService.updatePath(program);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = "updateName")
    public void updateName(@RequestBody Program program) {
        programService.updateName(program);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping(path = "delete/{program_id}")
    public void deleteProgram(@PathVariable("program_id") long programId) {
        programService.delete(programId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = "get/{program_id}")
    public Program getProgram(@PathVariable("program_id") long programId) {
        return programService.getProgram(programId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = "getBasicData")
    public Set<Program> getProgramBasicData(Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        return programService.getProgramBasicData(user.getId());
    }
}
