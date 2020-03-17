package automaton;

import java.util.ArrayList;
import java.util.List;

import exceptions.UndefinedFirstStateException;

public class MCAAutomaton {
	/**
	 * List of automata from the various lines.
	 */
	private List<Automaton> autos;
	
	/**
	 * Contructor for the MCAAutomaton class.
	 */
	public MCAAutomaton() {
		autos = new ArrayList<>();
	}
	
	/**
	 * Adds an automaton to the list.
	 * @param auto
	 */
	public void addAutomaton(Automaton auto) {
		autos.add(auto);
	}
	
	/**
	 * A simple getter for the list of automata.
	 * @return List[Automaton]
	 */
	public List<Automaton> getAutos() {
		return autos;
	}
	
	/**
	 * This calculates the added height of all automata in the list.
	 * @return int
	 * @throws UndefinedFirstStateException When one of the automaton didn't have a first state declared.
	 */
	public int getHeight() throws UndefinedFirstStateException {
		int h = 0;
		/*
		 * For all automaton a in the list, calculate the height of a.
		 * Then add it to the total h. 
		 */
		for(var a : autos) {
			h += a.getHeight();
		}
		return h;
	}
	
	/**
	 * Calculates the width of the MCA automaton.
	 * @return int
	 * @throws UndefinedFirstStateException When one of the automaton didn't have a first state declared.
	 */
	public int getWidth() throws UndefinedFirstStateException {
		int w = 0;
		/*
		 * For all automaton a, calculate the width of a.
		 * If the width is greater than the previous max width, then the new width is the new max width.
		 * Basically, it means that it tries to find the widest automaton in the list.
		 */
		for(var a : autos) {
			w = Math.max(w, a.getWidth());
		}
		return w;
	}
	
	/**
	 * Clones the current MCAAutomaton.
	 * @return MCAAutomaton
	 */
	public MCAAutomaton clone() {
		MCAAutomaton n = new MCAAutomaton();
		for(var a : autos) {
			n.addAutomaton(a.clone());
		}
		return n;
	}
}
