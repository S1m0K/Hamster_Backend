package com.example.hamster_backend.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import com.example.hamster_backend.model.enums.ViewDirection;
import javax.persistence.*;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "HAMSTER")
public class HamsterObject {
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "hamster_seq", sequenceName = "HAMSTER_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hamster_seq")
    private Integer hamster_id;

//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name = "TERRAIN_ID")
//    private TerrainObject terrainObject;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "TERRAIN_ID")
    private TerrainObject terrainObject;

    @Column(name = "X_CORD")
    private int xCord;

    @Column(name = "Y_CORD")
    private int yCord;

    @Column(name = "CNT_CORN_IN_MOUTH")
    private int cntCornInMouth;

    @Enumerated(EnumType.STRING)
    @Column(name = "VIEW_DIRECTION")
    private ViewDirection viewDirection;
}
