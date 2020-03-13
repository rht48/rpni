package algorithms;



import java.util.ArrayList;
import java.util.List;

import automaton.Automaton;
import automaton.State;
import examples.Examples;
import exceptions.UndefinedFirstStateException;

public class RPNI {
	public static Automaton rpni(Automaton auto, Examples neg) throws UndefinedFirstStateException {
		List<State> red = new ArrayList<>();
		List<State> blue = new ArrayList<>();
		
		Automaton old_hyp = auto.clone();
		red.add(old_hyp.getFirst());
		for(var t : old_hyp.getTransitions(old_hyp.getFirst())) {
			blue.add(t.getValue());
		}
		
		
		while(!blue.isEmpty()) {
			State s_blue = blue.remove(0);
			List<State> tmp_red = new ArrayList<>(red);
			
			boolean success = false;
			while(!tmp_red.isEmpty()) {
				State s_red= tmp_red.remove(0);
				Automaton hyp = old_hyp.clone();
				
				System.out.println("Merging: " + s_red.getId() + " " + s_blue.getId());
				hyp.merge(s_red, s_blue);
				hyp.determinize();
				
				State ns = hyp.getNewState(s_red);
				System.out.println("New state: " + ns.getId());
				if(test(hyp, neg)) {
					System.out.println("TEST SUCCESFUL!");
					success = true;
					red.add(0, ns);
					red.remove(s_red);
					for(var t : hyp.getTransitions(ns)) {
						if(!red.contains(t.getValue()))
							blue.add(t.getValue());
					}
					old_hyp = hyp;
					break;
				}else {
					System.out.println("TEST UNSUCCESSFUL, ROLLBACK");
				}
			}
			
			if(!success) {
				red.add(s_blue);
				System.out.println("FAILED! " + red.size());
				State ns = old_hyp.getNewState(s_blue);
				for(var t : old_hyp.getTransitions(ns)) {
					if(!red.contains(t.getValue()))
						blue.add(t.getValue());
				}
			}
			
		}
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
				System.out.print("Example {");
				for(int i = 0; i < strs.length - 1; i++) {
					System.out.print(strs[i] + ", ");
				}
				System.out.println(strs[strs.length - 1] + "} was accepted");
				return false;
			}
		}
		return true;
	}
}
