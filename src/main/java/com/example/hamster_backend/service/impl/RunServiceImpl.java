package com.example.hamster_backend.service.impl;

import com.example.hamster_backend.hamsterEvaluation.simulation.model.Terrain;
import com.example.hamster_backend.hamsterEvaluation.workbench.Workbench;
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
import java.util.ArrayList;

import static com.example.hamster_backend.model.entities.Program.resolveCompileOrder;

@Service
public class RunServiceImpl implements RunService {
    @Autowired
    ProgramService programService;

    @Autowired
    TerrainObjectService terrainObjectService;

    private final Workbench wb = Workbench.getWorkbench();

    private ArrayList<Program> getProgramsToCompile(Program program) {
        ArrayList<Program> programsToCompile = new ArrayList<>(programService.getAllNeededProgramToRun(program));
        if (!programsToCompile.isEmpty()) {
            programsToCompile = resolveCompileOrder(programsToCompile);
        }
        programsToCompile.add(program);//last to compile is main-containing program
        return programsToCompile;
    }

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
        String terrainPath = String.format("src/main/resources/hamster/%s/%s.ter", user.getUsername(), terrainObject.getTerrainName());
        Terrain terrain = wb.createHamsterTerrain(terrainObject.getCustomFields(), terrainObject.getHeight(), terrainObject.getWidth(), terrainObject.getDefaultHamster());
        wb.createTerrainFile(terrain, terrainPath);
        return terrainPath;
    }

    public ProgramRunFilePaths getRunFilePaths(long programId, long terrainId, User user) {
        String terrainFilePath = createTerrainFile(user, terrainId);

        Program program = programService.getProgram(programId);
        ArrayList<Program> programsToCompile = getProgramsToCompile(program);

        compileFiles(programsToCompile, program);

        wb.getJsonObject().clear();
        String mainMethodContainingPath = String.format("src/main/resources/hamster/%s/%s.ham", user.getUsername(),
                program.getProgramName());
        return new ProgramRunFilePaths(terrainFilePath, mainMethodContainingPath);
    }
}
