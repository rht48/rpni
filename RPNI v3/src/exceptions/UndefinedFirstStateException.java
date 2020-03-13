package exceptions;

public class UndefinedFirstStateException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public UndefinedFirstStateException() {
		super();
	}
	
	public UndefinedFirstStateException(String msg) {
		super(msg);
	}
}
