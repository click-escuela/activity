package click.escuela.activity.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.activity.api.ActivityApi;
import click.escuela.activity.enumerator.ActivityMessage;
import click.escuela.activity.enumerator.ActivityType;
import click.escuela.activity.exception.ActivityException;
import click.escuela.activity.mapper.Mapper;
import click.escuela.activity.model.Activity;
import click.escuela.activity.repository.ActivityRepository;
import click.escuela.activity.service.impl.ActivityServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class ActivityServiceTest {

	@Mock
	private ActivityRepository activityRepository;

	private ActivityServiceImpl activityServiceImpl = new ActivityServiceImpl();
	private ActivityApi activityApi;
	private Activity activity;
	private UUID id;
	private UUID courseId;
	private Integer schoolId;

	@Before
	public void setUp() throws ActivityException {
		PowerMockito.mockStatic(Mapper.class);

		id = UUID.randomUUID();
		courseId = UUID.randomUUID();
		schoolId = 1234;
		activity = Activity.builder().id(id).name("Historia de las catatumbas").subject("Historia")
				.type(ActivityType.HOMEWORK).schoolId(schoolId).courseId(courseId).dueDate(LocalDate.now())
				.description("Resolver todos los puntos").build();
		activityApi = ActivityApi.builder().name("Historia de las catatumbas").subject("Historia")
				.type(ActivityType.HOMEWORK.toString()).schoolId(schoolId).courseId(courseId.toString())
				.dueDate(LocalDate.now()).description("Resolver todos los puntos").build();
		Optional<Activity> optional = Optional.of(activity);
		
		Mockito.when(activityRepository.findById(id)).thenReturn(optional);
		Mockito.when(activityRepository.save(activity)).thenReturn(activity);
		
		ReflectionTestUtils.setField(activityServiceImpl, "activityRepository", activityRepository);
	}

	@Test
	public void whenCreateIsOk() throws ActivityException {
		Mockito.when(Mapper.mapperToActivity(activityApi)).thenReturn(activity);
		activityServiceImpl.create(activityApi);
		verify(activityRepository).save(activity);
	}

	@Test
	public void whenCreateIsError() {
		Mockito.when(activityRepository.save(null)).thenThrow(IllegalArgumentException.class);
		assertThatExceptionOfType(ActivityException.class).isThrownBy(() -> {
			activityServiceImpl.create(new ActivityApi());
		}).withMessage(ActivityMessage.CREATE_ERROR.getDescription());
	}
	
	@Test
	public void whenUpdateIsOk() throws ActivityException {
		Mockito.when(Mapper.mapperToActivityUpdate(activity,activityApi)).thenReturn(activity);
		activityApi.setId(id.toString());
		activityServiceImpl.update(activityApi);
		verify(activityRepository).save(activity);
	}

	@Test
	public void whenUpdateIsError() {
		Mockito.when(Mapper.mapperToActivityUpdate(activity,activityApi)).thenReturn(activity);
		assertThatExceptionOfType(ActivityException.class).isThrownBy(() -> {
			activityServiceImpl.update(new ActivityApi());
		}).withMessage(ActivityMessage.UPDATE_ERROR.getDescription());
	}
	
	@Test
	public void whenFinByIdIsOk() throws ActivityException {
		activityServiceImpl.findById(id.toString());
		verify(activityRepository).findById(id);
	}

	@Test
	public void whenFinByIdIsError() {
		assertThatExceptionOfType(ActivityException.class).isThrownBy(() -> {
			activityServiceImpl.findById(UUID.randomUUID().toString());
		}).withMessage(ActivityMessage.GET_ERROR.getDescription());
	}
	
	@Test
	public void whenGetAllIsOk() {
		activityServiceImpl.findAll();
		verify(activityRepository).findAll();
	}
	
	@Test
	public void whenDeleteIsOk() throws ActivityException{
		activityServiceImpl.delete(id.toString());
		verify(activityRepository).delete(activity);
	}
	
	@Test
	public void whenDeleteIsError(){
		assertThatExceptionOfType(ActivityException.class).isThrownBy(() -> {
			activityServiceImpl.delete(UUID.randomUUID().toString());
		}).withMessage(ActivityMessage.GET_ERROR.getDescription());
	}

}
