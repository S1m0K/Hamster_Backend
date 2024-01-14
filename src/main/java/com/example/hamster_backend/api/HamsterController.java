package com.example.hamster_backend.api;

import com.example.hamster_backend.model.WriteFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model.Terrain;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Workbench;
import com.example.hamster_backend.model.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.hamster_backend.service.ProgramService;
import com.example.hamster_backend.service.TerrainObjectService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.hamster_backend.model.entities.Program.resolveCompileOrder;


@RestController
@RequestMapping("/hamster")
public class HamsterController {


    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private TerrainObjectService terrainObjectService;

    @Autowired
    private ProgramService programService;

    private Workbench wb = Workbench.getWorkbench();


    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/defaultTerrain")
    @ResponseBody
    public ResponseEntity<?> defaultTerrain(@RequestBody JsonNode node) {
        Hamster hamster = mapper.convertValue(node.get("hamster"), Hamster.class);
        String path = String.format("src/main/resources/hamster/%s/%s/%s.ham", SecurityContextHolder.getContext().getAuthentication().getName(), hamster.getProgramName(), hamster.getProgramName());
        WriteFile.createNewFile(path);
        WriteFile.writeTextToFile(new File(path), hamster.getProgram());
        wb.getJsonObject().clear();
        return new ResponseEntity<>(wb.startProgram(path), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/existingTerrain")
    @ResponseBody
    public ResponseEntity<?> existingTerrain(@RequestBody JsonNode node) {
        Hamster hamster = mapper.convertValue(node.get("hamster"), Hamster.class);
        String hamsterPath = String.format("src/main/resources/hamster/%s/%s/%s.ham", SecurityContextHolder.getContext().getAuthentication().getName(), hamster.getProgramName(), hamster.getProgramName());
        String terrainPath = String.format("src/main/resources/hamster/%s/%s/%s.ter", SecurityContextHolder.getContext().getAuthentication().getName(), hamster.getProgramName(), hamster.getTerrainName());
        WriteFile.createNewFile(hamsterPath);
        WriteFile.writeTextToFile(new File(hamsterPath), hamster.getProgram());
        wb.getJsonObject().clear();
        return new ResponseEntity<>(wb.startProgram(hamsterPath, terrainPath), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/newTerrain")
    @ResponseBody
    public ResponseEntity<?> newTerrain(@RequestBody JsonNode node) {
        Hamster hamster = mapper.convertValue(node.get("hamster"), Hamster.class);
        String hamsterPath = String.format("src/main/resources/hamster/%s/%s/%s.ham", SecurityContextHolder.getContext().getAuthentication().getName(), hamster.getProgramName(), hamster.getProgramName());
        String terrainPath = String.format("src/main/resources/hamster/%s/%s/%s.ter", SecurityContextHolder.getContext().getAuthentication().getName(), hamster.getProgramName(), hamster.getTerrainName());
        WriteFile.createNewFile(hamsterPath);
        WriteFile.writeTextToFile(new File(hamsterPath), hamster.getProgram());
        wb.getJsonObject().clear();
        return new ResponseEntity<>(wb.startProgram(hamsterPath, terrainPath,
                wb.new TerrainForm(hamster.getLaenge(), hamster.getBreite(), hamster.getX(), hamster.getY(), hamster.getBlickrichtung(), hamster.getCornAnzahl(), hamster.getCorn(), hamster.getWall())), HttpStatus.OK);
    }


}