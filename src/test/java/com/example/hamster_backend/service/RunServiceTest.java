package com.example.hamster_backend.service;

import com.example.hamster_backend.model.entities.*;
import com.example.hamster_backend.model.enums.ViewDirection;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.thymeleaf.spring5.expression.Fields;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;

@SpringBootTest
public class RunServiceTest {
    @Autowired
    RunService runService;

    @InjectMocks
    ProgramService programService;

    @InjectMocks
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
                .field_id(123)
                .wall(true)
                .xCord(1)
                .yCord(1)
                .build();
        Field field2 = Field.builder()
                .field_id(124)
                .cntCorn(5)
                .xCord(2)
                .yCord(2)
                .build();

        Set<Field> fields = new HashSet<>();
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
        when(programService.getAllNeededProgramToRun(program1)).thenReturn(programs);
        when(terrainObjectService.getTerrainObject(terrain.getTerrainId())).thenReturn(terrain);

        ProgramRunFilePaths response = runService.getCompiledRunFilePaths(program1.getProgramId(), terrain.getTerrainId(), user);

    }
}
