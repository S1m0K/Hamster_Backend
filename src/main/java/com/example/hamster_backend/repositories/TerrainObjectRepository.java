package com.example.hamster_backend.repositories;

import com.example.hamster_backend.model.entities.TerrainObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TerrainObjectRepository extends JpaRepository<TerrainObject, Long> {
    Set<TerrainObject> findAllByUserID(long id);
}
