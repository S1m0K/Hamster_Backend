package at.ac.htlinn.hamsterbackend.courseManagement.course;

import static org.hamcrest.Matchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import at.ac.htlinn.hamsterbackend.courseManagement.course.dto.CourseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/snippets")
@RunWith(SpringRunner.class)
public class CourseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;
	
    @Autowired
    private MockMvc mockMvc;
	
	private final CourseDto course1 = CourseDto.builder()
			.name("Hamster 1")
			.build();
	private final CourseDto course2 = CourseDto.builder()
			.name("Hamster 2")
			.build();
    
    @Test
    @WithMockUser(username = "admin", password = "admin", authorities = "ADMIN")
    public void test() throws Exception {
    	ObjectNode objectNode;
    	String requestBody;
    	MvcResult result;
    	
    	// create course 1
    	objectNode = objectMapper.createObjectNode();
		objectNode.set("course", objectMapper.valueToTree(course1));
		requestBody = objectMapper.writeValueAsString(objectNode);
		
        result = mockMvc.perform(post("/courses")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content(requestBody)
	            .secure(true))
		        .andExpect(status().isOk())
		        .andDo(document("createCourse"))
		        .andReturn();
		int id1 = objectMapper.readValue(result.getResponse().getContentAsString(), int.class);
		
		// create course 2
		objectNode = objectMapper.createObjectNode();
		objectNode.set("course", objectMapper.valueToTree(course2));
		requestBody = objectMapper.writeValueAsString(objectNode);
		
		result = mockMvc.perform(post("/courses")
		        .contentType(MediaType.APPLICATION_JSON)
		        .content(requestBody)
	            .secure(true))
		        .andExpect(status().isOk())
		        .andReturn();
		int id2 = objectMapper.readValue(result.getResponse().getContentAsString(), int.class);
		
		// get course by id
        mockMvc.perform(get("/courses/" + id1)
				.contentType(MediaType.APPLICATION_JSON)
				.secure(true))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name", is(course1.getName())))
				.andDo(document("getCourse"));
        
        // get all courses
        mockMvc.perform(get("/courses")
				.contentType(MediaType.APPLICATION_JSON)
				.secure(true))
				.andExpect(status().isOk())
				.andDo(document("getCourses"));
        
        // update course
    	String updatedName = "updated name";
    	HashMap<String, Object> fields = new HashMap<String, Object>();
    	fields.put("name", updatedName);
		requestBody = objectMapper.writeValueAsString(fields);

        mockMvc.perform(patch("/courses/" + id1)
		        .contentType(MediaType.APPLICATION_JSON)
		        .content(requestBody)
		        .secure(true))
		        .andExpect(status().isOk())
		        .andExpect(jsonPath("$", is(id1)))
		        .andDo(document("updateCourse"));
        // check if course was updated
        mockMvc.perform(get("/courses?name=" + updatedName)
				.contentType(MediaType.APPLICATION_JSON)
				.secure(true))
				.andExpect(status().isOk());
        
        // delete course 1
        mockMvc.perform(delete("/courses/" + id1)
	            .secure(true))
				.andExpect(status().isNoContent())
		        .andDo(document("deleteCourse"));
        
        // delete course 2
        mockMvc.perform(delete("/courses/" + id2)
	            .secure(true))
				.andExpect(status().isNoContent());
    }
}
