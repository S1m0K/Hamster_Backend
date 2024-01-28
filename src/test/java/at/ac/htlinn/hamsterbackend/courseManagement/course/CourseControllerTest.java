package at.ac.htlinn.hamsterbackend.courseManagement.course;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

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

import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.courseManagement.student.StudentService;
import at.ac.htlinn.hamsterbackend.security.CustomPasswordEncoder;
import at.ac.htlinn.hamsterbackend.user.MyUserDetailsService;
import at.ac.htlinn.hamsterbackend.user.UserService;
import at.ac.htlinn.hamsterbackend.user.model.User;

@RunWith(SpringRunner.class)
@WebMvcTest(CourseController.class)
@AutoConfigureRestDocs(outputDir = "target/snippets")
public class CourseControllerTest {
	
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;
    @MockBean
	private StudentService studentService;
    @MockBean
	private UserService userService;
    
    // TODO: test doesn't launch without these?
    @MockBean
    private CustomPasswordEncoder customPasswordEncoder;
    @MockBean
    private MyUserDetailsService myUserDetailsService;
    
	private final User teacher = User.builder()
			.id(1)
			.build();
	
	private final Course course = Course.builder()
			.id(1)
			.name("hans")
			.teacher(teacher)
			.build();
    
    @Test
    @WithMockUser(authorities = "ADMIN")
    public void givenCourses_whenGetCourses_thenReturnJsonArray() throws Exception {

        List<Course> allCourses = Arrays.asList(course);

        when(courseService.getAllCourses()).thenReturn(allCourses);

        mockMvc.perform(get("/courses")
          .contentType(MediaType.APPLICATION_JSON)
          .secure(true))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(1)))
          .andExpect(jsonPath("$[0].name", is(course.getName())))
          .andDo(document("getCourses"));
    }
}
