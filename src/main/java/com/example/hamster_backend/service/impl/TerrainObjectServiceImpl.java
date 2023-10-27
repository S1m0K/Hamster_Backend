package com.example.hamster_backend.service.impl;

import com.example.hamster_backend.model.entities.TerrainObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.hamster_backend.repositories.TerrainObjectRepository;
import com.example.hamster_backend.service.TerrainObjectService;

import java.util.Set;

@Service
public class TerrainObjectServiceImpl implements TerrainObjectService {
    @Autowired
    TerrainObjectRepository terrainObjectRepository;

    @Override
    public void compareAndUpdateDatabase(TerrainObject terrainObject) {
        terrainObject.setHashValue(terrainObject.hashCode());
        Set<TerrainObject> userTerrains = terrainObjectRepository.findAllByUserID(terrainObject.getUserID());

        if (!userTerrains.isEmpty()) {
            userTerrains.forEach(t -> {
                if (t.getHashValue() == terrainObject.getHashValue()) {
                    t = terrainObject;
                    terrainObjectRepository.save(t);
                }
            });
        }else{
            terrainObjectRepository.save(terrainObject);
        }
    }
}
