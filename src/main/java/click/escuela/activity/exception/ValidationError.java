package click.escuela.activity.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationError {
	private String message;
	private String field;

	public ValidationError(String message, String field) {
		this.message = message;
		this.field = field;
	}
}
