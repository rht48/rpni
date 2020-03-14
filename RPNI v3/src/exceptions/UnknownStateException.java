package exceptions;

/**
 * Exception when we want to add a state/connected two states, but one of them wasn't defined
 * @author Romain
 *
 */
public class UnknownStateException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public UnknownStateException() {
		super();
	}
	
	public UnknownStateException(String msg) {
		super(msg);
	}
	
	
}
