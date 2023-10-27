package com.example.hamster_backend.model.entities;


import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;
import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@JsonTypeName("hamster")
public class Hamster {

    public Hamster(String program, String programName) {
        this.program = program;
        this.programName = programName;
    }

    @Id
    private Integer hamster_id;
    private String programName;
    private String terrainName;
    private String program;
    private int laenge, breite, x, y, blickrichtung;
    private int[] cornAnzahl;
    private int[][] corn, wall;
}
