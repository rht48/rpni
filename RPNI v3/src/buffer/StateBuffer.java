package buffer;

import java.util.ArrayList;
import java.util.List;

import automaton.State;
import automaton.Transition;

/**
 * The aim of this class is to be used as a buffer.
 * What it does is that we add States and then we check if a State is in the buffer.
 * Very useful for recursive functions to avoid loops.
 * @author Romain
 *
 */
public class StateBuffer {
	private List<State> buffer;
	
	/**
	 * Constructor for the StateBuffer class
	 */
	public StateBuffer() {
		buffer = new ArrayList<>();
	}
	
	/**
	 * Clears the buffer
	 */
	public void clear() {
		buffer.clear();
	}
	
	/**
	 * Adds a State to the buffer
	 * @param s State
	 */
	public void add(State s) {
		buffer.add(s);
	}
	
	/**
	 * Checks if the state is in the buffer or not
	 * @param s State
	 * @return boolean
	 */
	public boolean contains(State s) {
		return buffer.contains(s);
	}
	
	/**
	 * Checks if all states from the input are in the buffer or not
	 * @param s List[Transition[String,State]]
	 * @return boolean
	 */
	public boolean containsAll(List<Transition<String, State>> s) {
		List<State> l = new ArrayList<>();
		for(var t : s) {
			l.add(t.getValue());
		}
		return containsAllStates(l);
	}
	
	/**
	 * Checks if all states from the input are in the buffer or not
	 * @param s List[State]
	 * @return boolean
	 */
	public boolean containsAllStates(List<State> s) {
		return buffer.containsAll(s);
	}
	
	/**
	 * A simple getter for the buffer
	 * @return List[State]
	 */
	public List<State> getBuffer() {
		return buffer;
	}
}
