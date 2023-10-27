package com.example.hamster_backend.service;


import com.example.hamster_backend.model.entities.Program;

public interface ProgramService {
    void compareAndUpdateDatabase(Program program);

    void saveOrUpdate(Program program);

    void delete(long programId);
}
