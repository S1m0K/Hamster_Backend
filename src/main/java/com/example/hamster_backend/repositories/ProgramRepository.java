package com.example.hamster_backend.repositories;

import com.example.hamster_backend.model.entities.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
    Set<Program> findAllByUserID(long id);
}
