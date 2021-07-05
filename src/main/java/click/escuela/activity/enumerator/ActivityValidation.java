package click.escuela.activity.enumerator;

public enum ActivityValidation {

	NAME_EMPTY("Name cannot be empty"), NAME_BAD_SIZE("Name must be less than 50 characters"),
	SCHOOL_ID_NULL("School Id cannot be null"), COURSE_ID_EMPTY("Course Id cannot be empty"),
	STUDENT_ID_EMPTY("Student Id cannot be empty"), TYPE_EMPTY("Type cannot be empty"),
	SUBJECT_EMPTY("Subject cannot be empty"), SUBJECT_BAD_SIZE("Subject must be less than 50 characters"),
	DESCRIPTION_EMPTY("Description cannot be empty"),
	DESCRIPTION_BAD_SIZE("Description must be less than 50 characters");

	private ActivityValidation(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	private String description;
}
