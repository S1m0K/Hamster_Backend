package com.example.hamster_backend.model.entities;

public class ProgramRunFiles {
    public final String terrainPath;
    public final String mainMethodContainingPath;

    public ProgramRunFiles(String terrainPath, String mainMethodContainingPath) {
        this.terrainPath = terrainPath;
        this.mainMethodContainingPath = mainMethodContainingPath;
    }
}
