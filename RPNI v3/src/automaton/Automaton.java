package automaton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import buffer.StateBuffer;
import exceptions.UndefinedFirstStateException;
import exceptions.UnknownStateException;

public class Automaton {
	
	private Map<State, List<Transition<String,State>>> map;
	private State first;
	private State currentState;
	private static final State OUT_OF_BOUNDS = new State();
	
	public Automaton() {
		map = new HashMap<>();
		currentState = null;
	}
	
	public void addState(State s) {
		if(!map.containsKey(s)) {
			List<Transition<String,State>> list = new ArrayList<>();
			map.put(s, list);
		}
	}
	
	public Automaton clone() {
		Automaton auto = new Automaton();
		Map<State,State> hist = new HashMap<>();
		for(var s : map.keySet()) {
			State n = s.clone();
			auto.addState(n);
			if(s.equals(first)) auto.first = n;
			hist.put(s, n);
		}
		
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
	
	public void connect(State s1, State s2, String t) throws UnknownStateException {
		if(!map.containsKey(s1)) throw new UnknownStateException(s1.getId());
		if(!map.containsKey(s2)) throw new UnknownStateException(s2.getId());
		map.get(s1).add(new Transition<String,State>(t, s2));
	}
	
	public void determinize() throws UndefinedFirstStateException {
		State fr = this.getFirst();
		StateBuffer buff = new StateBuffer();
		determinize(fr, buff);
	}
	
	private void determinize(State s, StateBuffer buff) {
		buff.add(s);
		while(!isDeterministic(s)) {
			System.out.println("State " + s.getId() + " is not determinized");
			determinize(s);
		}
		List<Transition<String,State>> trans = new ArrayList<>(map.get(s));
		//for(var t : map.get(s)) {
		while(!trans.isEmpty()) {
			Transition<String,State> t = trans.remove(0);
			if(!buff.contains(t.getValue())) {
				determinize(t.getValue(), buff);
			}
		}
	}
	
	private void determinize(State s) {
		Map<String, List<State>> doubles = new HashMap<>();
		
		for(var t : map.get(s)) {
			if(!doubles.containsKey(t.getKey())) {
				doubles.put(t.getKey(), new ArrayList<>());
			}
			doubles.get(t.getKey()).add(t.getValue());
		}
		
		for(var str : doubles.keySet()) {
			List<State> list = doubles.get(str);
			State ref = list.get(0);
			for(int i = 1; i < list.size(); i++) {
				mergeOut(ref, list.get(i), this);
			}
		}
	}
	
	public void feed(String str) {
		for(var t : map.get(currentState)) {
			if(t.getKey().equals(str)) {
				currentState = t.getValue();
				return;
			}
		}
		currentState = OUT_OF_BOUNDS;
	}
	
	public List<State> getAllDescendants(State s){
		StateBuffer buff = new StateBuffer();
		return getAllDescendants(s, buff);
	}
	
	private List<State> getAllDescendants(State s, StateBuffer buff){
		buff.add(s);
		List<State> res = new ArrayList<>();
		if(map.containsKey(s)) {
			for(var t : map.get(s)) {
				if(!buff.contains(t.getValue())) {
					res.add(t.getValue());
					res.addAll(getAllDescendants(t.getValue(), buff));
				}
			}
		}
		return res;
	}
	
	public List<State> getAllAscendants(State s){
		List<State> asc = new ArrayList<>();
		List<State> desc = this.getAllDescendants(s);
		for(var st : this.getStates()) {
			if(!desc.contains(st)) {
				asc.add(st);
			}
		}
		return asc;
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
	public State getFirst() throws UndefinedFirstStateException {
		if(first == null) throw new UndefinedFirstStateException("The first state has not been defined.");
		return first;
	}
	
	public int getHeight() throws UndefinedFirstStateException {
		if(first == null) throw new UndefinedFirstStateException("The first state has not been defined.");
		StateBuffer buff = new StateBuffer();
		return getHeight(first, buff);
	}
	
	private int getHeight(State s, StateBuffer buff) {
		buff.add(s);
		if(buff.containsAll(map.get(s)))
			return 1;
		int h = 0;
		for(var t : map.get(s)) {
			if(!buff.contains(t.getValue())) {
				h += getHeight(t.getValue(), buff);
			}
		}
		return h;
	}
	
	public State getNewState(State s) {
		for(var st : map.keySet()) {
			if(st.getCode() == s.getCode()) {
				return st;
			}
		}
		return null;
	}
	
	public State getStateFromId(String id) {
		for(var s : map.keySet()) {
			if(id.equals(s.getId())) return s;
		}
		throw new NullPointerException("The state with id: " + id + " does not exist.");
	}
	
	public Set<State> getStates() {
		return map.keySet();
	}
	
	public List<Transition<String,State>> getTransitions(State s){
		return map.get(s);
	}
	
	public int getWidth() throws UndefinedFirstStateException {
		if(first == null) throw new UndefinedFirstStateException("The first state has not been defined.");
		StateBuffer buff = new StateBuffer();
		return getWidth(first, buff);
	}
	
	private int getWidth(State s, StateBuffer buff) {
		buff.add(s);
		int max = 0;
		for(var t : map.get(s)) {
			if(!buff.contains(t.getValue()))
				max = Math.max(max, getWidth(t.getValue(), buff));
		}
		return 1 + max;
	}
	
	public void goToStart() {
		this.currentState = this.first;
	}
	
	public boolean isDeterministic(State st) {
		List<String> trans = new ArrayList<>();
		State s = this.getStateFromId(st.getId());
		for(var t : map.get(s)) {
			if(!trans.contains(t.getKey())) {
				trans.add(t.getKey());
			}else {
				return false;
			}
		}
		return true;
	}
	
	public boolean isFinished() {
		return !currentState.equals(OUT_OF_BOUNDS) && currentState.isFinish();
	}
	
	public void merge(State s1, State s2) {
		this.merge(s1, s2, this);
	}
	
	public void merge(State st1, State st2, Automaton parent) {
		//Reroutes all transitions from s2 to S1
		/*
		System.out.println(s1.getId() + " " + s2.getId() + " " + getStateFromId(s1.getId()));
		for(var s : map.keySet()) {
			System.out.print(s.getId() + ", ");
		}
		System.out.println();*/
		State s1 = getNewState(st1);
		State s2 = getNewState(st2);
		
		this.getTransitions(s1).addAll(parent.getTransitions(s2));
		
		List<State> list = parent.getAllDescendants(s2);
		for(var s : list) {
			if(!map.containsKey(s)) {
				this.addState(s);
				this.getTransitions(s).addAll(parent.getTransitions(s));
			}
		}
		
		//Reroutes all transitions to s2 to s1
		for(var s : map.keySet()) {
			for(var t : map.get(s)) {
				if(t.getValue().equals(s2)) {
					t.setValue(s1);
				}
			}
		}
		
		parent.removeState(s2);
		s1.merge(s2);
	}
	
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
	
	public void removeState(State state) {
		for(var s : map.keySet()) {
			List<Transition<String,State>> trans = map.get(s);
			for(int i = trans.size() - 1; i >= 0; i--) {
				if(trans.get(i).getValue() == state) {
					map.get(s).remove(trans.get(i));
				}
			}
		}
		map.remove(state);
	}
	
	public void setFirst(State first) {
		this.first = first;
		this.currentState = this.first;
	}
	
}
