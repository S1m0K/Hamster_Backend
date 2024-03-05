package at.ac.htlinn.hamsterbackend.run;

import at.ac.htlinn.hamsterbackend.hamsterEvaluation.compiler.model.Precompiler;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.model.HamsterFile;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.simulation.model.Terrain;
import at.ac.htlinn.hamsterbackend.hamsterEvaluation.workbench.Workbench;
import at.ac.htlinn.hamsterbackend.program.Program;
import at.ac.htlinn.hamsterbackend.program.ProgramService;
import at.ac.htlinn.hamsterbackend.terrain.TerrainObject;
import at.ac.htlinn.hamsterbackend.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


@Service
public class RunService {
    @Autowired
    ProgramService programService;

    private final Workbench wb = Workbench.getWorkbench();

    public boolean createTerrainFile(TerrainObject terrainObject, String terrainPath) {
        Terrain terrain = wb.createHamsterTerrain(terrainObject.getCustomFields(), terrainObject.getHeight(), terrainObject.getWidth(), terrainObject.getDefaultHamster());
        return wb.createTerrainFile(terrain, terrainPath);
    }

    public boolean createHamFile(String hamsterPath) {
        File f = new File(hamsterPath);
        return DirectoryManagement.createFile(f);
//
//        FileWriter fileWriter = null;
//        try {
//            fileWriter = new FileWriter(f.getAbsoluteFile());
//            PrintWriter printWriter = new PrintWriter(fileWriter);
//            printWriter.print(program.getSourceCode());
//            printWriter.close();
//            return true;
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
    }

    public boolean precompileHamFile(Program program, String programPath) {
        Precompiler precompiler = new Precompiler();
        HamsterFile hamsterFile = new HamsterFile(program.getSourceCode(), program.getProgramType(), programPath);
        try {
            return precompiler.precompile(hamsterFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getTerrainFilePath(User user, TerrainObject terrainObject) {
        String terrainDir = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "RunDir" + File.separator + user.getId() + File.separator + "TerrainDir";
        DirectoryManagement.createDirs(terrainDir);
        if (!DirectoryManagement.createDirs(terrainDir)) return null;
        return String.format(terrainDir + File.separator + "%s.ter", terrainObject.getTerrainName());
    }

    public String getHamsterProgramFilePath(User user, Program program) {
        String hamsterDir = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "RunDir" + File.separator + user.getId() + File.separator + "HamsterFiles";
        if (!DirectoryManagement.createDirs(hamsterDir)) return null;
        return String.format(hamsterDir + File.separator + "%s.ham", program.getProgramName());
    }

    public ProgramRunFilePaths getCompiledRunFilePaths(long programId, User user) {
        String compDir = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "RunDir" + File.separator + user.getId() + File.separator + "CompDir";

        if (!DirectoryManagement.createDirs(compDir)) return null;


        Program program = programService.getProgram(programId);
        String mainMethodContainingCompiledPath = String.format(compDir + File.separator + "%s.class", program.getProgramName());

        ArrayList<Program> programsToCompile = new ArrayList<>(programService.getAllNeededProgramsToRun(program));
        Precompiler precompiler = new Precompiler();

        for (Program p : programsToCompile) {
            HamsterFile hamsterFile = new HamsterFile(p.getSourceCode(), p.getProgramType(), "");
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

        return new ProgramRunFilePaths("", mainMethodContainingCompiledPath);
    }
}
