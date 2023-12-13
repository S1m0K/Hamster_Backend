package com.example.hamster_backend.model.entities;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Table(name = "TERRAIN")
public class TerrainObject {
    @Id
    @SequenceGenerator(name = "terrain_seq", sequenceName = "TERRAIN_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "terrain_seq")
    @Column(name = "ID")
    private long terrainId;

    @Column(name = "USER_ID")
    private long userId;

    @Column(name = "TERRAIN_NAME")
    private String terrainName;

    @Column(name = "WIDTH")
    private int width;

    @Column(name = "HEIGHT")
    private int height;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hamster_id", referencedColumnName = "id")
    private HamsterObject defaultHamster;

    @OneToMany(targetEntity = Field.class, mappedBy = "terrainObject", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Field> customFields = new HashSet<>();

    @Column(name = "TERRAIN_PATH")
    private String terrainPath;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TerrainObject that = (TerrainObject) o;
        return Objects.equals(userId, that.userId) && Objects.equals(terrainName, that.terrainName) && Objects.equals(width, that.width)
                && Objects.equals(height, that.height) && Objects.equals(customFields, that.customFields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, terrainName, width, height, /*hamsters*/defaultHamster, customFields);
    }

    public TerrainObject(long terrainId, String terrainName, String terrainPath) {
        this.terrainId = terrainId;
        this.terrainName = terrainName;
        this.terrainPath = terrainPath;
    }
}
