package click.escuela.activity.exception;

import click.escuela.activity.enumerator.SchoolMessage;

public class SchoolException extends TransactionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SchoolException(SchoolMessage schoolMessage) {
		super(schoolMessage.getCode() ,schoolMessage.getDescription());
	}

}
