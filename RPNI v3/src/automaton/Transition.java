package automaton;

/**
 * This generic class aims to models a transition between two objects
 * @author Romain
 *
 * @param <E>
 * @param <T>
 */
public class Transition<E,T> {
	
	/**
	 * Key for the transition.
	 */
	private E key;
	
	/**
	 * Value corresponding to the transition.
	 */
	private T value;
	
	/**
	 * Constructor for the transition class
	 * @param key E
	 * @param value T
	 */
	public Transition(E key, T value) {
		super();
		this.key = key;
		this.value = value;
	}
	
	/**
	 * A getter for the key
	 * @return E
	 */
	public E getKey() {
		return key;
	}
	
	/**
	 * A getter for the value
	 * @return T
	 */
	public T getValue() {
		return value;
	}
	
	/**
	 * A setter for value
	 * @param value T
	 */
	public void setValue(T value) {
		this.value = value;
	}
	
	
	
}
