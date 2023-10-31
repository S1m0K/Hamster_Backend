package com.example.hamster_backend.service;


import com.example.hamster_backend.model.entities.TerrainObject;

import java.util.Set;

public interface TerrainObjectService {
    void compareAndUpdateDatabase(TerrainObject terrainObject);

    Set<TerrainObject> getTerrainObjectBasicData(long id);

    void delete(long id);

    TerrainObject getTerrainObject(long id);

    void save(TerrainObject terrainObject);

    void updatePath(TerrainObject terrainObject);

    void updateName(TerrainObject terrainObject);
}
