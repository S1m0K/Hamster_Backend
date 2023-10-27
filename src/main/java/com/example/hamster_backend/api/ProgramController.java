package com.example.hamster_backend.api;

import com.example.hamster_backend.model.entities.Program;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.hamster_backend.service.ProgramService;

@RestController
@RequestMapping("/program")
public class ProgramController {
    @Autowired
    ProgramService programService;

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = "saveOrUpdate")
    public void saveProgram(@RequestBody Program program){programService.saveOrUpdate(program);}

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping(path = "delete/{program_id}")
    public void deleteProgram(@PathVariable("program_id") long programId){programService.delete(programId);}


}
