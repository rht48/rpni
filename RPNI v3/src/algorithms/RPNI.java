package algorithms;



import java.util.ArrayList;
import java.util.List;

import automaton.Automaton;
import automaton.State;
import examples.Examples;
import exceptions.UndefinedFirstStateException;
import writers.Writer;

public class RPNI {
	public static Automaton rpni(Automaton auto, Examples neg) throws UndefinedFirstStateException {
		List<State> red = new ArrayList<>();
		List<State> blue = new ArrayList<>();
		
		Automaton old_hyp = auto.clone();
		State fr = old_hyp.getFirst();
		red.add(fr);
		Writer.write("data/dat", "SR;State {" + fr.getId() + "} is our hypothesis;" + fr.getCode() + ";-1\n");
		Writer.write("data/dat", "SB2;Setting direct descendants to blue;" + fr.getCode() + ";-1\n");
		//Writer.write("data/dat", "FN2;This is the base hypothesis\n");
		for(var t : old_hyp.getTransitions(old_hyp.getFirst())) {
			blue.add(t.getValue());
			t.getValue().setBlue(true);
		}
		
		
		while(!blue.isEmpty()) {
			State s_blue = blue.remove(0);
			List<State> tmp_red = new ArrayList<>(red);
			
			boolean success = false;
			while(!tmp_red.isEmpty()) {
				State s_red= tmp_red.remove(0);
				Automaton hyp = old_hyp.clone();
				
				//System.out.println("Merging: " + s_red.getId() + " " + s_blue.getId());
				Writer.write("data/dat", "MG;Merging {" + s_red.getId() + "} with {" + s_blue.getId() + "};" + s_red.getCode() + ";" + s_blue.getCode() + "\n");
				hyp.merge(s_red, s_blue);
				hyp.determinize();
				
				State ns = hyp.getNewState(s_red);
				//System.out.println("New state: " + ns.getId());
				if(test(hyp, neg)) {
					//System.out.println("TEST SUCCESFUL!");
					success = true;
					red.add(0, ns);
					red.remove(s_red);
					Writer.write("data/dat", "TS;No negative examples have been accepted, this is the new hypothesis\n");
					Writer.write("data/dat", "SB1;Setting direct descendants to blue;" + ns.getCode() + ";-1\n");
					for(var t : hyp.getTransitions(ns)) {
						if(!red.contains(t.getValue()))
							blue.add(t.getValue());
					}
					old_hyp = hyp;
					break;
				}else {
					Writer.write("data/dat", "RB;Rolling back to previous automaton\n");
					//System.out.println("TEST UNSUCCESSFUL, ROLLBACK");
				}
			}
			
			if(!success) {
				s_blue.setBlue(false);
				s_blue.setRed(true);
				Writer.write("data/dat", "SR;No possible merges for {" + s_blue.getId() + "} so adding to red;" + s_blue.getCode() + ";-1\n");
				red.add(s_blue);
				//System.out.println("FAILED! " + red.size());
				State ns = old_hyp.getNewState(s_blue);
				Writer.write("data/dat", "SB2;Setting direct descendants to blue;" + ns.getCode() + ";-1\n");
				for(var t : old_hyp.getTransitions(ns)) {
					if(!red.contains(t.getValue()))
						blue.add(t.getValue());
				}
			}
			
		}
		Writer.write("data/dat", "PO;Repositioning !\n");
		return old_hyp;
	}
	
	private static boolean test(Automaton auto, Examples exs) {
		for(var ex : exs.getExs()) {
			String[] strs = ex.getStrs();
			auto.goToStart();
			for(int i = 0; i < strs.length; i++) {
				auto.feed(strs[i]);
			}
			if(auto.isFinished()) {
				//System.out.print("Example {");
				Writer.write("data/dat", "NT;Example {");
				for(int i = 0; i < strs.length - 1; i++) {
					//System.out.print(strs[i] + ", ");
					Writer.write("data/dat", strs[i] + ",");
				}
				//System.out.println(strs[strs.length - 1] + "} was accepted");
				Writer.write("data/dat", strs[strs.length - 1] + "} was accepted\n");
				return false;
			}
		}
		return true;
	}
}
