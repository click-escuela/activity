package click.escuela.activity.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import click.escuela.activity.enumerator.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "activity")
@Entity
@Builder
public class Activity {
	@Id
	@Column(name = "id", columnDefinition = "BINARY(16)")
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	private UUID id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "school_id", nullable = false)
	private Integer schoolId;

	@Column(name = "course_id", columnDefinition = "BINARY(16)", nullable = false)
	private UUID courseId;

	@Column(name = "due_date", nullable = false, columnDefinition = "DATETIME")
	private LocalDate dueDate;

	@Column(name = "type", nullable = false)
	@Enumerated(EnumType.STRING)
	private ActivityType type;

	@Column(name = "subject", nullable = false)
	private String subject;

	@Column(name = "description", nullable = false)
	private String description;
}
