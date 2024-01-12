package com.example.hamster_backend.service;

import com.example.hamster_backend.model.entities.ProgramRunFilePaths;
import com.example.hamster_backend.model.entities.User;

public interface RunService
{
    ProgramRunFilePaths getCompiledRunFilePaths(long programId, long terrainId, User user);
}
