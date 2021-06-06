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

	private static ModelMapper modelMapper = new ModelMapper();

	public static Activity mapperToActivity(ActivityApi activityApi) {
		Activity activity = modelMapper.map(activityApi, Activity.class);
		activity.setType(mapperToEnum(activityApi.getType()));
		activity.setCourseId(UUID.fromString(activityApi.getCourseId()));
		return activity;
	}

	private static ActivityType mapperToEnum(String type) {
		return modelMapper.map(type, ActivityType.class);
	}

	public static ActivityDTO mapperToStudentDTO(Activity activity) {
		ActivityDTO activityDTO = modelMapper.map(activity, ActivityDTO.class);
		activityDTO.setType(activity.getType().toString());
		return activityDTO;
	}

	public static List<ActivityDTO> mapperToActivitiesDTO(List<Activity> listActivites) {
		 List<ActivityDTO> activityDTOList = new ArrayList<>();
		 listActivites.stream().forEach(p -> activityDTOList.add(mapperToStudentDTO(p)));
		 return activityDTOList;
	}


}
