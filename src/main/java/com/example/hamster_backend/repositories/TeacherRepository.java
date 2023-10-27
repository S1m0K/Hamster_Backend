package com.example.hamster_backend.repositories;

import com.example.hamster_backend.model.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long>{
	Teacher findById(int id); 	@Query(value = "SELECT * FROM TEACHER t JOIN USERS u USING (user_id) where u.user_id=:user_id", nativeQuery = true)
	Teacher findByUserId(@Param("user_id") long user_id); 
	@Query(value = "SELECT * FROM TEACHER t JOIN USERS u USING (user_id) where u.username=:username", nativeQuery = true)
	Teacher findByName(@Param("username") String username); 

}
