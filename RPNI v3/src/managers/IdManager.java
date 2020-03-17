package managers;


import automaton.Automaton;
import automaton.MCAAutomaton;
import automaton.State;
import buffer.StateBuffer;
import exceptions.UndefinedFirstStateException;

/**
 * This class sets the ids of every state in automata.
 * @author Romain
 *
 */
public class IdManager {
	
	/**
	 * Sets the ids for an MCAAutomaton.
	 * @param auto
	 * @throws UndefinedFirstStateException When the first state is undefined.
	 */
	public static void setIds(MCAAutomaton auto) throws UndefinedFirstStateException {
		int id = 0;
		for(var a : auto.getAutos()) {
			id = setIds(a.getFirst(), a, id, new StateBuffer());
		}
	}
	
	/**
	 * Sets the ids for an Automaton.
	 * @param auto
	 * @throws UndefinedFirstStateException When the first state is undefined.
	 */
	public static void setIds(Automaton auto) throws UndefinedFirstStateException {
		StateBuffer buff = new StateBuffer();
		setIds(auto.getFirst(), auto, 0, buff);
	}
	
	/**
	 * This is the core of the setId function.
	 * @param s
	 * @param auto
	 * @param id
	 * @param buff
	 * @return int: The id of the next state.
	 */
	private static int setIds(State s, Automaton auto, int id, StateBuffer buff) {
		/*
		 * Add the state s to the buffer in order to avoid loops.
		 */
		buff.add(s);
		/*
		 * Set the id of the state, and increment the id.
		 */
		s.setId("" + id);
		id++;
		/*
		 * For every transition from the state s, set the id of the pointed state.
		 */
		for(var t : auto.getTransitions(s)) {
			if(!buff.contains(t.getValue())) {
				id = setIds(t.getValue(), auto, id, buff);
			}
		}
		return id;
	}
	
}
