package at.ac.htlinn.hamsterbackend.courseManagement.activity.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonTypeName;

import at.ac.htlinn.hamsterbackend.courseManagement.activity.dto.ExerciseDto;
import at.ac.htlinn.hamsterbackend.courseManagement.course.CourseService;
import at.ac.htlinn.hamsterbackend.courseManagement.course.model.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "exercise")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonTypeName("exercise") 
public class Exercise extends Activity {
	public static final String type = "exercise";
	
	@Builder
	public Exercise(int id, String name, String details, boolean hidden, Course course,
			Date deadline, String hamster) {
		super(id, name, details, hidden, course);
		this.deadline = deadline;
		this.hamster = hamster;
	}
	
	public Exercise(ExerciseDto exercise, CourseService courseService) {
		super(exercise.getId(), exercise.getName(), exercise.getDetails(), exercise.isHidden(),
				courseService.getCourseById(exercise.getCourseId()));
		this.deadline = exercise.getDeadline();
		this.hamster = exercise.getHamster();
	}
	
	@Column(name = "deadline")
	private Date deadline;
	
	@Column(name = "hamster", nullable = false)
	private String hamster;
}
