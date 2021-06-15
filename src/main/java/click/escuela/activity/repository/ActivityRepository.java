package click.escuela.activity.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import click.escuela.activity.model.Activity;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {

}
