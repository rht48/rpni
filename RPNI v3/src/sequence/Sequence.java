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

public class Sequence {
	
	private List<Move> moves;
	private Automaton mca;
	private Automaton auto;
	private Automaton old_auto;
	private int index;
	
	public Sequence() {
		moves = new ArrayList<>();
		auto = new Automaton();
		index = 0;
	}
	
	public void addMove(Move m) {
		moves.add(m);
	}
	
	public Automaton getAuto() {
		return this.auto;
	}
	
	public void initAuto(MCAAutomaton mca) {
		for(var a : mca.getAutos()) {
			for(var s : a.getStates()) {
				auto.addState(s);
			}
		}
		
		for(var a : mca.getAutos()) {
			for(var s : a.getStates()) {
				auto.getTransitions(s).addAll(a.getTransitions(s));
			}
		}
		old_auto = auto.clone();
		this.mca = auto.clone();
	}
	
	public boolean hasNext() {
		return index < moves.size();
	}
	
	public boolean hasPrev() {
		return index > 0;
	}
	
	public String nextType() {
		return moves.get(index).getType();
	}
	
	public void nextIndex() {
		index++;
	}
	
	public Automaton nextAuto() throws UndefinedFirstStateException {
		Move m = moves.get(index);
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
	
	public Automaton prevAuto() throws UndefinedFirstStateException {
		auto = mca.clone();
		old_auto = auto.clone();
		int old_i = index;
		index = 0;
		for(int i = 0; i < old_i - 1; i++) {
			nextAuto();
		}
		return auto.clone();
	}
	
	public String toString() {
		if(hasNext())
			return moves.get(index).toString();
		else
			return "Algorithm finished !";
	}
	
	
}
