package click.escuela.activity.service;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDate;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.activity.api.ActivityApi;
import click.escuela.activity.enumerator.ActivityType;
import click.escuela.activity.exception.ActivityException;
import click.escuela.activity.mapper.Mapper;
import click.escuela.activity.model.Activity;

@RunWith(PowerMockRunner.class)
public class MapperTest {

	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private Mapper mapper;
	
	private ActivityApi activityApi;
	private Activity activity;
	private UUID id;
	private UUID courseId;
	private Integer schoolId;

	@Before
	public void setUp() throws ActivityException {

		id = UUID.randomUUID();
		courseId = UUID.randomUUID();
		schoolId = 1234;
		activity = Activity.builder().id(id).name("Historia de las catatumbas").subject("Historia")
				.type(ActivityType.HOMEWORK).schoolId(schoolId).courseId(courseId).dueDate(LocalDate.now())
				.description("Resolver todos los puntos").build();
		activityApi = ActivityApi.builder().name("Historia de las catatumbas").subject("Historia")
				.type(ActivityType.HOMEWORK.toString()).schoolId(schoolId).courseId(courseId.toString())
				.dueDate(LocalDate.now()).description("Resolver todos los puntos").build();
		Mockito.when(modelMapper.map(activityApi, Activity.class)).thenReturn(activity);
		ReflectionTestUtils.setField(mapper, "modelMapper", modelMapper);

	}

	@Test
	public void whenMapperActivityIsOk() throws ActivityException {
		Boolean hasTrue= true;
		try {
		Mapper.mapperToActivity(activityApi);
		}
		catch(Exception e) {
			hasTrue = false;
		}
		assertThat(hasTrue).isTrue();
	}
	
	@Test
	public void whenMapperActivityUpdateIsOk() throws ActivityException {
		Boolean hasTrue= true;
		try {
		Mapper.mapperToActivityUpdate(activity,activityApi);
		}
		catch(Exception e) {
			hasTrue = false;
		}
		assertThat(hasTrue).isTrue();
	}

}
