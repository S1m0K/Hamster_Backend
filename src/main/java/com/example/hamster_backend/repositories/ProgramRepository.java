package com.example.hamster_backend.repositories;

import com.example.hamster_backend.model.entities.Program;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {
    Set<Program> findAllByUserId(long id);

    @Query("select p.programId, p.programName, p.programPath from Program p where p.userId = :userId")
    Set<Program> findAllProgramBasicDataByUserID(@Param("userid") long userId);

    Program findProgramByProgramId(long id);

    @Modifying
    @Query("update Program p set p.sourceCode = :source_code  where p.programId = :program_id ")
    void update(@Param("program_id") long programId,
                @Param("source_code") String sourceCode
    );

    @Modifying
    @Query("update Program p set p.programName = :program_name  where p.programId = :program_id ")
    void updateName(@Param("program_id") long programId,
                @Param("program_name") String programName
    );

    @Modifying
    @Query("update Program p set p.programPath = :program_path  where p.programId = :program_id ")
    void updatePath(@Param("program_id") long programId,
                    @Param("program_path") String programPath
    );

    Program findProgramByProgramName(String name);

}
