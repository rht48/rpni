package algorithms;

import automaton.Automaton;
import automaton.MCAAutomaton;
import automaton.State;
import exceptions.UndefinedFirstStateException;

public class PTA {
	public static Automaton pta(MCAAutomaton auto) throws UndefinedFirstStateException {
		Automaton a = auto.getAutos().get(0);
		
		for(int i = 1; i < auto.getAutos().size(); i++) {
			pta(a, auto.getAutos().get(i));
		}
		return a;
	}
	
	private static void pta(Automaton ref, Automaton auto) throws UndefinedFirstStateException {
		State f1 = ref.getFirst();
		State f2 = auto.getFirst();
		
		//System.out.println("Merged: " +  f1.getId() + " with " + f2.getId());

		ref.mergeOut(f1, f2, auto);
		
		ref.determinize();
		//pta(f1, ref);
		
	}
	/*
	private static void pta(State s1, Automaton ref) {
		if(!ref.isDeterministic(s1)) {
			ref.determinize(s1);
			System.out.println("determinize");
		}
		for(var t : ref.getTransitions(s1)) {
			pta(t.getValue(), ref);
		}
	}
	*/
}
