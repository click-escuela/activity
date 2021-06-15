package click.escuela.activity.exception;

import click.escuela.activity.enumerator.ActivityMessage;

public class ActivityException extends TransactionException {

	private static final long serialVersionUID = 1L;
	
	public ActivityException(ActivityMessage activityMessage) {
		super(activityMessage.getCode() ,activityMessage.getDescription());
	}

}
