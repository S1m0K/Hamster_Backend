package at.ac.htlinn.hamsterbackend.courseManagement.course;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.courseManagement.solution.dto.SolutionDto;
import at.ac.htlinn.hamsterbackend.user.model.User;

@SpringBootTest
public class CourseServiceTest {
	
	@Autowired
	CourseService courseService;
	
    @MockBean
    CourseRepository courseRepository;
    
	private final User user = User.builder()
			.id(1)
			.build();
	
	private final Course course = Course.builder()
			.id(1)
			.name("Hamster")
			.teacher(user)
			.build();
    
    @Test
    public void getCourseByIdTest() {
    	when(courseRepository.getById(course.getId())).thenReturn(course);
    	
    	Course found = courseService.getCourseById(course.getId());
    	
    	assertEquals(found.getName(), course.getName());
    }
    
    @Test
    public void getAllCoursesTest() {
        List<Course> courses = Arrays.asList(course);
        when(courseRepository.findAll()).thenReturn(courses);
        
        List<Course> found = courseService.getAllCourses();
        
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getName(), course.getName());
    }
    
    @Test
    public void getCourseByNameTest() {
    	when(courseRepository.getByName(course.getName())).thenReturn(course);
    	
    	Course found = courseService.getCourseByName(course.getName());
    			
    	assertEquals(found.getName(), course.getName());
    }
    
    @Test
    public void getCoursesByStudentIdTest() {
        List<Course> courses = Arrays.asList(course);
    	when(courseRepository.getCoursesByStudentId(1)).thenReturn(courses);
    	
    	List<Course> found = courseService.getCoursesByStudentId(1);

        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getName(), course.getName());
    }
    
    @Test
    public void getCoursesByTeacherIdTest() {
        List<Course> courses = Arrays.asList(course);
    	when(courseRepository.getCoursesByTeacherId(1)).thenReturn(courses);
    	
    	List<Course> found = courseService.getCoursesByTeacherId(1);

        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getName(), course.getName());
    }
    
    @Test
    public void getCourseBySolutionTest() {
    	SolutionDto solution = SolutionDto.builder()
    			.id(1)
    			.activityId(1)
    			.build();
    	
    	when(courseRepository.getByActivityId(solution.getId())).thenReturn(course);
    	
    	Course found = courseService.getCourseBySolution(solution);
    			
    	assertEquals(found.getName(), course.getName());
    }
    
    @Test
    public void saveCourseTest() {
    	when(courseRepository.save(course)).thenReturn(course);
    	
    	Course saved = courseService.saveCourse(course);
    			
    	assertEquals(saved.getName(), course.getName());
    }
    
    @Test
    public void updateCourseTest() throws NoSuchFieldException, Exception {
    	Course updatedCourse = Course.builder()
    			.id(1)
    			.name("HamsterUpdated")
    			.teacher(user)
    			.build();
    	
    	HashMap<String, Object> fields = new HashMap<String, Object>();
    	fields.put("name", updatedCourse.getName());
    	
    	when(courseRepository.save(course)).thenReturn(course);
    	
		Course updated = courseService.updateCourse(course, fields);
    			
    	assertEquals(updated.getName(), updatedCourse.getName());
    }
    
    @Test
    public void deleteCourseTest() {
    	boolean success = courseService.deleteCourse(course);
    	
    	assertTrue(success);
    }
}
