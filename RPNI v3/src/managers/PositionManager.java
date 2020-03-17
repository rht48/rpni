package managers;


import automaton.Automaton;
import automaton.MCAAutomaton;
import automaton.State;
import buffer.StateBuffer;
import exceptions.UndefinedFirstStateException;

/**
 * This state sets the position of states in an automaton.
 * @author Romain
 *
 */
public class PositionManager {
	
	/**
	 * Sets the positions of the states in an MCAAutomaton.
	 * @param auto
	 * @param width
	 * @param height
	 * @throws UndefinedFirstStateException When the first state is undefined.
	 */
	public static void setPositions(MCAAutomaton auto, float width, float height) throws UndefinedFirstStateException {
		int h = auto.getHeight();
		int w = auto.getWidth();
		/*
		 * Calculate the increments between two states.
		 */
		float incrX = width/((float) w + 1.0f);
		float incrY = height/((float) h + 1.0f);
		/*
		 * Initializes the first positions.
		 */
		float posX = incrX;
		float posY = incrY;
		/*
		 * For every automaton in the MCAAutomaton, sets the position of the states of that automaton.
		 */
		for(var a : auto.getAutos()) {
			setPositions(a.getFirst(), a, posX, posY, incrX, incrY, new StateBuffer());
			posY += a.getHeight() * incrY;
		}
	}
	
	/**
	 * Sets the position of the states in an Automaton.
	 * @param auto
	 * @param width
	 * @param height
	 * @throws UndefinedFirstStateException When the first state is undefined.
	 */
	public static void setPositions(Automaton auto, float width, float height) throws UndefinedFirstStateException {
		StateBuffer buff = new StateBuffer();
		State first = auto.getFirst();
		
		int h = auto.getHeight();
		int w = auto.getWidth();
		/*
		 * Calculate the increments between two states.
		 */
		float incrX = width/((float) w + 1.0f);
		float incrY = height/((float) h + 1.0f);
		/*
		 * Set the positions starting from the first state in the automaton.
		 */
		setPositions(first, auto, incrX, incrY, incrX, incrY, buff);
	}

	/**
	 * This is the core of the setPositions functions.
	 * @param s
	 * @param auto
	 * @param posX
	 * @param posY
	 * @param incrX
	 * @param incrY
	 * @param buff
	 */
	private static void setPositions(State s, Automaton auto, float posX, float posY, float incrX, float incrY, StateBuffer buff) {
		/*
		 * Adds the state s in the buffer in order to avoid loops.
		 */
		buff.add(s);
		/*
		 * Sets the position of the state.
		 */
		s.setPosX(posX);
		s.setPosY(posY);
		/*
		 * Increments the position X.
		 */
		posX += incrX;
		/*
		 * For every state connected to the state s, set the position.
		 */
		for(var t : auto.getTransitions(s)) {
			if(!buff.contains(t.getValue())) {
				setPositions(t.getValue(), auto, posX, posY, incrX, incrY, buff);
				/*
				 * Get the height of the automaton created by the next state.
				 */
				int h = auto.getHeight(t.getValue());
				/*
				 * Increments the position Y based on the height of the next state.
				 */
				posY += h * incrY;
			}
		}
	}
	
}
