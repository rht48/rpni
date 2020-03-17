package automaton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import buffer.StateBuffer;
import exceptions.UndefinedFirstStateException;
import exceptions.UnknownStateException;
import writers.Writer;

/**
 * This class models an automaton.
 * It has states that are connected to each other.
 * Also has various functions to modify the automaton.
 * @author Romain
 *
 */
public class Automaton {
	
	/**
	 * This is the core of the automaton. It stores every states and every transition between the states.
	 * Each state has a list of transitions to other states.
	 */
	private Map<State, List<Transition<String,State>>> map;
	/**
	 * Is the first element of the automaton.
	 * Basically, it is the input of the automaton.
	 */
	private State first;
	/**
	 * Used for browsing the automaton.
	 */
	private State currentState;
	/**
	 * Defines a state that is out of bounds while browsing the automaton.
	 */
	private static final State OUT_OF_BOUNDS = new State();
	
	/**
	 * Constructor for the Automaton class.
	 */
	public Automaton() {
		map = new HashMap<>();
		currentState = null;
	}
	
	/**
	 * Adds a state to the automaton.
	 * @param s
	 */
	public void addState(State s) {
		if(!map.containsKey(s)) {
			List<Transition<String,State>> list = new ArrayList<>();
			map.put(s, list);
		}
	}
	
	/**
	 * Clones the current Automaton.
	 * @return Automaton
	 */
	public Automaton clone() {
		Automaton auto = new Automaton();
		Map<State,State> hist = new HashMap<>();
		/*
		 * For every state in the automaton, clone it and add it to the cloned automaton.
		 */
		for(var s : map.keySet()) {
			State n = s.clone();
			auto.addState(n);
			if(s.equals(first)) auto.first = n;
			hist.put(s, n);
		}
		
		/*
		 * For every transitions of every state in the automaton, clone it and add it to the new automaton.
		 */
		for(var s : map.keySet()) {
			for(var t : map.get(s)) {
				try {
					auto.connect(hist.get(s), hist.get(t.getValue()), t.getKey());
				} catch (UnknownStateException e) {
					e.printStackTrace();
				}
			}
		}
		
		return auto;
	}
	
	/**
	 * Connects two states together.
	 * To be done as: connect state s1 to state s2 with condition t.
	 * @param s1
	 * @param s2
	 * @param t
	 * @throws UnknownStateException When the state s1 or s2 is unknown to the automaton.
	 */
	public void connect(State s1, State s2, String t) throws UnknownStateException {
		if(!map.containsKey(s1)) throw new UnknownStateException(s1.getId());
		if(!map.containsKey(s2)) throw new UnknownStateException(s2.getId());
		map.get(s1).add(new Transition<String,State>(t, s2));
	}
	
	/**
	 * This function tries to determinize the automaton from the starting at the first state.
	 * @throws UndefinedFirstStateException When the first state is undefined.
	 */
	public void determinize() throws UndefinedFirstStateException {
		State fr = this.getFirst();
		StateBuffer buff = new StateBuffer();
		determinize(fr, buff);
	}
	
	/**
	 * Determinize the automaton from any state. Buffer is used to avoid any loops that may be present.
	 * @param s
	 * @param buff
	 */
	private void determinize(State s, StateBuffer buff) {
		/*
		 * Adds the state s to the buffer, so that it is avoided in future encounters.
		 */
		buff.add(s);
		/*
		 * While the current state is not deterministic, then continue to determinize the current state.
		 */
		while(!isDeterministic(s)) {
			//Writes the current action to data/dat, for future use in the sequence.
			Writer.write("data/dat", "NT;{" + s.getId() + "} is not determinized\n");
			determinize(s);
		}
		
		List<Transition<String,State>> trans = new ArrayList<>(map.get(s));
		/*
		 * For every state connected to the current state, determinize that state.
		 */
		while(!trans.isEmpty()) {
			Transition<String,State> t = trans.remove(0);
			/*
			 * Is not present in the buffer, in other word, if we haven't yet determinized the state, then do it.
			 */
			if(!buff.contains(t.getValue())) {
				determinize(t.getValue(), buff);
			}
		}
	}
	
	/**
	 * This is the core of the function determinize. Determinize the state s (removes all transition in double by merging states).
	 * @param s
	 */
	private void determinize(State s) {
		Map<String, List<State>> doubles = new HashMap<>();
		/*
		 * For every transition from the state s, add the "name" of the transition to the HashMap as to have a
		 * history of what has been seen. Then store that state in the list.
		 */
		for(var t : map.get(s)) {
			if(!doubles.containsKey(t.getKey())) {
				doubles.put(t.getKey(), new ArrayList<>());
			}
			doubles.get(t.getKey()).add(t.getValue());
		}
		
		/*
		 * For every "name" of transitions, checks if the list behind has a size over 1 excluded.
		 * That means that that transition is not determinized, and thus needs to be merged.
		 */
		for(var str : doubles.keySet()) {
			List<State> list = doubles.get(str);
			/*
			 * The reference state is the first state of the list.
			 * That means that all doubles will be merged to this state.
			 */
			State ref = list.get(0);
			for(int i = 1; i < list.size(); i++) {
				//Writes the current action to data/dat, for future use in the sequence.
				Writer.write("data/dat", "MO;Merging {" + ref.getId() + "} with {" + list.get(i).getId() + "};" + ref.getCode() + ";" + list.get(i).getCode() + "\n");
				mergeOut(ref, list.get(i), this);
			}
		}
	}
	
	/**
	 * Feeds the automaton with a transition.
	 * Moves the current state to the next state that has the transition str.
	 * If no state is found, then the current state will be the state OUT_OF_BOUNDS. 
	 * @param str
	 */
	public void feed(String str) {
		/*
		 * For each transition of the current state, compares the transition name and str.
		 * If they are equal then current state will take the state pointed by the transition str.
		 */
		if(!(currentState == OUT_OF_BOUNDS)) {
			for(var t : map.get(currentState)) {
				if(t.getKey().equals(str)) {
					currentState = t.getValue();
					return;
				}
			}
			currentState = OUT_OF_BOUNDS;
		}
	}
	
	/**
	 * Gets all states which are descendants of the state s. They may be children, grandchildren, etc...
	 * @param s
	 * @return List[State]
	 */
	public List<State> getAllDescendants(State s){
		StateBuffer buff = new StateBuffer();
		return getAllDescendants(s, buff);
	}
	
	/**
	 * This is the core of the algorithm. The buffer is used to avoid any loops in the automaton.
	 * @param s
	 * @param buff
	 * @return List[State]
	 */
	private List<State> getAllDescendants(State s, StateBuffer buff){
		/*
		 * Adds the state s to the buffer in order to prevent loops.
		 */
		buff.add(s);
		List<State> res = new ArrayList<>();
		/*
		 * Is the state s is present in the automaton, then continue the algorithm.
		 */
		if(map.containsKey(s)) {
			/*
			 * For every transition from the state s, adds the state pointed by the transition to the list.
			 * Then add every descendants of that state to the list.
			 */
			for(var t : map.get(s)) {
				if(!buff.contains(t.getValue())) {
					res.add(t.getValue());
					res.addAll(getAllDescendants(t.getValue(), buff));
				}
			}
		}
		return res;
	}
	
	/**
	 * Gets all ascendant of the state s.
	 * @param s
	 * @return List[State]
	 */
	public List<State> getAllAscendants(State s){
		/*
		 * What this algorithm does is pretty much:
		 * return {All States} - {All Descendants of the State s}
		 */
		List<State> asc = new ArrayList<>();
		List<State> desc = this.getAllDescendants(s);
		/*
		 * This is the subtraction part. 
		 */
		for(var st : this.getStates()) {
			if(!desc.contains(st)) {
				asc.add(st);
			}
		}
		return asc;
	}
	
	/**
	 * Getter for the state pointed by the current state.
	 * @return State
	 */
	public State getCurrentState() {
		return currentState;
	}
	
	/**
	 * Returns the first state of the automaton.
	 * @return State
	 * @throws UndefinedFirstStateException When the first state is undefined.
	 */
	public State getFirst() throws UndefinedFirstStateException {
		if(first == null) throw new UndefinedFirstStateException("The first state has not been defined.");
		return first;
	}
	
	/**
	 * Calculates the height of the automaton from the first state.
	 * @return int
	 * @throws UndefinedFirstStateException When the first state is undefined.
	 */
	public int getHeight() throws UndefinedFirstStateException {
		if(first == null) throw new UndefinedFirstStateException("The first state has not been defined.");
		StateBuffer buff = new StateBuffer();
		return getHeight(first, buff);
	}
	
	/**
	 * Calculates the height of the automaton from any given state s.
	 * @param s
	 * @return int
	 */
	public int getHeight(State s) {
		StateBuffer buff = new StateBuffer();
		return getHeight(s, buff);
	}
	
	/**
	 * This is the core of the function getHeight
	 * @param s
	 * @param buff
	 * @return int
	 */
	private int getHeight(State s, StateBuffer buff) {
		buff.add(s);
		/*
		 * If the buffer contains all transitions from the state s, that means that we are at a leaf, so add 1.
		 */
		if(buff.containsAll(map.get(s)))
			return 1;
		/*
		 * If it isn't a leaf, then look at every state connected to the state s, and accumulate every height.
		 */
		int h = 0;
		for(var t : map.get(s)) {
			if(!buff.contains(t.getValue())) {
				h += getHeight(t.getValue(), buff);
			}
		}
		return h;
	}
	
	/**
	 * This function returns the state that has the same code as the state s.
	 * Note that the name of the function doesn't really tell what it does. Sorry !
	 * @param s
	 * @return State or null if the function didn't find the state.
	 */
	public State getNewState(State s) {
		/*
		 * For every state in the automaton, check if the codes are similar. If so, return that state.
		 */
		for(var st : map.keySet()) {
			if(st.getCode() == s.getCode()) {
				return st;
			}
		}
		return null;
	}
	
	/**
	 * This function returns the state that has the code given in parameter.
	 * @param code
	 * @return State
	 * @throws NullPointerException If the no state has the code given in parameter.
	 */
	public State getStateFromCode(int code) {
		/*
		 * For every state in the automaton, check if that state has the same code, if so, return it.
		 */
		for(var s : map.keySet()) {
			if(s.getCode() == code) return s;
		}
		throw new NullPointerException("The state with code: " + code + " does not exist.");
	}
	
	/**
	 * Returns the state from a given id.
	 * DEPRECATED the ID is not unique, so results may vary from time to time.
	 * @param id
	 * @return State
	 * @deprecated
	 * @throws NullPointerException When no state has the given id.
	 */
	public State getStateFromId(String id) {
		/*
		 * For every state in the automaton, compares the ids. If there is a match, return that state.
		 */
		for(var s : map.keySet()) {
			if(id.equals(s.getId())) return s;
		}
		throw new NullPointerException("The state with id: " + id + " does not exist.");
	}
	
	/**
	 * A simple getter for every state in the automaton.
	 * @return
	 */
	public Set<State> getStates() {
		return map.keySet();
	}
	
	/**
	 * Returns all transitions from a given state.
	 * @param s
	 * @return List[Transition[String,State]]
	 */
	public List<Transition<String,State>> getTransitions(State s){
		return map.get(s);
	}
	
	/**
	 * Calculates the width of the automaton starting at the first state.
	 * @return int
	 * @throws UndefinedFirstStateException When the first state is undefined.
	 */
	public int getWidth() throws UndefinedFirstStateException {
		if(first == null) throw new UndefinedFirstStateException("The first state has not been defined.");
		StateBuffer buff = new StateBuffer();
		return getWidth(first, buff);
	}
	
	/**
	 * This is the core of the algorithm getWidth.
	 * @param s
	 * @param buff
	 * @return int
	 */
	private int getWidth(State s, StateBuffer buff) {
		/*
		 * Adds the state s to the buffer in order to avoid loops.
		 */
		buff.add(s);
		int max = 0;
		/*
		 * For every state connected to the state s, max will be equal to the maximum 
		 * between max and the width of the next state.
		 */
		for(var t : map.get(s)) {
			if(!buff.contains(t.getValue()))
				max = Math.max(max, getWidth(t.getValue(), buff));
		}
		return 1 + max;
	}
	
	/**
	 * Sets the current state to the fisrt state.
	 */
	public void goToStart() {
		this.currentState = this.first;
	}
	
	/**
	 * Determines if the given state is deterministic or not.
	 * @param st
	 * @return boolean
	 */
	public boolean isDeterministic(State st) {
		/*
		 * trans is the histtory of all "names" of the transitions.
		 */
		List<String> trans = new ArrayList<>();
		/*
		 * Get the state from the code, because st may not be present in the automaton.
		 * This is just a failsafe.
		 */
		State s = this.getStateFromCode(st.getCode());
		/*
		 * For every transition from the state st, check if the name is in the list.
		 * If so, then it is a double so return false.
		 * Else, put that name in the list.
		 */
		for(var t : map.get(s)) {
			if(!trans.contains(t.getKey())) {
				trans.add(t.getKey());
			}else {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if the current state is at the end or not.
	 * Returns true when out of bounds.
	 * @return boolean
	 */
	public boolean isFinished() {
		return !currentState.equals(OUT_OF_BOUNDS) && currentState.isFinish();
	}
	
	/**
	 * Merges the state s1 with the state s2
	 * @param s1
	 * @param s2
	 */
	public void merge(State s1, State s2) {
		this.merge(s1, s2, this);
	}
	
	/**
	 * This is the core of the function merge, merges state st1 with st2 that may be from a different automaton.
	 * @param st1
	 * @param st2
	 * @param parent
	 */
	public void merge(State st1, State st2, Automaton parent) {
		/*
		 * Gets the states from the codes of st1 and st2.
		 */
		State s1 = getNewState(st1);
		State s2 = getNewState(st2);
		
		/*
		 * Adds all the transitions of s2 to s1.
		 * Connects s1 to all states connected by s2.
		 */
		this.getTransitions(s1).addAll(parent.getTransitions(s2));
		
		/*
		 * Get the descendants of s2.
		 */
		List<State> list = parent.getAllDescendants(s2);
		/*
		 * For every descendants, adds it if it isn't in the automaton.
		 * Adds all transitions too. 
		 */
		for(var s : list) {
			if(!map.containsKey(s)) {
				this.addState(s);
				this.getTransitions(s).addAll(parent.getTransitions(s));
			}
		}
		
		/*
		 * For every transitions in the automaton, reroutes all connections to s2 to s1.
		 */
		for(var s : map.keySet()) {
			for(var t : map.get(s)) {
				if(t.getValue().equals(s2)) {
					t.setValue(s1);
				}
			}
		}
		
		/*
		 * Removes the state s2 as it doesn't exist anymore.
		 */
		parent.removeState(s2);
		/*
		 * Merges the two states together.
		 */
		s1.merge(s2);
	}
	
	/**
	 * This is only a partial merger between states.
	 * Basically, it only merges the states outbounds of the state s2.
	 * @param s1
	 * @param s2
	 * @param parent
	 */
	public void mergeOut(State s1, State s2, Automaton parent) {
		this.getTransitions(s1).addAll(parent.getTransitions(s2));
		List<State> list = parent.getAllDescendants(s2);
		for(var s : list) {
			if(!map.containsKey(s)) {
				this.addState(s);
				this.getTransitions(s).addAll(parent.getTransitions(s));
			}
		}
		parent.removeState(s2);
		s1.merge(s2);
	}
	
	/**
	 * Removes all traces of a state in the automaton.
	 * @param state
	 */
	public void removeState(State state) {
		/*
		 * First, we need to remove all transtions to the state s.
		 */
		for(var s : map.keySet()) {
			List<Transition<String,State>> trans = map.get(s);
			for(int i = trans.size() - 1; i >= 0; i--) {
				if(trans.get(i).getValue() == state) {
					map.get(s).remove(trans.get(i));
				}
			}
		}
		/*
		 * Then we remove the state in the list of states.
		 */
		map.remove(state);
	}
	
	/**
	 * Sets the first state based on a given state.
	 * @param first
	 */
	public void setFirst(State first) {
		this.first = first;
		this.currentState = this.first;
	}
	
	/**
	 * Sets the fist state based on the isStart attribute of the state.
	 */
	public void setFirst() {
		/*
		 * For every state in the automaton, if the state is at the start, then we put that state as the first.
		 */
		for(var s : map.keySet()) {
			if(s.isStart()) {
				first = s;
				currentState = first;
			}
		}
	}
	
}
