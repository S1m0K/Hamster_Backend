package com.example.hamster_backend.service;


import com.example.hamster_backend.model.entities.TerrainObject;

import java.util.Set;

public interface TerrainObjectService {
    void compareAndUpdateDatabase(TerrainObject terrainObject);

    Set<TerrainObject> getTerrainObjectBasicData(long id);

    boolean delete(long id);

    TerrainObject getTerrainObject(long id);

    TerrainObject save(TerrainObject terrainObject);

    boolean updatePath(TerrainObject terrainObject);

    boolean updateName(TerrainObject terrainObject);

    boolean update(TerrainObject terrainObject);
}
