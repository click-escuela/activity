package click.escuela.activity.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import click.escuela.activity.api.ActivityApi;
import click.escuela.activity.dto.ActivityDTO;
import click.escuela.activity.enumerator.ActivityType;
import click.escuela.activity.model.Activity;

@Component
public class Mapper {

	private Mapper() {

	}

	private static ModelMapper modelMapper = new ModelMapper();

	public static Activity mapperToActivity(ActivityApi activityApi) {
		Activity activity = modelMapper.map(activityApi, Activity.class);
		activity.setCourseId(UUID.fromString(activityApi.getCourseId()));
		activity.setStudentId(UUID.fromString(activityApi.getStudentId()));
		activity.setType(mapperToEnum(activityApi.getType()));
		return activity;
	}

	public static ActivityType mapperToEnum(String type) {
		return modelMapper.map(ActivityType.valueOf(type), ActivityType.class);
	}

	public static ActivityDTO mapperToActivityDTO(Activity activity) {
		return modelMapper.map(activity, ActivityDTO.class);
	}

	public static List<ActivityDTO> mapperToActivitiesDTO(List<Activity> listActivites) {
		List<ActivityDTO> activityDTOList = new ArrayList<>();
		listActivites.stream().forEach(p -> activityDTOList.add(mapperToActivityDTO(p)));
		return activityDTOList;
	}

	public static Activity mapperToActivityUpdate(Activity activity, ActivityApi activityApi) {
		modelMapper.map(activityApi,activity);
		activity.setCourseId(UUID.fromString(activityApi.getCourseId()));
		activity.setStudentId(UUID.fromString(activityApi.getStudentId()));
		activity.setType(mapperToEnum(activityApi.getType()));
		return activity;
	}

}
