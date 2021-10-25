package click.escuela.activity.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityDTO {
	@JsonProperty(value = "id")
	private String id;

	@JsonProperty(value = "name")
	private String name;

	@JsonProperty(value = "school_id")
	private String schoolId;

	@JsonProperty(value = "course_id")
	private String courseId;
	
	@JsonProperty(value = "student_id")
	private String studentId;

	@JsonProperty(value = "due_date")
	private LocalDate dueDate;

	@JsonProperty(value = "type")
	private String type;

	@JsonProperty(value = "subject")
	private String subject;

	@JsonProperty(value = "description")
	private String description;
}
