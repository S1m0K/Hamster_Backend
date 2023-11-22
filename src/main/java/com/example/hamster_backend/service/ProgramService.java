package com.example.hamster_backend.service;


import com.example.hamster_backend.model.entities.Program;

import java.util.ArrayList;
import java.util.Set;

public interface ProgramService {
    void compareAndUpdateDatabase(Program program);

    void save(Program program);

    void updatePath(Program program);

    void updateName(Program program);

    void delete(long programId);

    Set<Program> getProgramBasicData(long id);

    Program getProgram(long id);

    ArrayList<Program> getProgramsByNames(ArrayList<String> names);

    Set<Program> getAllNeededProgramToRun(String mainFileCode);

}
