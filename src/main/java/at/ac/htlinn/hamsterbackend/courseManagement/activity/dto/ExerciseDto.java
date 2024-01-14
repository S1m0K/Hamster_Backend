package at.ac.htlinn.hamsterbackend.courseManagement.activity.dto;

import java.util.Date;

import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Exercise;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDto extends ActivityDto {
	public ExerciseDto(Exercise exercise) {
		super(exercise.getId(), exercise.getCourse().getId(),
				exercise.getName(), exercise.getDetails(), exercise.isHidden());
		this.deadline = exercise.getDeadline();
		this.hamster = exercise.getHamster();
	}

	private Date deadline;
	private String hamster;
}
