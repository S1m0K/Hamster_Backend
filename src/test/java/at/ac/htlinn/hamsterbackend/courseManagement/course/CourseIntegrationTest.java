package at.ac.htlinn.hamsterbackend.courseManagement.course;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import at.ac.htlinn.hamsterbackend.user.model.User;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class CourseIntegrationTest {

	@LocalServerPort
	private int port;
	private String url = "https://localhost:" + port;

    @Autowired
    private ObjectMapper objectMapper;
	
    @Autowired
    private MockMvc mockMvc;
    
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
    public void createAndDeleteCourse() throws Exception {
    	
		JsonNode node = objectMapper.valueToTree(course);
		ObjectNode objectNode = objectMapper.createObjectNode();
		objectNode.set("course", node);
		String requestBody = objectMapper.writeValueAsString(objectNode);
        
        var result = mockMvc.perform(post(url + "/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
		int id = objectMapper.readValue(result.getResponse().getContentAsString(), int.class);
        
        // remove course
        mockMvc.perform(delete(url + "/courses/" + id))
		.andExpect(status().is(HttpStatus.NO_CONTENT.value()));
    }
}
