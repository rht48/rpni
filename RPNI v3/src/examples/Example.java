package examples;

/**
 * A class that stores an example like: {a,a,b,a}
 * @author Romain
 *
 */
public class Example {
	private String[] strs;

	/**
	 * Constructor for the Example class.
	 * @param strs String The array of the example.
	 */
	public Example(String[] strs) {
		super();
		this.strs = strs;
	}
	
	/**
	 * Getter for the array of examples.
	 * @return
	 */
	public String[] getStrs() {
		return strs;
	}
	
	/**
	 * Setter for the array of examples.
	 * @param strs
	 */
	public void setStrs(String[] strs) {
		this.strs = strs;
	}
	
}
