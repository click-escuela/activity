package click.escuela.activity.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.activity.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {

	public List<Activity> findBySchoolId(Integer schoolId);

	public List<Activity> findByCourseId(UUID courseId);

	public List<Activity> findByStudentId(UUID studentId);
}
