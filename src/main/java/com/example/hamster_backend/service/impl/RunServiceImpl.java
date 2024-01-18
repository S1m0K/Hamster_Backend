package com.example.hamster_backend.service.impl;

import com.example.hamster_backend.hamsterEvaluation.model.HamsterFile;
import com.example.hamster_backend.hamsterEvaluation.simulation.model.Terrain;
import com.example.hamster_backend.hamsterEvaluation.workbench.Workbench;
import com.example.hamster_backend.model.Compiler;
import com.example.hamster_backend.model.WriteFile;
import com.example.hamster_backend.model.entities.Program;
import com.example.hamster_backend.model.entities.ProgramRunFilePaths;
import com.example.hamster_backend.model.entities.TerrainObject;
import com.example.hamster_backend.model.entities.User;
import com.example.hamster_backend.service.ProgramService;
import com.example.hamster_backend.service.RunService;
import com.example.hamster_backend.service.TerrainObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


@Service
public class RunServiceImpl implements RunService {
    @Autowired
    ProgramService programService;

    @Autowired
    TerrainObjectService terrainObjectService;

    private final Workbench wb = Workbench.getWorkbench();


    private void compileFiles(ArrayList<Program> programsToCompile, Program program) {
        for (Program p : programsToCompile) {
            String sourceCodeFilePath = String.format("src/main/resources/hamster/%s/%s.ham", SecurityContextHolder.getContext().getAuthentication().getName(), program.getProgramName());
            WriteFile.createNewFile(sourceCodeFilePath);
            WriteFile.writeTextToFile(new File(sourceCodeFilePath), program.getSourceCode());
            wb.compile(sourceCodeFilePath);
        }
    }

    private String createTerrainFile(User user, long terrainId) {
        TerrainObject terrainObject = terrainObjectService.getTerrainObject(terrainId);
        String terrainPath = String.format("src/main/resources/RunDir/%d/TerrainDir/%s.ter", user.getId(), terrainObject.getTerrainName());
        Terrain terrain = wb.createHamsterTerrain(terrainObject.getCustomFields(), terrainObject.getHeight(), terrainObject.getWidth(), terrainObject.getDefaultHamster());
        wb.createTerrainFile(terrain, terrainPath);
        return terrainPath;
    }

    public ProgramRunFilePaths getCompiledRunFilePaths(long programId, long terrainId, User user) {
        String terrainFilePath = createTerrainFile(user, terrainId);

        Program program = programService.getProgram(programId);
        ArrayList<Program> programsToCompile = new ArrayList<>(programService.getAllNeededProgramToRun(program));

        String path = "src\\main\\resources\\RunDir\\" + user.getId() + "\\CompDir";
        Compiler.createCompilingDir(path);
        for (Program p : programsToCompile) {
            HamsterFile hamsterFile = new HamsterFile(p.getSourceCode(), p.getProgramType());
            File f = hamsterFile.getFile();
            String sourceCodeFilePath = String.format("src/main/resources/RunDir/%d/CompDir/%s.ham", user.getId(), program.getProgramName());
            Compiler.addFileToCompilingDir(f.getPath(), sourceCodeFilePath);
            Compiler.compile(path, path, sourceCodeFilePath);
        }

        String mainMethodContainingCompiledPath = String.format("src/main/resources/RunDir/%d/CompDir/%s.class", user.getId(),
                program.getProgramName());
        return new ProgramRunFilePaths(terrainFilePath, mainMethodContainingCompiledPath);
    }
}
