package click.escuela.activity.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.activity.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {

	public List<Activity> findBySchoolId(Long schoolId);

	public List<Activity> findByCourseId(UUID courseId);

	public List<Activity> findByStudentId(UUID studentId);
	
	public Optional<Activity> findByIdAndSchoolId(UUID id, Long schoolId);
}
