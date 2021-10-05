package click.escuela.activity.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
import click.escuela.activity.dto.ActivityDTO;
import click.escuela.activity.enumerator.ActivityMessage;
import click.escuela.activity.enumerator.ActivityType;
import click.escuela.activity.exception.ActivityException;
import click.escuela.activity.exception.SchoolException;
import click.escuela.activity.mapper.Mapper;
import click.escuela.activity.model.Activity;
import click.escuela.activity.model.School;
import click.escuela.activity.repository.ActivityRepository;
import click.escuela.activity.service.impl.ActivityServiceImpl;
import click.escuela.activity.service.impl.SchoolServiceImpl;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Mapper.class })
public class ActivityServiceTest {

	@Mock
	private ActivityRepository activityRepository;
	
	@Mock
	private SchoolServiceImpl schoolService;

	private ActivityServiceImpl activityServiceImpl = new ActivityServiceImpl();
	private ActivityApi activityApi;
	private Activity activity;
	private UUID id;
	private UUID courseId;
	private UUID studentId;
	private UUID schoolId;
	private List<Activity> activities = new ArrayList<>();

	@Before
	public void setUp() throws ActivityException, SchoolException {
		PowerMockito.mockStatic(Mapper.class);

		id = UUID.randomUUID();
		courseId = UUID.randomUUID();
		studentId = UUID.randomUUID();
		schoolId = UUID.randomUUID();
		School school = new School();
		school.setId(schoolId);
		activity = Activity.builder().id(id).name("Historia de las catatumbas").subject("Historia")
				.type(ActivityType.HOMEWORK).school(school).courseId(courseId).studentId(studentId).dueDate(LocalDate.now())
				.description("Resolver todos los puntos").build();
		activityApi = ActivityApi.builder().name("Historia de las catatumbas").subject("Historia")
				.type(ActivityType.HOMEWORK.toString()).courseId(courseId.toString()).studentId(studentId.toString())
				.dueDate(LocalDate.now()).description("Resolver todos los puntos").build();
		Optional<Activity> optional = Optional.of(activity);
		activities.add(activity);
		
		Mockito.when(activityRepository.findById(id)).thenReturn(optional);
		Mockito.when(activityRepository.save(activity)).thenReturn(activity);
		Mockito.when(activityRepository.findByCourseId(courseId)).thenReturn(activities);
		Mockito.when(activityRepository.findBySchoolId(schoolId)).thenReturn(activities);
		Mockito.when(activityRepository.findByStudentId(studentId)).thenReturn(activities);
		Mockito.when(activityRepository.findByIdAndSchoolId(id, schoolId)).thenReturn(optional);
		Mockito.when(schoolService.getById(schoolId.toString())).thenReturn(school);
		
		ReflectionTestUtils.setField(activityServiceImpl, "activityRepository", activityRepository);
		ReflectionTestUtils.setField(activityServiceImpl, "schoolService", schoolService);
	}

	@Test
	public void whenCreateIsOk() throws ActivityException, SchoolException {
		Mockito.when(Mapper.mapperToActivity(activityApi)).thenReturn(activity);
		activityServiceImpl.create(schoolId.toString(), activityApi);
		verify(activityRepository).save(activity);
	}

	@Test
	public void whenCreateIsError() {
		Mockito.when(activityRepository.save(null)).thenThrow(IllegalArgumentException.class);
		assertThatExceptionOfType(ActivityException.class).isThrownBy(() -> {
			activityServiceImpl.create(schoolId.toString(), new ActivityApi());
		}).withMessage(ActivityMessage.CREATE_ERROR.getDescription());
	}
	
	@Test
	public void whenUpdateIsOk() throws ActivityException {
		Mockito.when(Mapper.mapperToActivityUpdate(activity,activityApi)).thenReturn(activity);
		activityApi.setId(id.toString());
		activityServiceImpl.update(schoolId.toString(), activityApi);
		verify(activityRepository).save(activity);
	}

	@Test
	public void whenUpdateIsError() {
		Mockito.when(Mapper.mapperToActivityUpdate(activity,activityApi)).thenReturn(activity);
		assertThatExceptionOfType(ActivityException.class).isThrownBy(() -> {
			activityServiceImpl.update(schoolId.toString(), new ActivityApi());
		}).withMessage(ActivityMessage.UPDATE_ERROR.getDescription());
	}
	
	@Test
	public void whenGetByIdIsOk() throws ActivityException {
		activityServiceImpl.getById(id.toString(), schoolId.toString());
		verify(activityRepository).findByIdAndSchoolId(id, schoolId);
	}

	@Test
	public void whenGetByIdIsError() {
		assertThatExceptionOfType(ActivityException.class).isThrownBy(() -> {
			activityServiceImpl.getById(UUID.randomUUID().toString(), schoolId.toString() );
		}).withMessage(ActivityMessage.GET_ERROR.getDescription());
	}
	
	@Test
	public void whenGetByCourseIdIsOk() {
		activityServiceImpl.getByCourse(courseId.toString());
		verify(activityRepository).findByCourseId(courseId);
	}

	@Test
	public void whenGetByCourseIsEmpty() {
		courseId = UUID.randomUUID();
		List<ActivityDTO> listEmpty = activityServiceImpl.getByCourse(UUID.randomUUID().toString());
		assertThat(listEmpty).isEmpty();
	}
	
	@Test
	public void whenGetByStudentIdIsOk() {
		activityServiceImpl.getByStudent(studentId.toString());
		verify(activityRepository).findByStudentId(studentId);
	}

	@Test
	public void whenGetByStudentIsEmpty() {
		studentId = UUID.randomUUID();
		List<ActivityDTO> listEmpty = activityServiceImpl.getByStudent(studentId.toString());
		assertThat(listEmpty).isEmpty();
	}
	
	@Test
	public void whenGetBySchoolIdIsOk() throws ActivityException {
		activityServiceImpl.getBySchool(schoolId.toString());
		verify(activityRepository).findBySchoolId(schoolId);
	}

	@Test
	public void whenGetBySchoolIdIsEmty() {
		schoolId = UUID.randomUUID();
		List<ActivityDTO> listEmpty = activityServiceImpl.getBySchool(schoolId.toString());
		assertThat(listEmpty).isEmpty();
	}
	
	@Test
	public void whenGetAllIsOk() {
		activityServiceImpl.findAll();
		verify(activityRepository).findAll();
	}
	
	@Test
	public void whenDeleteIsOk() throws ActivityException{
		activityServiceImpl.delete(id.toString(), schoolId.toString());
		verify(activityRepository).delete(activity);
	}
	
	@Test
	public void whenDeleteIsError(){
		assertThatExceptionOfType(ActivityException.class).isThrownBy(() -> {
			activityServiceImpl.delete(UUID.randomUUID().toString(), schoolId.toString());
		}).withMessage(ActivityMessage.GET_ERROR.getDescription());
	}
	
}
