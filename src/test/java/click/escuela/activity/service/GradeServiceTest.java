package click.escuela.activity.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
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
import click.escuela.activity.exception.TransactionException;
import click.escuela.activity.mapper.Mapper;
import click.escuela.activity.model.Activity;
import click.escuela.activity.repository.ActivityRepository;
import click.escuela.activity.service.impl.ActivityServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class GradeServiceTest {

	@Mock
	private ActivityRepository activityRepository;

	private ActivityServiceImpl activityServiceImpl = new ActivityServiceImpl();
	private ActivityApi activityApi;
	private Activity activity;
	private UUID id;
	private UUID courseId;
	private Integer schoolId;

	@Before
	public void setUp() {
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

		Mockito.when(Mapper.mapperToActivity(activityApi)).thenReturn(activity);
		Mockito.when(activityRepository.save(activity)).thenReturn(activity);

		ReflectionTestUtils.setField(activityServiceImpl, "activityRepository", activityRepository);
	}

	@Test
	public void whenCreateIsOk() throws TransactionException {
		activityServiceImpl.create(activityApi);
		verify(activityRepository).save(Mapper.mapperToActivity(activityApi));
	}

	@Test
	public void whenCreateIsError() {
		Mockito.when(activityRepository.save(null)).thenThrow(IllegalArgumentException.class);

		assertThatExceptionOfType(TransactionException.class).isThrownBy(() -> {
			activityServiceImpl.create(Mockito.any());
		}).withMessage(ActivityMessage.CREATE_ERROR.getDescription());
	}
	
	@Test
	public void whenGetAllIsOk() {
		activityServiceImpl.findAll();
		verify(activityRepository).findAll();
	}

}
