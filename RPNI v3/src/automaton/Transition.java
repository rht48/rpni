package automaton;

public class Transition<E,T> {
	private E key;
	private T value;
	
	public Transition(E key, T value) {
		super();
		this.key = key;
		this.value = value;
	}

	public E getKey() {
		return key;
	}

	public T getValue() {
		return value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	
	
}
