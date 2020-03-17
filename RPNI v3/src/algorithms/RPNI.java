package algorithms;


import java.util.ArrayList;
import java.util.List;

import automaton.Automaton;
import automaton.State;
import examples.Examples;
import exceptions.UndefinedFirstStateException;
import writers.Writer;

/**
 * RPNI algorithm.
 * @author Romain
 *
 */
public class RPNI {
	/**
	 * RPNI Algorithm starting from the pta.
	 * @param auto
	 * @param neg
	 * @return Automaton The automaton created by the RPNI algorithm.
	 * @throws UndefinedFirstStateException When the first state is undefined.
	 */
	public static Automaton rpni(Automaton auto, Examples neg) throws UndefinedFirstStateException {
		/*
		 * Red list is the list of states that are fixed. They are our hypothesis. 
		 */ 
		List<State> red = new ArrayList<>();
		/*
		 * Blue list is the list of descendants of red.
		 */
		List<State> blue = new ArrayList<>();
		
		/*
		 * old_hyp is our previous automaton, in case that the new one is bad.
		 */
		Automaton old_hyp = auto.clone();
		/*
		 * Get the first state of the automaton.
		 */
		State fr = old_hyp.getFirst();
		
		/*
		 * Add the first state to the red list, as it is our hypothesis.
		 */
		red.add(fr);
		/*
		 * Writing a bunch of stuff to the data/dat file explaining this.
		 */
		Writer.write("data/dat", "SR;State {" + fr.getId() + "} is our hypothesis;" + fr.getCode() + ";-1\n");
		Writer.write("data/dat", "SB2;Setting direct descendants to blue;" + fr.getCode() + ";-1\n");
		
		/*
		 * Add all descendants of red to blue. 
		 */
		for(var t : old_hyp.getTransitions(old_hyp.getFirst())) {
			blue.add(t.getValue());
			t.getValue().setBlue(true);
		}
		
		/*
		 * While there are still descendants, continue the algorithm.
		 */
		while(!blue.isEmpty()) {
			/*
			 * Remove the first blue state from the list.
			 */
			State s_blue = blue.remove(0);
			
			/*
			 * Creating a temporary list based on red, just not to lose the first list.
			 */
			List<State> tmp_red = new ArrayList<>(red);
			
			/*
			 * success is a variable that says is there has been a successful merger aka the new
			 * automaton does not accept a negative example.
			 */
			boolean success = false;
			
			/*
			 * For every red, we try to merge the blue state to the red state.
			 */
			while(!tmp_red.isEmpty()) {
				/*
				 * Removes the first red state.
				 */
				State s_red= tmp_red.remove(0);
				/*
				 * Clones the previous automaton, just to keep a back up.
				 * hyp is our new hypothesis.
				 */
				Automaton hyp = old_hyp.clone();
				
				
				Writer.write("data/dat", "MG;Merging {" + s_red.getId() + "} with {" + s_blue.getId() + "};" + s_red.getCode() + ";" + s_blue.getCode() + "\n");
				/*
				 * Merge red with blue.
				 */
				hyp.merge(s_red, s_blue);
				/*
				 * Determinize the created automaton.
				 */
				hyp.determinize();
				
				/*
				 * Get the new state formed.
				 */
				State ns = hyp.getNewState(s_red);
				
				/*
				 * Test the automaton with the negative examples.
				 * If the test passes, then hyp becomes our back up, add ns to red and add
				 * all descendants of ns to blue.
				 * Else we rollback to the previous automaton.
				 */
				if(test(hyp, neg)) {
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
			
			/*
			 * If there hasn't been a successful merger between the blue state and all red states,*
			 * then we add that blue state to the red list and add all of its descendants to blue.
			 */
			if(!success) {
				s_blue.setBlue(false);
				s_blue.setRed(true);
				Writer.write("data/dat", "SR;No possible merges for {" + s_blue.getId() + "} so adding to red;" + s_blue.getCode() + ";-1\n");
				red.add(s_blue);
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
	
	/**
	 * Tests the automaton with the negative examples.
	 * @param auto
	 * @param exs
	 * @return boolean: true if the automaton doesn't accept an example, false otherwise.
	 */
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
