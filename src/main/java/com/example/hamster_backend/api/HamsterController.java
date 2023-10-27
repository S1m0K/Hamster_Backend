package com.example.hamster_backend.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.hamster_backend.hamsterEvaluation.simulation.model.Terrain;
import com.example.hamster_backend.hamsterEvaluation.workbench.Workbench;
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

    /**
     * Creates new File and will create a parent folder if not existing
     * It also writes the given program into the file
     *
     * @param path
     */
    private File createNewFile(String path) {
        try {
            File file = new File(path);
            new File(path.substring(0, path.lastIndexOf("/"))).mkdirs();
            file.createNewFile();
            return file;
        } catch (IOException e) {
            return null;
        }
    }

    private boolean writeTextToFile(File file, String program) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, false);
            fileOutputStream.write(program.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (IOException | NullPointerException e) {
            return false;
        }
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping("/defaultTerrain")
    @ResponseBody
    public ResponseEntity<?> defaultTerrain(@RequestBody JsonNode node) {
        Hamster hamster = mapper.convertValue(node.get("hamster"), Hamster.class);
        String path = String.format("src/main/resources/hamster/%s/%s/%s.ham", SecurityContextHolder.getContext().getAuthentication().getName(), hamster.getProgramName(), hamster.getProgramName());
        createNewFile(path);
        writeTextToFile(new File(path), hamster.getProgram());
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
        createNewFile(hamsterPath);
        writeTextToFile(new File(hamsterPath), hamster.getProgram());
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
        createNewFile(hamsterPath);
        writeTextToFile(new File(hamsterPath), hamster.getProgram());
        wb.getJsonObject().clear();
        return new ResponseEntity<>(wb.startProgram(hamsterPath, terrainPath,
                wb.new TerrainForm(hamster.getLaenge(), hamster.getBreite(), hamster.getX(), hamster.getY(), hamster.getBlickrichtung(), hamster.getCornAnzahl(), hamster.getCorn(), hamster.getWall())), HttpStatus.OK);
    }

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
            createNewFile(sourceCodeFilePath);
            writeTextToFile(new File(sourceCodeFilePath), program.getSourceCode());
            wb.compile(sourceCodeFilePath);
        }

        wb.getJsonObject().clear();
        String mainMethodContainingPath = String.format("src/main/resources/hamster/%s/%s.ham", SecurityContextHolder.getContext().getAuthentication().getName(),
                programs.get(programs.size() - 1).getProgramName());
        return new ResponseEntity<>(wb.startProgram(mainMethodContainingPath, terrainPath), HttpStatus.OK);
    }


}