package sequence;

import java.util.ArrayList;
import java.util.List;

import automaton.Automaton;
import automaton.MCAAutomaton;
import automaton.State;
import exceptions.UndefinedFirstStateException;
import main.Launcher;
import managers.IdManager;
import managers.PositionManager;

/**
 * This class stores every move during the various algorithms.
 * Has the option to move to the next move or to the previous.
 * @author Romain
 *
 */
public class Sequence {
	
	/**
	 * List of moves generated from the various algorithms.
	 */
	private List<Move> moves;
	/**
	 * MCA automaton. First automaton of the sequence.
	 */
	private Automaton mca;
	/**
	 * Current automaton of the sequence.
	 */
	private Automaton auto;
	/**
	 * Previous automaton of the sequence. Used during the rpni algorithm.
	 */
	private Automaton old_auto;
	/**
	 * Index of the current element on the list.
	 */
	private int index;
	
	/**
	 * Constructor for the Sequence class.
	 * Puts the current index to 0.
	 */
	public Sequence() {
		moves = new ArrayList<>();
		auto = new Automaton();
		index = 0;
	}
	
	/**
	 * Adds a move at the end of the sequence.
	 * @param m
	 */
	public void addMove(Move m) {
		moves.add(m);
	}
	
	/**
	 * A simple getter for the auto.
	 * @return
	 */
	public Automaton getAuto() {
		return this.auto;
	}
	
	/**
	 * Initializes the automatons in the sequence.
	 * @param mca
	 */
	public void initAuto(MCAAutomaton mca) {
		/*
		 * This adds all states in the mca automaton in the sequence automaton (auto)
		 */
		for(var a : mca.getAutos()) {
			for(var s : a.getStates()) {
				auto.addState(s);
			}
		}
		
		/*
		 * Adds all transitions from the mca to auto.
		 */
		for(var a : mca.getAutos()) {
			for(var s : a.getStates()) {
				auto.getTransitions(s).addAll(a.getTransitions(s));
			}
		}
		/*
		 * Initializes old_auto and this.mca to auto to have a back up.
		 * We have to clone to avoid modifying all automatons while modifying one.
		 */
		old_auto = auto.clone();
		this.mca = auto.clone();
	}
	
	/**
	 * Does the sequence have a next element in the list ?
	 * @return true when it does, false in the other case.
	 */
	public boolean hasNext() {
		return index < moves.size();
	}
	
	/**
	 * Does the sequence have a previous element in the list ?
	 * @return true when it does, false in the other case.
	 */
	public boolean hasPrev() {
		return index > 0;
	}
	
	/**
	 * Returns the type of the next element of the list.
	 * @return String
	 * @throws NullPointerException When the sequence doesn't have a next element.
	 */
	public String nextType() {
		if(hasNext())
			return moves.get(index).getType();
		throw new NullPointerException("Index out of bounds.");
	}
	
	/**
	 * Moves the current index to the next index.
	 */
	public void nextIndex() {
		index++;
	}
	
	/**
	 * Calculates the next automaton based on the sequence of moves.
	 * Returns the cloned new automaton.
	 * @return Automaton
	 * @throws UndefinedFirstStateException When the automaton doesn't have a first element.
	 */
	public Automaton nextAuto() throws UndefinedFirstStateException {
		/*
		 * Get the next move of the sequence.
		 */
		Move m = moves.get(index);
		/*
		 * This is a big switch/case basically based on the tag of the type.
		 */
		if(m.getType().equals("MO")) {
			State s1 = auto.getStateFromCode(m.getCode1());
			State s2 = auto.getStateFromCode(m.getCode2());
			auto.mergeOut(s1, s2, auto);
		}else if(m.getType().equals("MG")) {
			State s1 = auto.getStateFromCode(m.getCode1());
			State s2 = auto.getStateFromCode(m.getCode2());
			auto.merge(s1, s2);
		}else if(m.getType().equals("RB")) {
			auto = old_auto.clone();
		}else if(m.getType().equals("TS")) {
			old_auto = auto.clone();
		}else if(m.getType().equals("FN1")) {
			auto.setFirst();
		}else if(m.getType().equals("FN2")) {
			old_auto = auto.clone();
		}else if(m.getType().equals("SR")) {
			State s = auto.getStateFromCode(m.getCode1());
			s.setRed(true);
			s.setBlue(false);
		}else if(m.getType().equals("SB1")) {
			State s = auto.getStateFromCode(m.getCode1());
			for(var t : auto.getTransitions(s)) {
				if(!t.getValue().isRed())
					t.getValue().setBlue(true);;
			}
			old_auto = auto.clone();
		}else if(m.getType().equals("SB2")) {
			State s = auto.getStateFromCode(m.getCode1());
			for(var t : auto.getTransitions(s)) {
				if(!t.getValue().isRed())
					t.getValue().setBlue(true);;
			}
			old_auto = auto.clone();
		}else if(m.getType().equals("ID")) {
			IdManager.setIds(auto);
		}else if(m.getType().equals("PO")) {
			PositionManager.setPositions(auto, Launcher.WIDTH, Launcher.HEIGHT);
		}
		index++;
		return auto.clone();
	}
	
	/**
	 * Returns the previous automaton in the sequence.
	 * @return Automaton
	 * @throws UndefinedFirstStateException When the first state is undefined.
	 */
	public Automaton prevAuto() throws UndefinedFirstStateException {
		/*
		 * What the algorithm does, is start from the start and do every move until index - 1.
		 */
		auto = mca.clone();
		old_auto = auto.clone();
		int old_i = index;
		index = 0;
		for(int i = 0; i < old_i - 1; i++) {
			nextAuto();
		}
		return auto.clone();
	}
	
	/**
	 * Returns the commentary of the current move.
	 * @return String
	 */
	public String toString() {
		if(hasNext())
			return moves.get(index).toString();
		else
			return "Algorithm finished !";
	}
	
	
}
