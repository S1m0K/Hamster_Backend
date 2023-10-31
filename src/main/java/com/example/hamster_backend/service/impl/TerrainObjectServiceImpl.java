package com.example.hamster_backend.service.impl;

import com.example.hamster_backend.model.entities.TerrainObject;
import com.example.hamster_backend.model.session.AuthorizationUtils;
import com.example.hamster_backend.repositories.TerrainObjectRepository;
import com.example.hamster_backend.service.TerrainObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class TerrainObjectServiceImpl implements TerrainObjectService {
    @Autowired
    TerrainObjectRepository terrainObjectRepository;

    @Override
    public void compareAndUpdateDatabase(TerrainObject terrainObject) {
        terrainObject.setHashValue(terrainObject.hashCode());
        Set<TerrainObject> userTerrains = terrainObjectRepository.findAllByUserId(terrainObject.getUserId());

        if (!userTerrains.isEmpty()) {
            userTerrains.forEach(t -> {
                if (t.getHashValue() == terrainObject.getHashValue()) {
                    t = terrainObject;
                    terrainObjectRepository.save(t);
                }
            });
        } else {
            terrainObjectRepository.save(terrainObject);
        }
    }

    @Override
    public Set<TerrainObject> getTerrainObjectBasicData(long id) {
        return terrainObjectRepository.findAllTerrainBasicDataByUserID(id);
    }

    @Override
    public void delete(long id) {
        Optional<TerrainObject> o = terrainObjectRepository.findById(id);
        if (o.isPresent()) {
            TerrainObject t = o.get();
            if (AuthorizationUtils.getAuthUserId().equals(t.getUserId())) {
                terrainObjectRepository.delete(t);
            }
        }
    }

    @Override
    public TerrainObject getTerrainObject(long id) {
        return terrainObjectRepository.findTerrainObjectByTerrainId(id);
    }

    @Override
    public void save(TerrainObject terrainObject) {
        Optional<TerrainObject> o = terrainObjectRepository.findById(terrainObject.getTerrainId());
        if (o.isPresent()) {
            TerrainObject t = o.get();
            if (AuthorizationUtils.getAuthUserId().equals(t.getUserId())) {
                terrainObjectRepository.update(t.getTerrainId(),t.getCustomFields(), t.getDefaultHamster(), t.getHeight(),t.getWidth());
            }
        } else {
            terrainObjectRepository.save(terrainObject);
        }
    }

    @Override
    public void updatePath(TerrainObject terrainObject) {
        terrainObjectRepository.updatePath(terrainObject.getTerrainId(),terrainObject.getTerrainPath());
    }

    @Override
    public void updateName(TerrainObject terrainObject) {
        terrainObjectRepository.updateName(terrainObject.getTerrainId(), terrainObject.getTerrainName());
    }
}
