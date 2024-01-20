package com.example.hamster_backend.run;

import com.example.hamster_backend.program.Program;
import com.example.hamster_backend.terrain.TerrainObject;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RunConfig {
    TerrainObject terrainObject;
    private Set<Program> programs = new HashSet<>();
}
