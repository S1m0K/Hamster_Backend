package at.ac.htlinn.hamsterbackend.run;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterFile;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model.Terrain;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Workbench;
import at.ac.htlinn.hamsterbackend.program.Program;
import at.ac.htlinn.hamsterbackend.program.ProgramService;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObject;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObjectService;
import at.ac.htlinn.hamsterbackend.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;


@Service
public class RunService  {
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
