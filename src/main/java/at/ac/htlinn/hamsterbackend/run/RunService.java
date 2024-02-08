package at.ac.htlinn.hamsterbackend.run;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.compiler.model.Precompiler;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterFile;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model.Terrain;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Workbench;
import at.ac.htlinn.hamsterbackend.program.Program;
import at.ac.htlinn.hamsterbackend.program.ProgramService;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObject;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObjectService;
import at.ac.htlinn.hamsterbackend.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.lego.model.LegoPrecompiler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


@Service
public class RunService {
    @Autowired
    ProgramService programService;

    @Autowired
    TerrainObjectService terrainObjectService;

    private final Workbench wb = Workbench.getWorkbench();

    private boolean createTerrainFile(TerrainObject terrainObject, String dir) {
        String path = String.format(dir + File.separator + "/%s.ter", terrainObject.getTerrainName());
        Terrain terrain = wb.createHamsterTerrain(terrainObject.getCustomFields(), terrainObject.getHeight(), terrainObject.getWidth(), terrainObject.getDefaultHamster());
        return wb.createTerrainFile(terrain, path);
    }


    public ProgramRunFilePaths getCompiledRunFilePaths(long programId, long terrainId, User user) {
        String compDir = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "RunDir" + File.separator + user.getId() + File.separator + "CompDir";
        String terrainDir = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "RunDir" + File.separator + user.getId() + File.separator + "TerrainDir";

        if (!DirectoryManagement.createDirs(compDir)) return null;
        if (!DirectoryManagement.createDirs(terrainDir)) return null;


        TerrainObject terrainObject = terrainObjectService.getTerrainObject(terrainId);
        String terrainFilePath = String.format(terrainDir + File.separator + "%s.ter", terrainObject.getTerrainName());
        if (!createTerrainFile(terrainObject, terrainDir)) return null;

        Program program = programService.getProgram(programId);
        String mainMethodContainingCompiledPath = String.format(compDir + File.separator + "%s.class", program.getProgramName());

        ArrayList<Program> programsToCompile = new ArrayList<>(programService.getAllNeededProgramsToRun(program));
        Precompiler precompiler = new Precompiler();

        for (Program p : programsToCompile) {
            HamsterFile hamsterFile = new HamsterFile(p.getSourceCode(), p.getProgramType());
            try {
                precompiler.precompile(hamsterFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //TODO check method and usage
            String sourceCodeFilePath = String.format(compDir + File.separator + "%s.ham", program.getProgramName());
            DirectoryManagement.moveFileToDir(hamsterFile.getFile().getPath(), compDir);
            Compiler.compile(compDir, compDir, sourceCodeFilePath);
        }

        return new ProgramRunFilePaths(terrainFilePath, mainMethodContainingCompiledPath);
    }
}
