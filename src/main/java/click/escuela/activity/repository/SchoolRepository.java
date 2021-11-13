package click.escuela.activity.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.activity.model.School;

public interface SchoolRepository extends JpaRepository<School, UUID> {

}
