package click.escuela.activity.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.activity.api.ActivityApi;
import click.escuela.activity.dto.ActivityDTO;
import click.escuela.activity.enumerator.ActivityMessage;
import click.escuela.activity.exception.TransactionException;
import click.escuela.activity.mapper.Mapper;
import click.escuela.activity.model.Activity;
import click.escuela.activity.repository.ActivityRepository;
import click.escuela.activity.service.ActivityServiceGeneric;

@Service
public class ActivityServiceImpl implements ActivityServiceGeneric<ActivityApi, ActivityDTO> {

	@Autowired
	private ActivityRepository activityRepository;

	@Override
	public void create(ActivityApi activityApi) throws TransactionException {
		try {
			Activity activity = Mapper.mapperToActivity(activityApi);
			activityRepository.save(activity);
		} catch (Exception e) {
			throw new TransactionException(ActivityMessage.CREATE_ERROR.getCode(),
					ActivityMessage.CREATE_ERROR.getDescription());
		}
	}

	@Override
	public List<ActivityDTO> findAll() {
		List<Activity> listActivites = activityRepository.findAll();
		return Mapper.mapperToActivitiesDTO(listActivites);
	}

}
