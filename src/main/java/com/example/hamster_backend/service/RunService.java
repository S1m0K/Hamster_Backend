package com.example.hamster_backend.service;

import com.example.hamster_backend.model.entities.ProgramRunFiles;
import com.example.hamster_backend.model.entities.User;

public interface RunService
{
    ProgramRunFiles getRunFiles(long programId, long terrainId, User user);
}
