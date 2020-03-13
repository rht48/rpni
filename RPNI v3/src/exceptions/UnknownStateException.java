package exceptions;

public class UnknownStateException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public UnknownStateException() {
		super();
	}
	
	public UnknownStateException(String msg) {
		super(msg);
	}
	
	
}
