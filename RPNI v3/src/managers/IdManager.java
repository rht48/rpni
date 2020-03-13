package managers;


import automaton.Automaton;
import automaton.MCAAutomaton;
import automaton.State;
import buffer.StateBuffer;
import exceptions.UndefinedFirstStateException;

public class IdManager {
	
	public static void setIds(MCAAutomaton auto) throws UndefinedFirstStateException {
		int id = 0;
		for(var a : auto.getAutos()) {
			id = setIds(a.getFirst(), a, id, new StateBuffer());
		}
	}
	
	public static void setIds(Automaton auto) throws UndefinedFirstStateException {
		StateBuffer buff = new StateBuffer();
		setIds(auto.getFirst(), auto, 0, buff);
	}
	
	private static int setIds(State s, Automaton auto, int id, StateBuffer buff) {
		buff.add(s);
		s.setId("" + id);
		id++;
		for(var t : auto.getTransitions(s)) {
			if(!buff.contains(t.getValue())) {
				id = setIds(t.getValue(), auto, id, buff);
			}
		}
		return id;
	}
	
}
