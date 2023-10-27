package com.example.hamster_backend.api;

import com.example.hamster_backend.hamsterEvaluation.simulation.model.Terrain;
import com.example.hamster_backend.hamsterEvaluation.workbench.Workbench;
import com.example.hamster_backend.model.WriteFile;
import com.example.hamster_backend.model.entities.Program;
import com.example.hamster_backend.model.entities.RunConfig;
import com.example.hamster_backend.model.entities.TerrainObject;
import com.example.hamster_backend.model.entities.User;
import com.example.hamster_backend.service.ProgramService;
import com.example.hamster_backend.service.TerrainObjectService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private ObjectMapper mapper;

    private Workbench wb = Workbench.getWorkbench();


    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/runProgram")
    @ResponseBody
    public ResponseEntity<?> runProgram(@RequestBody JsonNode node) {
        RunConfig runConfig = mapper.convertValue(node.get("runConfig"), RunConfig.class);
        TerrainObject terrainObject = runConfig.getTerrainObject();
        User u = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        terrainObject.setUserID(u.getId());

        terrainObjectService.compareAndUpdateDatabase(terrainObject);

        String terrainPath = String.format("src/main/resources/hamster/%s/%s.ter", SecurityContextHolder.getContext().getAuthentication().getName(), terrainObject.getTerrainName());
        Terrain terrain = wb.createHamsterTerrain(terrainObject.getTerrainName(), terrainObject.getCustomFields(), terrainObject.getHeight(), terrainObject.getWidth(), terrainObject.getDefaultHamster());
        wb.createTerrainFile(terrain, terrainPath);

        ArrayList<Program> programs = runConfig.getPrograms().stream().collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        programs = resolveCompileOrder(programs);

        for (Program program : programs) {
            programService.compareAndUpdateDatabase(program);

            String sourceCodeFilePath = String.format("src/main/resources/hamster/%s/%s.ham", SecurityContextHolder.getContext().getAuthentication().getName(), program.getProgramName());
            WriteFile.createNewFile(sourceCodeFilePath);
            WriteFile.writeTextToFile(new File(sourceCodeFilePath), program.getSourceCode());
            wb.compile(sourceCodeFilePath);
        }

        wb.getJsonObject().clear();
        String mainMethodContainingPath = String.format("src/main/resources/hamster/%s/%s.ham", SecurityContextHolder.getContext().getAuthentication().getName(),
                programs.get(programs.size() - 1).getProgramName());
        return new ResponseEntity<>(wb.startProgram(mainMethodContainingPath, terrainPath), HttpStatus.OK);
    }


    //TODO: rewrite to runProgram(<ProgramId>,<TerrainName>) --> look into file and search for classNames --> load them and compile
}
