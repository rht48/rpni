package algorithms;

import automaton.Automaton;
import automaton.MCAAutomaton;
import automaton.State;
import exceptions.UndefinedFirstStateException;
import writers.Writer;

/**
 * PTA algorithm.
 * @author Romain
 *
 */
public class PTA {
	/**
	 * PTA algorithm based on the MCAAutomaton.
	 * @param auto
	 * @return Automaton The PTA created during the algorithm.
	 * @throws UndefinedFirstStateException When the first state is undefined.
	 */
	public static Automaton pta(MCAAutomaton auto) throws UndefinedFirstStateException {
		/*
		 * The reference automaton is the first automaton in the mca.
		 */
		Automaton a = auto.getAutos().get(0);
		/**
		 * For every automaton except the first, merge and determinize.
		 */
		for(int i = 1; i < auto.getAutos().size(); i++) {
			pta(a, auto.getAutos().get(i));
		}
		/*
		 * Write a bunch of stuff into data/dat in order to set up for the next algorithm.
		 */
		Writer.write("data/dat", "FN1;PTA finished !\n");
		Writer.write("data/dat", "ID;Setting new IDs for better clarity\n");
		Writer.write("data/dat", "PO;Setting positions for better clarity\n");
		Writer.write("data/dat", "FN2;Setting up for RPNI\n");
		return a;
	}
	
	/**
	 * This is the core of the pta function. Merges the first two states then 
	 * determinize the new automaton.
	 * @param ref
	 * @param auto
	 * @throws UndefinedFirstStateException When the first state is undefined.
	 */
	private static void pta(Automaton ref, Automaton auto) throws UndefinedFirstStateException {
		/*
		 * Get the first of each automaton.
		 */
		State f1 = ref.getFirst();
		State f2 = auto.getFirst();
		
		Writer.write("data/dat", "MO;Merging {" + f1.getId() + "} with {" + f2.getId() + "};" + f1.getCode() + ";" + f2.getCode() + "\n");
		/*
		 * Merge these two states.
		 */
		ref.mergeOut(f1, f2, auto);
		
		/*
		 * Determinize the resulting automaton.
		 */
		ref.determinize();
	}

}
