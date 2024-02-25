package at.ac.htlinn.hamsterbackend.courseManagement.activity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import at.ac.htlinn.hamsterbackend.courseManagement.activity.dto.ContestParticipant;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.dto.ContestResults;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Activity;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Contest;
import at.ac.htlinn.hamsterbackend.courseManagement.activity.model.Exercise;
import at.ac.htlinn.hamsterbackend.courseManagement.solution.SolutionService;
import at.ac.htlinn.hamsterbackend.courseManagement.solution.dto.SolutionDto;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ActivityService {

	private SolutionService solutionService;
	private ActivityRepository activityRepository;

	public Activity getActivityById(int activityId) { 
		return (Activity)Hibernate.unproxy(activityRepository.getById(activityId));
	}
	
	public Activity saveActivity(Activity activity) {
		try {
			return activityRepository.save(activity);
		} catch(Exception e) {
			return null;
		}
	}
	
	public Activity updateActivity(Activity activity, Map<String, Object> fields) throws NoSuchFieldException, Exception {
		// attempt to update all specified fields
		// TODO: key currently needs to match actual field names instead of JSON field names
		for (Map.Entry<String, Object> set : fields.entrySet()) {
			try {
				Field field;
				try {
					// look for field in superclass Activity
					field = Activity.class.getDeclaredField(set.getKey());
				}
				catch (NoSuchFieldException e) {
					// look for field in subclass
					field = activity instanceof Exercise ?
						Exercise.class.getDeclaredField(set.getKey()) : Contest.class.getDeclaredField(set.getKey());
				}
				
		    	field.setAccessible(true);
		    	field.set(activity, set.getValue());
			}
			catch (NoSuchFieldException e) {
				System.out.println(e);
				throw new NoSuchFieldException(set.getKey());
			}
			catch (Exception e) {
				throw new Exception(set.getKey());
			}
		}
		
		return saveActivity(activity);
	}

	public boolean deleteActivity(int activityId) {
		try {
			Activity activity = activityRepository.getById(activityId);
			activityRepository.delete(activity);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public List<Activity> getAllActivitiesInCourse(int courseId) {
		
		// get exercises
		List<Activity> activities = new ArrayList<Activity>();
		for(Activity activity : activityRepository.getByCourseId(courseId)) {
			activities.add(activity);
		}
		
		return activities;
	}
	
	public ContestResults getContestResults(Contest contest, int userId) {
		List<SolutionDto> solutions = solutionService.getSolutionsByActivityId(contest.getId());
		
		// remove all solutions that have not yet been submitted
		solutions.stream()
	    	      .filter(solution -> solution.isSubmitted())
	    	      .collect(Collectors.toList());
		
		Collections.sort(solutions, new Comparator<SolutionDto>() {
		   public int compare(SolutionDto o1, SolutionDto o2){
			  return o1.getSubmissionDate().compareTo(o2.getSubmissionDate());
		   }
		});
		
		Date start = contest.getStart();
		ContestResults results = new ContestResults();

		ContestParticipant firstPlace = new ContestParticipant();
		firstPlace.setUsername(solutions.get(0).getStudentName());
		firstPlace.setTime((solutions.get(0).getSubmissionDate().getTime() - start.getTime()) / 1000);
		results.setFirstPlace(firstPlace);
		
		if (solutions.size() > 1) {
			ContestParticipant secondPlace = new ContestParticipant();
			secondPlace.setUsername(solutions.get(1).getStudentName());
			secondPlace.setTime((solutions.get(1).getSubmissionDate().getTime() - start.getTime()) / 1000);
			results.setSecondPlace(secondPlace);
		}
		
		if (solutions.size() > 2) {
			ContestParticipant thirdPlace = new ContestParticipant();
			thirdPlace.setUsername(solutions.get(2).getStudentName());
			thirdPlace.setTime((solutions.get(2).getSubmissionDate().getTime() - start.getTime()) / 1000);
			results.setThirdPlace(thirdPlace);
		}
		
		SolutionDto userSolution = new SolutionDto(solutionService.getSolutionByActivityAndStudentId(contest.getId(), userId));
		int userPlace = solutions.indexOf(userSolution);
		long userTime = (userSolution.getSubmissionDate().getTime() - start.getTime()) / 1000;
		results.setUserPlace(userPlace + 1);
		results.setUserTime(userTime);
		
		return results;
	}
}
