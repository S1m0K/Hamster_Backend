package at.ac.htlinn.hamsterbackend.courseManagement.activity;

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

import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Activity;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Exercise;
import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.user.model.User;

@SpringBootTest
public class ActivityServiceTest {
	
	@Autowired
	ActivityService activityService;
	
    @MockBean
    ActivityRepository activityRepository;
    
	private final User user = User.builder()
			.id(1)
			.build();
	private final Course course = Course.builder()
			.id(2)
			.name("Hamster")
			.teacher(user)
			.build();
	private final Exercise exercise = Exercise.builder()
			.id(3)
			.name("HamsterExercise")
			.course(course)
			.build();
    
    @Test
    public void getActivityByIdTest() {
    	when(activityRepository.getById(exercise.getId())).thenReturn(exercise);
    	Exercise found = (Exercise) activityService.getActivityById(exercise.getId());
    	assertEquals(found.getName(), exercise.getName());
    }
    
    @Test
    public void saveActivityTest() {
    	when(activityRepository.save(exercise)).thenReturn(exercise);
    	Exercise saved = (Exercise) activityService.saveActivity(exercise);
    	assertEquals(saved.getName(), exercise.getName());
    }
    
    @Test
    public void updateActivityTest() throws NoSuchFieldException, Exception {
    	Exercise updatedExercise = Exercise.builder()
    			.hamster("Hamster")
    			.build();
    	
    	HashMap<String, Object> fields = new HashMap<String, Object>();
    	fields.put("hamster", updatedExercise.getHamster());
    	
    	when(activityRepository.save(exercise)).thenReturn(exercise);
		Exercise updated = (Exercise) activityService.updateActivity(exercise, fields);
    	assertEquals(updated.getHamster(), updatedExercise.getHamster());
    }
    
    @Test
    public void deleteActivityTest() {
    	boolean success = activityService.deleteActivity(exercise.getId());
    	assertTrue(success);
    }
    
    @Test
    public void getAllActivitiesInCourseTest() {
        List<Activity> activities = Arrays.asList(exercise);
        
        when(activityRepository.getByCourseId(course.getId())).thenReturn(activities);
        List<Activity> found = activityService.getAllActivitiesInCourse(course.getId());
        assertEquals(found.size(), 1);
        assertEquals(found.get(0).getName(), exercise.getName());
    }
}
