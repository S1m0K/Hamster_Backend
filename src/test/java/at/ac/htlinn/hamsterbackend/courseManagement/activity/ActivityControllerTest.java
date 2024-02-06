package at.ac.htlinn.hamsterbackend.courseManagement.activity;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import at.ac.htlinn.hamsterbackend.courseManagement.activity.dto.ExerciseDto;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Activity;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Exercise;
import at.ac.htlinn.hamsterbackend.courseManagement.course.CourseService;
import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.courseManagement.student.StudentService;
import at.ac.htlinn.hamsterbackend.courseManagement.teacher.TeacherService;
import at.ac.htlinn.hamsterbackend.security.CustomPasswordEncoder;
import at.ac.htlinn.hamsterbackend.user.MyUserDetailsService;
import at.ac.htlinn.hamsterbackend.user.UserService;
import at.ac.htlinn.hamsterbackend.user.model.User;

@RunWith(SpringRunner.class)
@WebMvcTest(ActivityController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class ActivityControllerTest {
	
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;
    @MockBean
    private ActivityService activityService;
    @MockBean
	private StudentService studentService;
    @MockBean
	private TeacherService teacherService;
    @MockBean
	private UserService userService;
    
    @MockBean
    private CustomPasswordEncoder customPasswordEncoder;
    @MockBean
    private MyUserDetailsService myUserDetailsService;
    
    @MockBean
    private Principal principal;
    
	private final User user = User.builder()
			.id(1)
			.build();
	
	private final Course course = Course.builder()
			.id(1)
			.name("Hamster")
			.teacher(user)
			.build();
	
	private final Exercise exercise = Exercise.builder()
			.id(1)
			.name("HamsterExercise")
			.course(course)
			.hamster("abc")
			.build();
	
	@BeforeEach
	public void Setup() {
		when(userService.findUserByID(1)).thenReturn(user);
		when(userService.findUserByUsername("admin")).thenReturn(user);
		when(userService.isUserPrivileged(user)).thenReturn(true);
	}

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void getExerciseByIdTest() throws Exception {
        when(activityService.getActivityById(1)).thenReturn(exercise);

        mockMvc.perform(get("/activities/" + exercise.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .secure(true))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.name", is(exercise.getName())))
		  .andDo(document("getActivity"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void getActivitiesByCourseIdTest() throws Exception {
        when(courseService.getCourseById(course.getId())).thenReturn(course);
        List<Activity> activities = Arrays.asList(exercise);
        when(activityService.getAllActivitiesInCourse(course.getId())).thenReturn(activities);

        mockMvc.perform(get("/activities?course_id=" + course.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .secure(true))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(1)))
          .andExpect(jsonPath("$[0].name", is(exercise.getName())))
          .andDo(document("getActivitiesByCourse"));
    }
    
    // TODO: doesn't work yet
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void createActivityTest() throws Exception {
        when(courseService.getCourseById(course.getId())).thenReturn(course);
		when(activityService.saveActivity(exercise)).thenReturn(exercise);
    	
		JsonNode node = objectMapper.valueToTree(new ExerciseDto(exercise));
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("exercise", node);
		String requestBody = objectMapper.writeValueAsString(objectNode);

        mockMvc.perform(post("/activities")
          .contentType(MediaType.APPLICATION_JSON)
          .content(requestBody)
          .principal(principal)
          .secure(true))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", is(1)))
          .andDo(document("createActivity"));
    }
    
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void updateActivityTest() throws Exception {
    	Exercise updatedExercise = Exercise.builder()
    			.id(1)
    			.name("HamsterExerciseUpdated")
    			.build();
    	
    	HashMap<String, Object> fields = new HashMap<String, Object>();
    	fields.put("name", updatedExercise.getName());
    	
    	when(activityService.getActivityById(exercise.getId())).thenReturn(exercise);
    	when(activityService.updateActivity(exercise, fields)).thenReturn(updatedExercise);
    	
		String requestBody = objectMapper.writeValueAsString(fields);

        mockMvc.perform(patch("/activities/" + exercise.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .content(requestBody)
          .principal(principal)
          .secure(true))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", is(1)))
          .andDo(document("updateActivity"));
    }
    
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void deleteCourseTest() throws Exception {
    	when(activityService.getActivityById(exercise.getId())).thenReturn(exercise);
    	when(activityService.deleteActivity(exercise.getId())).thenReturn(true);

        mockMvc.perform(delete("/activities/" + exercise.getId())
          .contentType(MediaType.APPLICATION_JSON)
          .principal(principal)
          .secure(true))
          .andExpect(status().isNoContent())
          .andDo(document("deleteActivity"));
    }
}
