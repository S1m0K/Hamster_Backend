package com.example.hamster_backend.api;

import com.example.hamster_backend.hamsterEvaluation.simulation.model.Terrain;
import com.example.hamster_backend.hamsterEvaluation.workbench.Workbench;
import com.example.hamster_backend.model.WriteFile;
import com.example.hamster_backend.model.entities.Program;
import com.example.hamster_backend.model.entities.TerrainObject;
import com.example.hamster_backend.model.entities.User;
import com.example.hamster_backend.service.ProgramService;
import com.example.hamster_backend.service.TerrainObjectService;
import com.example.hamster_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;

import static com.example.hamster_backend.model.entities.Program.resolveCompileOrder;

@RestController
@RequestMapping("/run")
public class RunController {
    @Autowired
    TerrainObjectService terrainObjectService;

    @Autowired
    ProgramService programService;

    @Autowired
    UserService userService;

    private final Workbench wb = Workbench.getWorkbench();

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/runProgram/{program_id}/{terrain_id}")
    @ResponseBody
    public ResponseEntity<?> runProgram(@PathVariable("program_id") long programId, @PathVariable("terrain_id") long terrainId) {
        TerrainObject terrainObject = terrainObjectService.getTerrainObject(terrainId);
        User user = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        String terrainPath = String.format("src/main/resources/hamster/%s/%s.ter", user.getUsername(), terrainObject.getTerrainName());
        Terrain terrain = wb.createHamsterTerrain(terrainObject.getCustomFields(), terrainObject.getHeight(), terrainObject.getWidth(), terrainObject.getDefaultHamster());
        wb.createTerrainFile(terrain, terrainPath);


        Program program = programService.getProgram(programId);
        ArrayList<Program> programsToCompile = new ArrayList<>(programService.getAllNeededProgramToRun(program.getSourceCode()));
        if (!programsToCompile.isEmpty()) {
            programsToCompile = resolveCompileOrder(programsToCompile);
        }
        programsToCompile.add(program);//last to compile is main-containing program

        for (Program p : programsToCompile) {
            String sourceCodeFilePath = String.format("src/main/resources/hamster/%s/%s.ham", SecurityContextHolder.getContext().getAuthentication().getName(), program.getProgramName());
            WriteFile.createNewFile(sourceCodeFilePath);
            WriteFile.writeTextToFile(new File(sourceCodeFilePath), program.getSourceCode());
            wb.compile(sourceCodeFilePath);
        }

        wb.getJsonObject().clear();
        String mainMethodContainingPath = String.format("src/main/resources/hamster/%s/%s.ham", user.getUsername(),
                program.getProgramName());
        return new ResponseEntity<>(wb.startProgram(mainMethodContainingPath, terrainPath), HttpStatus.OK);
    }
}
