package com.example.hamster_backend.service;


import com.example.hamster_backend.model.entities.TerrainObject;

public interface TerrainObjectService {
    void compareAndUpdateDatabase(TerrainObject terrainObject);
}
