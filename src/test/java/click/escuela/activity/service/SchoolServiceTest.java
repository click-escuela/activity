package click.escuela.activity.service;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import click.escuela.activity.enumerator.SchoolMessage;
import click.escuela.activity.exception.SchoolException;
import click.escuela.activity.model.School;
import click.escuela.activity.repository.SchoolRepository;
import click.escuela.activity.service.impl.SchoolServiceImpl;

@RunWith(PowerMockRunner.class)
public class SchoolServiceTest {

	@Mock
	private SchoolRepository schoolRepository;
	
	private SchoolServiceImpl schoolServiceImpl = new SchoolServiceImpl();
	private School school;
	private Long id;

	@Before
	public void setUp() {

		id = 1L;
		school = School.builder().id(id).name("Colegio Nacional").cellPhone("47589869")
				.email("colegionacional@edu.gob.com").adress("Entre Rios 1418")
				.build();
		
		Optional<School> optional = Optional.of(school);

		Mockito.when(schoolRepository.findById(id)).thenReturn(optional);

		ReflectionTestUtils.setField(schoolServiceImpl, "schoolRepository", schoolRepository);
	}

	@Test
	public void whenGetByIdIsOK() throws SchoolException {
		schoolServiceImpl.getById(id.toString());
		verify(schoolRepository).findById(id);
	}

	@Test
	public void whenGetByIdIsError() {
		id = 2L;
		assertThatExceptionOfType(SchoolException.class).isThrownBy(() -> {
			schoolServiceImpl.getById(id.toString());
		}).withMessage(SchoolMessage.GET_ERROR.getDescription());
	}
	
}