package com.example.hamster_backend.service;


import com.example.hamster_backend.model.entities.*;

import java.util.List;
import java.util.Set;

public interface CourseService {
    List<Course> getAllCourses();

    Course createCourse(Course course);

    boolean deleteCourse(Course course);

    Course getCourseByName(String name);

    User getUserByStudent(Student student);

    List<User> getAllStudents();

    List<Student> getAllStudentsInCourse(Course course);

    List<User> getAllUsersInCourse(Course course);

    List<User> getCourseTeachers(Course course);

    User getUserByTeacher(Teacher teacher);

    boolean setCourseTeacher(Course course, Teacher teacher);

    boolean deleteCourseTeacher(Course course, Teacher teacher);

    boolean addStudentToCourse(Course course, Student student);

    Student getStudentByID(int id);

    boolean addStudentsToCourse(Course course, Set<Student> students);

    boolean removeStudentFromCourse(Course course, Student student);

    boolean removeStudentsFromCourse(Course course, Set<Student> students);

    boolean isUserTeacher(Course course, User user);

    boolean isUserStudent(Course course, User user);

    boolean isUserInCourse(Course course, User user);

    Exercise createExercise(Exercise exercise);

    boolean deleteExercise(Exercise exercise);

    Exercise getExerciseByID(int id);

    Exercise getExerciseByCourse(int course_id, String name);

    Solution createSolution(Solution solution);

    boolean deleteSolution(Solution solution);

    Solution getSolutionByID(int id);

    Solution getSolutionByExercise(int exercise_id, String name);
}
