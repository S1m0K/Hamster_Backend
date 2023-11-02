package com.example.hamster_backend.service.impl;

import com.example.hamster_backend.model.entities.Program;
import com.example.hamster_backend.repositories.ProgramRepository;
import com.example.hamster_backend.service.ProgramService;
import com.example.hamster_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ProgramServiceImpl implements ProgramService {

    @Autowired
    ProgramRepository programRepository;

    @Autowired
    UserService userService;

    @Override
    public void compareAndUpdateDatabase(Program program) {
        program.setHashValue(program.hashCode());
        Set<Program> userPrograms = programRepository.findAllByUserId(program.getUserId());

        if (!userPrograms.isEmpty()) {
            userPrograms.forEach(t -> {
                if (t.getHashValue() == program.getHashValue()) {
                    t = program;
                    programRepository.save(t);
                }
            });
        } else {
            programRepository.save(program);
        }
    }


    @Override
    public void delete(long programId) {
        Optional<Program> o = programRepository.findById(programId);
        if (o.isPresent()) {
            Program p = o.get();
            long userId = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
            if (userId == p.getUserId()) {
                programRepository.delete(p);
            }
        }
    }

    @Override
    public Set<Program> getProgramBasicData(long id) {
        return programRepository.findAllProgramBasicDataByUserID(id);
    }

    @Override
    public Program getProgram(long id) {
        return programRepository.findProgramByProgramId(id);
    }

    @Override
    public ArrayList<Program> getProgramsByNames(ArrayList<String> names) {
        ArrayList<Program> programs = new ArrayList<>();
        names.forEach(n -> {
            programs.add(programRepository.findProgramByProgramName(n));
        });
        return programs;
    }

    @Override
    public Set<Program> getAllNeededProgramToRun(String mainFileCode) {
        Set<String> foundClasses = new HashSet<>();
        Set<Program> programs = new HashSet<>();
        Set<String> classNames = new HashSet<>(Program.extractImportedClasses(mainFileCode));

        while (!classNames.isEmpty()) {
            classNames.forEach(n -> {
                Program program = programRepository.findProgramByProgramName(n);
                classNames.addAll(Program.extractImportedClasses(program.getSourceCode()));
                foundClasses.add(n);
            });
            classNames.removeIf(foundClasses::contains);
        }

        foundClasses.forEach(n -> programs.add(programRepository.findProgramByProgramName(n)));

        return programs;
    }

    @Override
    public void save(Program program) {
        Optional<Program> o = programRepository.findById(program.getProgramId());
        if (o.isPresent()) {
            Program p = o.get();
            long userId = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
            if (userId == p.getUserId()) {
                programRepository.update(p.getProgramId(), p.getSourceCode());
            }
        } else {
            programRepository.save(program);
        }
    }

    @Override
    public void updatePath(Program program) {
        programRepository.updatePath(program.getProgramId(), program.getProgramPath());
    }

    @Override
    public void updateName(Program program) {
        programRepository.updateName(program.getProgramId(), program.getProgramName());
    }
}
