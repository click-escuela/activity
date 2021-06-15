package click.escuela.activity.api;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(Include.NON_EMPTY)
@Schema(description = "Activity Api")
@SuperBuilder
public class ActivityApi {

	@JsonProperty(value = "id", required = false)
	private String id;

	@NotBlank(message = "Name cannot be empty")
	@Size(max = 50, message = "Name must be less than 50 characters")
	@JsonProperty(value = "name", required = true)
	private String name;

	@NotNull(message = "School Id cannot be null")
	@JsonProperty(value = "schoolId", required = true)
	private Integer schoolId;

	@NotBlank(message = "Course Id cannot be empty")
	@Size(max = 50, message = "Course Id must be less than 50 characters")
	@JsonProperty(value = "courseId", required = true)
	private String courseId;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@JsonProperty(value = "dueDate", required = true)
	private LocalDate dueDate;

	@NotBlank(message = "Type cannot be empty")
	@JsonProperty(value = "type", required = true)
	private String type;

	@NotBlank(message = "Subject cannot be empty")
	@Size(max = 50, message = "Subject must be less than 50 characters")
	@JsonProperty(value = "subject", required = true)
	private String subject;

	@NotBlank(message = "Description cannot be empty")
	@Size(max = 50, message = "Description must be less than 50 characters")
	@JsonProperty(value = "description", required = true)
	private String description;

}
