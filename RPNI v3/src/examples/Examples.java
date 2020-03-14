package examples;

import java.util.ArrayList;
import java.util.List;

/**
 * This class stores all examples.
 * @author Romain
 *
 */
public class Examples {
	private List<Example> exs;
	
	/**
	 * Constructor for the Examples class
	 */
	public Examples() {
		exs = new ArrayList<>();
	}
	
	/**
	 * Adds an example to the list of examples.
	 * @param ex Example
	 */
	public void addExample(Example ex) {
		exs.add(ex);
	}
	
	/**
	 * Getter for the list of examples
	 * @return List[Example]
	 */
	public List<Example> getExs(){
		return exs;
	}
}
