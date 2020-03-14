package buffer;

import java.util.ArrayList;
import java.util.List;

import automaton.State;
import automaton.Transition;

public class StateBuffer {
	
	private List<State> buffer;
	public StateBuffer() {
		buffer = new ArrayList<>();
	}
	
	public void clear() {
		buffer.clear();
	}
	
	public void add(State s) {
		buffer.add(s);
	}
	
	public boolean contains(State s) {
		return buffer.contains(s);
	}
	
	public boolean containsAll(List<Transition<String, State>> s) {
		List<State> l = new ArrayList<>();
		for(var t : s) {
			l.add(t.getValue());
		}
		return containsAllStates(l);
	}
	
	public boolean containsAllStates(List<State> s) {
		return buffer.containsAll(s);
	}
	
	public List<State> getBuffer() {
		return buffer;
	}
}
