package com.example.hamster_backend.model.entities;

public class ProgramRunFilePaths {
    public final String terrainPath;
    public final String mainMethodContainingPath;

    public ProgramRunFilePaths(String terrainPath, String mainMethodContainingPath) {
        this.terrainPath = terrainPath;
        this.mainMethodContainingPath = mainMethodContainingPath;
    }
}
