package com.example.hamster_backend.service;

import com.example.hamster_backend.model.entities.ProgramRunFilePaths;

import at.ac.htlinn.hamsterbackend.user.model.User;

public interface RunService
{
    ProgramRunFilePaths getCompiledRunFilePaths(long programId, long terrainId, User user);
}
