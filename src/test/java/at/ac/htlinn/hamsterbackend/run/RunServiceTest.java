package at.ac.htlinn.hamsterbackend.run;

import at.ac.htlinn.hamsterbackend.program.Program;
import at.ac.htlinn.hamsterbackend.program.ProgramService;
import at.ac.htlinn.hamsterbackend.terrain.*;
import at.ac.htlinn.hamsterbackend.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RunServiceTest {
    @Autowired
    RunService runService;

    @MockBean
    ProgramService programService;

    @MockBean
    TerrainObjectService terrainObjectService;

    @Test
    public void getCompiledRunFilePathsTest() {
        User user = User.builder()
                .id(123)
                .username("user")
                .build();

        Program program1 = Program.builder()
                .programId(123)
                .programName("TestProgram1")
                .userId(user.getId())
                .programPath("")
                .sourceCode("")
                .build();

        Program program2 = Program.builder()
                .programId(124)
                .programName("TestProgram2")
                .userId(user.getId())
                .programPath("")
                .sourceCode("")
                .build();

        Program program3 = Program.builder()
                .programId(125)
                .programName("TestProgram3")
                .userId(user.getId())
                .programPath("")
                .sourceCode("")
                .build();

        HamsterObject defaultHamster = HamsterObject.builder()
                .hamster_id(123)
                .yCord(4)
                .xCord(4)
                .cntCornInMouth(10)
                .viewDirection(ViewDirection.NORTH)
                .build();

        Field field1 = Field.builder()
                .field_id(123L)
                .wall(true)
                .xCord(1)
                .yCord(1)
                .build();
        Field field2 = Field.builder()
                .field_id(124L)
                .cntCorn(5)
                .xCord(2)
                .yCord(2)
                .build();

        List<Field> fields = new ArrayList<>();
        fields.add(field1);
        fields.add(field2);

        TerrainObject terrain = TerrainObject.builder()
                .terrainId(123)
                .terrainName("TestTerrain")
                .terrainPath("")
                .defaultHamster(defaultHamster)
                .height(12)
                .width(12)
                .userId(user.getId())
                .customFields(fields)
                .build();

        Set<Program> programs = new HashSet<>();
        programs.add(program1);
        programs.add(program2);
        programs.add(program3);

        when(programService.getProgram(program1.getProgramId())).thenReturn(program1);
        when(programService.getAllNeededProgramsToRun(program1)).thenReturn(programs);
        when(terrainObjectService.getTerrainObject(terrain.getTerrainId())).thenReturn(terrain);

        ProgramRunFilePaths response = runService.getCompiledRunFilePaths(program1.getProgramId(), terrain.getTerrainId(), user);

        assertEquals("src\\main\\resources\\RunDir\\123\\CompDir\\TestProgram1.class", response.mainMethodContainingPath);
        assertEquals("src\\main\\resources\\RunDir\\123\\TerrainDir\\TestTerrain.ter", response.terrainPath);

    }
}
