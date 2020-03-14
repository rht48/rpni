package exceptions;

/**
 * Exception for a method that requires the first state of an automaton, but it wasn't defined
 * @author Romain
 *
 */
public class UndefinedFirstStateException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public UndefinedFirstStateException() {
		super();
	}
	
	public UndefinedFirstStateException(String msg) {
		super(msg);
	}
}
