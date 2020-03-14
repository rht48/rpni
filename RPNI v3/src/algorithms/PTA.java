package algorithms;

import automaton.Automaton;
import automaton.MCAAutomaton;
import automaton.State;
import exceptions.UndefinedFirstStateException;
import writers.Writer;

public class PTA {
	public static Automaton pta(MCAAutomaton auto) throws UndefinedFirstStateException {
		Automaton a = auto.getAutos().get(0);
		//Writer.write("data/dat", "PTA");
		for(int i = 1; i < auto.getAutos().size(); i++) {
			pta(a, auto.getAutos().get(i));
		}
		return a;
	}
	
	private static void pta(Automaton ref, Automaton auto) throws UndefinedFirstStateException {
		State f1 = ref.getFirst();
		State f2 = auto.getFirst();
		
		Writer.write("data/dat", "MO;Merging {" + f1.getId() + "} with {" + f2.getId() + "};" + f1.getCode() + ";" + f2.getCode() + "\n");
		ref.mergeOut(f1, f2, auto);
		
		ref.determinize();
	}

}
