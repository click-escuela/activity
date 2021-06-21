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
import click.escuela.activity.mapper.Mapper;
import click.escuela.activity.model.Activity;
import click.escuela.activity.repository.ActivityRepository;
import click.escuela.activity.service.ActivityServiceGeneric;

@Service
public class ActivityServiceImpl implements ActivityServiceGeneric<ActivityApi, ActivityDTO> {

	@Autowired
	private ActivityRepository activityRepository;

	@Override
	public void create(ActivityApi activityApi) throws ActivityException {
		try {
			activityRepository.save(Mapper.mapperToActivity(activityApi));
		} catch (Exception e) {
			throw new ActivityException(ActivityMessage.CREATE_ERROR);
		}
	}

	@Override
	public List<ActivityDTO> findAll() {
		return Mapper.mapperToActivitiesDTO(activityRepository.findAll());
	}

	public void update(ActivityApi activityApi) throws ActivityException {
		try {
			findById(activityApi.getId())
					.ifPresent(activity -> activityRepository.save(Mapper.mapperToActivityUpdate(activity, activityApi)));
		} catch (Exception e) {
			throw new ActivityException(ActivityMessage.UPDATE_ERROR);
		}
	}

	public void delete(String id) throws ActivityException {
		findById(id).ifPresent(activity -> activityRepository.delete(activity));
	}

	public Optional<Activity> findById(String id) throws ActivityException {
		return Optional.of(activityRepository.findById(UUID.fromString(id))
				.orElseThrow(() -> new ActivityException(ActivityMessage.GET_ERROR)));
	}

}
