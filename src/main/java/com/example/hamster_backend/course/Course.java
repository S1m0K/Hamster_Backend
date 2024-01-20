package com.example.hamster_backend.course;

import java.util.Set;

import com.example.hamster_backend.user.User;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "course")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonTypeName("course") 
public class Course {

	public Course(String name) {
		this.name = name; 
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "course_id")
	private Integer id;
	@Column(name = "name", unique = true, nullable = false)
	private String name;
		
	@ManyToMany(cascade = CascadeType.MERGE)
	@JoinTable(name = "user_course", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))
	private Set<User> users;

}
