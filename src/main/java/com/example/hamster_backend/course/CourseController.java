package com.example.hamster_backend.course;

import java.util.List;
import java.util.stream.Collectors;

import com.example.hamster_backend.course.*;
import com.example.hamster_backend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/course")
public class CourseController {

	@Autowired
	private CourseService courseServiceImpl;
	@Autowired
	private ObjectMapper mapper;
	
	// TODO: check if teacher is in course when making rest request!
	
//	/**
//	 * Get student by id from database Needs as @PathVariable student_id
//	 *
//	 * @param json
//	 * @return
//	 */
	@GetMapping("/students")
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> getStudents() {
		List<User> students = courseServiceImpl.getAllStudents();
		return new ResponseEntity<>(students, HttpStatus.OK);
	}
		
	
//	/**
//	 * Get student by id from database Needs as @PathVariable student_id
//	 *
//	 * @param json
//	 * @return
//	 */
	@GetMapping("/students/{id}")
	@PreAuthorize("hasAuthority('DEV')")
	public ResponseEntity<?> getStudentByID(@PathVariable long id) {
		Student student = courseServiceImpl.getStudentByID((int)id);
		if (student == null || courseServiceImpl.getUserByStudent(student) == null) {
			return new ResponseEntity<>("Student not exisiting!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(student, HttpStatus.OK);
	}
		

//	/**
//	 * Get student from course by id from database
//	 * Needs as @PathVariable student_id and courseid
//	 *
//	 * @param json
//	 * @return
//	 */
	@GetMapping("/course/{courseid}/students/{studentid}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getStudentInCourseByCourseID(@PathVariable long courseid, @PathVariable long studentid) {
		Student student = courseServiceImpl.getStudentByID((int) studentid);
		Course course = courseServiceImpl.getCourseByID((int) studentid);
		User user = courseServiceImpl.getUserByStudent(student);
		if (student == null || user == null || !courseServiceImpl.isUserInCourse(course, user)) {
			return new ResponseEntity<>("Student not exisiting!", HttpStatus.NOT_FOUND);
		}
		student.getUser().setPassword(""); 
		return new ResponseEntity<>(student, HttpStatus.OK);
	}

	
//	/**
//	 * Get all students in Course from database Needs as @PathVariable course_id
//	 *
//	 * @param json
//	 * @return
//	 */
	@GetMapping("/course/{id}/students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getAllStudentsByCourseID(@PathVariable long id) {
		Course course = courseServiceImpl.getCourseByID((int) id);
		List<Student> students;
		if ((students = courseServiceImpl.getAllStudentsInCourse(course)) == null) {
			return new ResponseEntity<>("Course not existing or no users in course!", HttpStatus.NOT_FOUND);
		}
		students.stream().map(user -> {
	        user.getUser().setPassword("");
	        return user;
	    }).collect(Collectors.toList());
		return new ResponseEntity<>(students, HttpStatus.OK);
	}
	
//	/**
//	 * Get all students in Course from database Needs as @RequestParam coursename
//	 *
//	 * @param json
//	 * @return
//	 */
	@GetMapping("/course/students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getAllStudentsByCourseName(@RequestParam(name = "coursename", required = false) String coursename) {
		Course course = courseServiceImpl.getCourseByName(coursename);
		List<Student> students;
		if ((students = courseServiceImpl.getAllStudentsInCourse(course)) == null) {
			return new ResponseEntity<>("Course not existing or no users in course!", HttpStatus.NOT_FOUND);
		}
		students.stream().map(user -> {
	        user.getUser().setPassword("");
	        return user;
	    }).collect(Collectors.toList());
		return new ResponseEntity<>(students, HttpStatus.OK);
	}

	/*
	 * Adds student to existing Course Needs course object + student object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/course/students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> addStudentCourse(@RequestBody JsonNode node) {
		Course course = mapper.convertValue(node.get("course"), Course.class); 
		Student student = mapper.convertValue(node.get("student"), Student.class);
		return courseServiceImpl.addStudentToCourse(course, student) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not add student to course!", HttpStatus.NOT_IMPLEMENTED);
	}

	/*
	 * Removes student from Course Needs course object + student object
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/course/students")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> removeStudentCourse(@RequestBody JsonNode node) {
		Course course = mapper.convertValue(node.get("course"), Course.class); 
		Student student = mapper.convertValue(node.get("student"), Student.class);
		return courseServiceImpl.removeStudentFromCourse(course, student) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not remove student from course!", HttpStatus.NOT_MODIFIED);
	}
	
	/*
	 * Get teacher from course from database Needs as @PathVariable course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/course/{id}/teachers")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getCourseTeacherByCourseID(@PathVariable long id) {
		Course course = courseServiceImpl.getCourseByID((int) id);
		List<User> teachers;
		if ((teachers = courseServiceImpl.getCourseTeachers(course)) == null) {
			return new ResponseEntity<>("There is no teacher in this course - contact an admin!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(teachers, HttpStatus.OK);
	}

	
	/*
	 * get teacher from course from database Needs as @RequestParam coursename
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/course/teachers")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getCourseTeacherByCourseName(@RequestParam(name = "coursename", required = false) String coursename) {
		Course course = courseServiceImpl.getCourseByName(coursename);
		List<User> teachers;
		if ((teachers = courseServiceImpl.getCourseTeachers(course)) == null) {
			return new ResponseEntity<>("There is no teacher in this course - contact an admin!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(teachers, HttpStatus.OK);
	}

	/*
	 * Get course from database Needs as @PathVariable course_id
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/course/{id}")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getCourseByID(@PathVariable long id) {
		Course course = courseServiceImpl.getCourseByID((int) id);
		if (course == null) {
			return new ResponseEntity<>("There is no course with this name!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(course, HttpStatus.OK);
	}

	
	/*
	 * Returns all or one course
	 * 
	 * @param json
	 * @return
	 */
	@GetMapping("/course")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> getCourseByCoursename(@RequestParam(name = "coursename", required = false) String coursename) {
		if(coursename == null) {
			return new ResponseEntity<>(courseServiceImpl.getAllCourses(), HttpStatus.OK);
		}
		Course course = courseServiceImpl.getCourseByName(coursename);
		if(course == null) {
			return new ResponseEntity<>("There is no course with this name!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(course, HttpStatus.OK);
	}
	
	/*
	 * Create a new Course Needs course and teacher object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/course")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> createCourse(@RequestBody JsonNode node) {
		Course course = mapper.convertValue(node.get("course"), Course.class); 
		Teacher teacher = mapper.convertValue(node.get("teacher"), Teacher.class);
		if (courseServiceImpl.createCourse(course) != null) {
			courseServiceImpl.setCourseTeacher(course, teacher);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>("Could not create course!", HttpStatus.NOT_IMPLEMENTED);
	}

	/*
	 * Deletes a existing Course Needs course object
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/course")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> deleteCourse(@RequestBody JsonNode node) {
		Course course = mapper.convertValue(node.get("course"), Course.class); 
		return courseServiceImpl.deleteCourse(course) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not delete course!", HttpStatus.NOT_MODIFIED);
	}



	/*
	 * Creates a new exercise in a existing Course Needs course object + exercise
	 * object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/exercises")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> createExercise(@RequestBody JsonNode node) {
		Exercise exercise = mapper.convertValue(node.get("exercise"), Exercise.class);
		return courseServiceImpl.createExercise(exercise) != null ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not create exercise!", HttpStatus.NOT_IMPLEMENTED);
	}

	/*
	 * Changes already existing (and published) exercises Needs course object +
	 * exercise object
	 * 
	 * @param json
	 * @return
	 */
	@PutMapping("/exercises")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> patchExercise(@RequestBody JsonNode node) {
		Exercise exercise = mapper.convertValue(node.get("exericse"), Exercise.class);
		return courseServiceImpl.createExercise(exercise) != null ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not patch exercise!", HttpStatus.NOT_IMPLEMENTED);
	}

	/*
	 * Deletes existing exercise Needs course object + exercise object
	 * 
	 * @param json
	 * @return
	 */
	@DeleteMapping("/exercises")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> deleteExercise(@RequestBody JsonNode node) {
		Exercise exercise = mapper.convertValue(node.get("exercise"), Exercise.class);
		return courseServiceImpl.deleteExercise(exercise) ? new ResponseEntity<>(HttpStatus.OK)
				: new ResponseEntity<>("Could not delete exercise!", HttpStatus.NOT_IMPLEMENTED);
	}

	/*
	 * Gives a rating to an exercise for one student Needs course object + exercise
	 * object + student object
	 * 
	 * @param json
	 * @return
	 */
	// TODO: change url!
	@PostMapping("/rateExercise")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> rateExercise(@RequestBody JsonNode node) {
		// TODO: rate exercise from student
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// TODO: change url!
	// Not tested!
	/*
	 * Service for checking if logged in user is in course
	 * Needs course object
	 * 
	 * @param json
	 * @return
	 */
	@PostMapping("/isUserInCourse")
	@PreAuthorize("hasAuthority('TEACHER')")
	public ResponseEntity<?> isUserInCourse(@RequestBody JsonNode node) {
		Course course = mapper.convertValue(node.get("course"), Course.class); 
		if(course == null) {
			return new ResponseEntity<>("Course not found!", HttpStatus.NOT_FOUND); 
		}
		User user = mapper.convertValue(node.get("user"), User.class); 
		if(courseServiceImpl.isUserInCourse(course, user)) {
			return new ResponseEntity<>(HttpStatus.OK); 
		}
		return new ResponseEntity<>("User not in course!", HttpStatus.NOT_FOUND); 
	}

}
