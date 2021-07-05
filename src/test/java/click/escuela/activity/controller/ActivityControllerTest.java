package click.escuela.activity.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import click.escuela.activity.api.ActivityApi;
import click.escuela.activity.dto.ActivityDTO;
import click.escuela.activity.enumerator.ActivityMessage;
import click.escuela.activity.enumerator.ActivityType;
import click.escuela.activity.enumerator.ActivityValidation;
import click.escuela.activity.exception.ActivityException;
import click.escuela.activity.mapper.Mapper;
import click.escuela.activity.model.Activity;
import click.escuela.activity.rest.ActivityController;
import click.escuela.activity.rest.handler.Handler;
import click.escuela.activity.service.impl.ActivityServiceImpl;

@EnableWebMvc
@RunWith(MockitoJUnitRunner.class)
public class ActivityControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private ActivityController activityController;

	@Mock
	private ActivityServiceImpl activityService;

	private ObjectMapper mapper;
	private ActivityApi activityApi;
	private String id;
	private String schoolId;
	private String courseId;
	private String studentId;
	private final static String URL = "/school/{schoolId}/activity";

	@Before
	public void setup() throws ActivityException  {
		mockMvc = MockMvcBuilders.standaloneSetup(activityController).setControllerAdvice(new Handler()).build();
		mapper = new ObjectMapper().findAndRegisterModules().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
				.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false)
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		ReflectionTestUtils.setField(activityController, "activityService", activityService);

		id = UUID.randomUUID().toString();
		schoolId = "1234";
		courseId = UUID.randomUUID().toString();
		studentId = UUID.randomUUID().toString();
		activityApi = ActivityApi.builder().name("Historia de las catatumbas").subject("Historia")
				.type(ActivityType.HOMEWORK.toString()).schoolId(Integer.valueOf(schoolId))
				.courseId(courseId.toString()).studentId(studentId).dueDate(LocalDate.now()).description("Resolver todos los puntos")
				.build();
		Activity activity = Activity.builder().id(UUID.fromString(id)).name("Historia de las catatumbas")
				.subject("Historia").type(ActivityType.HOMEWORK).schoolId(Integer.valueOf(schoolId))
				.courseId(UUID.fromString(courseId)).studentId(UUID.fromString(studentId)).dueDate(LocalDate.now()).description("Resolver todos los puntos")
				.build();
		List<Activity> activities = new ArrayList<>();
		activities.add(activity);

		doNothing().when(activityService).create(Mockito.any());
		Mockito.when(activityService.findAll()).thenReturn(Mapper.mapperToActivitiesDTO(activities));
		Mockito.when(activityService.getById(id)).thenReturn(Mapper.mapperToActivityDTO(activity));
		Mockito.when(activityService.getBySchool(schoolId)).thenReturn(Mapper.mapperToActivitiesDTO(activities));
		Mockito.when(activityService.getByCourse(courseId)).thenReturn(Mapper.mapperToActivitiesDTO(activities));
		Mockito.when(activityService.getByStudent(studentId)).thenReturn(Mapper.mapperToActivitiesDTO(activities));

	}

	@Test
	public void whenCreateIsOk() throws JsonProcessingException, Exception {
		assertThat(resultActivityApi(post(URL, schoolId))).contains(ActivityMessage.CREATE_OK.name());
	}

	@Test
	public void whenCreateButNameEmpty() throws JsonProcessingException, Exception {
		activityApi.setName(StringUtils.EMPTY);
		assertThat(resultActivityApi(post(URL, schoolId))).contains(ActivityValidation.NAME_EMPTY.getDescription());
	}

	@Test
	public void whenCreateButSubjectEmpty() throws JsonProcessingException, Exception {
		activityApi.setSubject(StringUtils.EMPTY);
		assertThat(resultActivityApi(post(URL, schoolId))).contains(ActivityValidation.SUBJECT_EMPTY.getDescription());
	}

	@Test
	public void whenCreateButDescriptionEmpty() throws JsonProcessingException, Exception {
		activityApi.setDescription(StringUtils.EMPTY);
		assertThat(resultActivityApi(post(URL, schoolId)))
				.contains(ActivityValidation.DESCRIPTION_EMPTY.getDescription());
	}

	@Test
	public void whenCreateButCourseEmpty() throws JsonProcessingException, Exception {
		activityApi.setCourseId(StringUtils.EMPTY);
		assertThat(resultActivityApi(post(URL, schoolId)))
				.contains(ActivityValidation.COURSE_ID_EMPTY.getDescription());
	}
	
	@Test
	public void whenCreateButStudentEmpty() throws JsonProcessingException, Exception {
		activityApi.setStudentId(StringUtils.EMPTY);
		assertThat(resultActivityApi(post(URL, schoolId)))
				.contains(ActivityValidation.STUDENT_ID_EMPTY.getDescription());
	}

	@Test
	public void whenCreateButTypeEmpty() throws JsonProcessingException, Exception {
		activityApi.setType(StringUtils.EMPTY);
		String response = resultActivityApi(post(URL, schoolId));
		assertThat(response).contains(ActivityValidation.TYPE_EMPTY.getDescription());
	}

	@Test
	public void whenCreateButSchoolNull() throws JsonProcessingException, Exception {
		activityApi.setSchoolId(null);
		assertThat(resultActivityApi(post(URL, schoolId))).contains(ActivityValidation.SCHOOL_ID_NULL.getDescription());
	}

	@Test
	public void whenCreateErrorService() throws JsonProcessingException, Exception {
		doThrow(new ActivityException(ActivityMessage.CREATE_ERROR)).when(activityService).create(Mockito.any());
		assertThat(resultActivityApi(post(URL, schoolId))).contains(ActivityMessage.CREATE_ERROR.getDescription());
	}

	@Test
	public void whenGetAllIsOk() throws JsonProcessingException, Exception {
		assertThat(mapper
				.readValue(resultActivityApi(get(URL + "/getAll", schoolId)), new TypeReference<List<ActivityDTO>>() {
				}).get(0).getId()).contains(id.toString());
	}
	
	@Test
	public void getByActivityIdIsOk() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(resultActivityApi(get(URL + "/{activityId}", schoolId, id)),ActivityDTO.class)).hasFieldOrPropertyWithValue("id", id.toString());
	}

	@Test
	public void getByActivityIdIsError() throws JsonProcessingException, Exception {
		
		id = UUID.randomUUID().toString();
		doThrow(new ActivityException(ActivityMessage.GET_ERROR))
		.when(activityService).getById(id.toString());
		assertThat(resultActivityApi(get(URL + "/{activityId}", schoolId, id))).contains(ActivityMessage.GET_ERROR.getDescription());
	}

	@Test
	public void getBySchoolIdIsOk() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(resultActivityApi(get(URL, schoolId)), new TypeReference<List<ActivityDTO>>() {
		}).get(0).getId()).contains(id.toString());
	}

	@Test
	public void getBySchoolIdIsEmpty() throws JsonProcessingException, Exception {
		schoolId = "6666";
		assertThat(mapper.readValue(resultActivityApi(get(URL, schoolId)), new TypeReference<List<ActivityDTO>>() {
		})).isEmpty();
	}
	
	@Test
	public void getByCourseIdIsOk() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(resultActivityApi(get(URL + "/course/{courseId}",  schoolId, courseId)), new TypeReference<List<ActivityDTO>>() {
		}).get(0).getId()).contains(id.toString());
	}

	@Test
	public void getByCourseIdIsEmpty() throws JsonProcessingException, Exception {
		courseId = UUID.randomUUID().toString();
		assertThat(mapper.readValue(resultActivityApi(get(URL + "/course/{courseId}",  schoolId, courseId)), new TypeReference<List<ActivityDTO>>() {
		})).isEmpty();
	}
	
	@Test
	public void getByStudentIdIsOk() throws JsonProcessingException, Exception {
		assertThat(mapper.readValue(resultActivityApi(get(URL + "/student/{studentId}",  schoolId, studentId)), new TypeReference<List<ActivityDTO>>() {
		}).get(0).getId()).contains(id.toString());
	}

	@Test
	public void getStudentIdIsEmpty() throws JsonProcessingException, Exception {
		studentId = UUID.randomUUID().toString();
		assertThat(mapper.readValue(resultActivityApi(get(URL + "/student/{studentId}",  schoolId, studentId)), new TypeReference<List<ActivityDTO>>() {
		})).isEmpty();
	}

	@Test
	public void whenUpdateIsOk() throws JsonProcessingException, Exception {
		activityApi.setId(id);
		assertThat(resultActivityApi(put(URL, schoolId))).contains(ActivityMessage.UPDATE_OK.name());
	}

	@Test
	public void whenUpdateIsError() throws JsonProcessingException, Exception {
		doThrow(new ActivityException(ActivityMessage.UPDATE_ERROR)).when(activityService).update(Mockito.any());
		assertThat(resultActivityApi(put(URL, schoolId))).contains(ActivityMessage.UPDATE_ERROR.getDescription());
	}

	@Test
	public void whenDeleteIsOk() throws JsonProcessingException, Exception {
		assertThat(resultActivityApi(delete(URL + "/{activityId}", schoolId, id)))
				.contains(ActivityMessage.DELETE_OK.name());
	}

	@Test
	public void whenDeleteErrorService() throws JsonProcessingException, Exception {
		doThrow(new ActivityException(ActivityMessage.GET_ERROR)).when(activityService).delete(id);
		assertThat(resultActivityApi(delete(URL + "/{activityId}", schoolId, id)))
				.contains(ActivityMessage.GET_ERROR.getDescription());
	}

	private String toJson(final Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

	private String resultActivityApi(MockHttpServletRequestBuilder requestBuilder)
			throws JsonProcessingException, Exception {
		return mockMvc.perform(requestBuilder.contentType(MediaType.APPLICATION_JSON).content(toJson(activityApi)))
				.andReturn().getResponse().getContentAsString();
	}

}
