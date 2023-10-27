package com.example.hamster_backend.model.entities;

import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import javax.persistence.*;

@Builder
@Entity
@Table(name = "student")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonTypeName("student") 
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "student_id")
	private Integer id;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable = false)
	private User user;
	
	
	public Student(int student_id, int user_id) {
		this.id = student_id; 
		this.user = new User(user_id); 
	}
}
