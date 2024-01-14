package com.example.hamster_backend.service.impl;

import com.example.hamster_backend.model.entities.TerrainObject;
import com.example.hamster_backend.repositories.TerrainObjectRepository;
import com.example.hamster_backend.service.TerrainObjectService;

import at.ac.htlinn.hamsterbackend.user.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class TerrainObjectServiceImpl implements TerrainObjectService {
    @Autowired
    TerrainObjectRepository terrainObjectRepository;

    @Autowired
    UserService userService;


    @Override
    public Set<TerrainObject> getTerrainObjectBasicData(long id) {
        return terrainObjectRepository.findAllTerrainBasicDataByUserID(id);
    }

    @Override
    public boolean delete(long id) {
        Optional<TerrainObject> o = terrainObjectRepository.findById(id);
        if (o.isPresent()) {
            TerrainObject t = o.get();
            long userId = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
            if (userId == t.getUserId()) {
                terrainObjectRepository.delete(t);
                Optional<TerrainObject> o2 = terrainObjectRepository.findById(id);
                return o2.isEmpty();
            }
        }
        return false;
    }

    @Override
    public TerrainObject getTerrainObject(long id) {
        return terrainObjectRepository.findTerrainObjectByTerrainId(id);
    }

    @Override
    public TerrainObject save(TerrainObject terrainObject) {
        return terrainObjectRepository.save(terrainObject);
    }

    @Override
    public boolean updatePath(TerrainObject terrainObject) {
        return terrainObjectRepository.updatePath(terrainObject.getTerrainId(), terrainObject.getTerrainPath());
    }

    @Override
    public boolean updateName(TerrainObject terrainObject) {
        return terrainObjectRepository.updateName(terrainObject.getTerrainId(), terrainObject.getTerrainName());
    }

    @Override
    public boolean update(TerrainObject terrainObject) {
        Optional<TerrainObject> o = terrainObjectRepository.findById(terrainObject.getTerrainId());
        if (o.isPresent()) {
            TerrainObject t = o.get();
            long userId = userService.findUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
            if (userId == t.getUserId()) {
                return terrainObjectRepository.update(t.getTerrainId(), t.getCustomFields(), t.getDefaultHamster(), t.getHeight(), t.getWidth());
            }
        }
        return false;
    }
}
