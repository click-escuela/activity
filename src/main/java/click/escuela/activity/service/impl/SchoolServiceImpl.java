package click.escuela.activity.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import click.escuela.activity.enumerator.SchoolMessage;
import click.escuela.activity.exception.SchoolException;
import click.escuela.activity.model.School;
import click.escuela.activity.repository.SchoolRepository;

@Service
public class SchoolServiceImpl {

	@Autowired
	private SchoolRepository schoolRepository;

	private Optional<School> findById(String id) throws SchoolException {
		return Optional.of(schoolRepository.findById(UUID.fromString(id)))
				.orElseThrow(() -> new SchoolException(SchoolMessage.GET_ERROR));
	}

	public School getById(String id) throws SchoolException {
		Optional<School> schoolOptional = findById(id);
		if (schoolOptional.isPresent()) {
			return schoolOptional.get();
		} else {
			throw new SchoolException(SchoolMessage.GET_ERROR);
		}
	}
	
}
