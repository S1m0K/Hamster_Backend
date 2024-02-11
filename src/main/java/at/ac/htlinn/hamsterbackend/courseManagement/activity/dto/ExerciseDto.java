package at.ac.htlinn.hamsterbackend.courseManagement.activity.dto;

import java.util.Date;

import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Exercise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDto extends ActivityDto {
	
	@Builder
	public ExerciseDto(int id, int courseId, String name, String details, boolean hidden,
			Date deadline, String hamster) {
		super(id, courseId, name, details, hidden);
		this.deadline = deadline;
		this.hamster = hamster;
	}
	
	public ExerciseDto(Exercise exercise) {
		super(exercise.getId(), exercise.getCourse().getId(),
				exercise.getName(), exercise.getDetails(), exercise.isHidden());
		this.deadline = exercise.getDeadline();
		this.hamster = exercise.getHamster();
	}

	private Date deadline;
	private String hamster;
}
