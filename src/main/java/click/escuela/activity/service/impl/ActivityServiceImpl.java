package click.escuela.activity.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.activity.api.ActivityApi;
import click.escuela.activity.dto.ActivityDTO;
import click.escuela.activity.enumerator.ActivityMessage;
import click.escuela.activity.exception.ActivityException;
import click.escuela.activity.exception.SchoolException;
import click.escuela.activity.mapper.Mapper;
import click.escuela.activity.model.Activity;
import click.escuela.activity.model.School;
import click.escuela.activity.repository.ActivityRepository;
import click.escuela.activity.service.ActivityServiceGeneric;

@Service
public class ActivityServiceImpl implements ActivityServiceGeneric<ActivityApi, ActivityDTO> {

	@Autowired
	private ActivityRepository activityRepository;

	@Autowired
	private SchoolServiceImpl schoolService;

	@Override
	public void create(String schoolId, ActivityApi activityApi) throws ActivityException, SchoolException {
		School school = schoolService.getById(schoolId);
		try {
			Activity activity = Mapper.mapperToActivity(activityApi);
			activity.setSchool(school);
			activityRepository.save(activity);
		} catch (Exception e) {
			throw new ActivityException(ActivityMessage.CREATE_ERROR);
		}
	}

	@Override
	public List<ActivityDTO> findAll() {
		return Mapper.mapperToActivitiesDTO(activityRepository.findAll());
	}

	public void update(String schoolId, ActivityApi activityApi) throws ActivityException {
		try {
			findByIdAndSchoolId(activityApi.getId(), schoolId).ifPresent(
					activity -> activityRepository.save(Mapper.mapperToActivityUpdate(activity, activityApi)));
		} catch (Exception e) {
			throw new ActivityException(ActivityMessage.UPDATE_ERROR);
		}
	}

	public void delete(String id, String schoolId) throws ActivityException {
		findByIdAndSchoolId(id, schoolId).ifPresent(activity -> activityRepository.delete(activity));
	}

	private Optional<Activity> findByIdAndSchoolId(String id, String schoolId) throws ActivityException {
		return Optional.of(activityRepository.findByIdAndSchoolId(UUID.fromString(id), Long.valueOf(schoolId))
				.orElseThrow(() -> new ActivityException(ActivityMessage.GET_ERROR)));
	}

	public List<ActivityDTO> getBySchool(String schoolId) {
		return Mapper.mapperToActivitiesDTO(activityRepository.findBySchoolId(Long.valueOf(schoolId)));
	}

	public List<ActivityDTO> getByCourse(String courseId) {
		return Mapper.mapperToActivitiesDTO(activityRepository.findByCourseId(UUID.fromString(courseId)));
	}

	public List<ActivityDTO> getByStudent(String studentId) {
		return Mapper.mapperToActivitiesDTO(activityRepository.findByStudentId(UUID.fromString(studentId)));
	}

	public ActivityDTO getById(String activityId, String schoolId) throws ActivityException {
		Activity activity = findByIdAndSchoolId(activityId, schoolId)
				.orElseThrow(() -> new ActivityException(ActivityMessage.GET_ERROR));
		return Mapper.mapperToActivityDTO(activity);
	}

}
