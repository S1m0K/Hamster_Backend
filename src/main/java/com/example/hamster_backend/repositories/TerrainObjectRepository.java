package com.example.hamster_backend.repositories;

import com.example.hamster_backend.model.entities.Field;
import com.example.hamster_backend.model.entities.HamsterObject;
import com.example.hamster_backend.model.entities.TerrainObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TerrainObjectRepository extends JpaRepository<TerrainObject, Long> {
    Set<TerrainObject> findAllByUserId(long id);

    @Query("select new com.example.hamster_backend.model.entities.TerrainObject(t.terrainId, t.terrainName, t.terrainPath) from TerrainObject t where t.userId = :user_id")
    Set<TerrainObject> findAllTerrainBasicDataByUserID(@Param("user_id") long userId);

    TerrainObject findTerrainObjectByTerrainId(long id);

    @Modifying
    @Query("update TerrainObject t set t.customFields = :custom_fields , t.defaultHamster = :default_hamster, t.height=:height, t.width = :width where t.terrainId = :terrain_id ")
    void update(@Param("terrain_id") long terrainId,
                @Param("custom_fields") Set<Field> customFields,
                @Param("default_hamster") HamsterObject defaultHamster,
                @Param("height") int height,
                @Param("width") int width
    );

    @Modifying
    @Query("update TerrainObject t set t.terrainName = :terrain_name  where t.terrainId = :terrain_id ")
    void updateName(@Param("terrain_id") long terrainId,
                    @Param("terrain_name") String terrainName
    );

    @Modifying
    @Query("update TerrainObject t set t.terrainPath = :terrain_path  where t.terrainId = :terrain_id ")
    void updatePath(@Param("terrain_id") long terrainId,
                    @Param("terrain_path") String terrainPath
    );
}
