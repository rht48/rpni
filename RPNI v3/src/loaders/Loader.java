package loaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import automaton.Automaton;
import automaton.MCAAutomaton;
import automaton.State;
import examples.Example;
import examples.Examples;
import exceptions.UnknownStateException;
import sequence.Move;
import sequence.Sequence;


/**
 * This class loads is used to load various files into objects for further use.
 * @author Romain
 *
 */
public class Loader {
	
	/**
	 * Loads the MCA automaton based on what we put in the + file.
	 * @return MCAAutomaton
	 * @throws UnknownStateException When a state has not been defined.
	 */
	public static MCAAutomaton loadMCA() throws UnknownStateException {
		MCAAutomaton auto = new MCAAutomaton();
		try {
			BufferedReader br = new BufferedReader(new FileReader("res/+"));
			String str;
			while((str = br.readLine()) != null) {
				Automaton a = new Automaton();
				String[] strs = str.split(";");
				State prev = new State();
				
				//Sets the first state as the fisrt state in the automaton
				prev.setStart(true);
				a.setFirst(prev);
				a.addState(prev);
				
				//If the line is not null, then we go in
				if(!strs[0].equals("")) {
					//Adds the next state in the line, also connects the previous state to this new state
					for(int i = 0; i < strs.length; i++) {
						State temp = new State();
						a.addState(temp);
						a.connect(prev, temp, strs[i]);
						prev = temp;
					}
				}
				
				//Sets the last state of the line to be the finish
				prev.setFinish(true);
				auto.addAutomaton(a);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return auto;
	}
	
	/**
	 * Loads the examples from a given filename.
	 * @param filename String the filename of the example.
	 * @return Examples The list of examples in the file.
	 */
	public static Examples loadExamples(String filename) {
		Examples exs = new Examples();
		try {
			BufferedReader br = new BufferedReader(new FileReader("res/" + filename));
			String str;
			while((str = br.readLine()) != null) {
				String[] strs = str.split(";");
				exs.addExample(new Example(strs));
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return exs;
	}
	
	/**
	 * Loads the sequence produced by the different algorithms.
	 * @return Sequence
	 */
	public static Sequence loadSequence() {
		Sequence sq = new Sequence();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/dat"));
			String str;
			while((str = br.readLine()) != null) {
				String[] strs = str.split(";");
				//If the length is 2 then that means that the move was only informative, there wasn't really a move per say
				if(strs.length == 2) {
					sq.addMove(new Move(strs[0],strs[1]));
				}else if(!strs[0].equals("")) {
					sq.addMove(new Move(strs[0], strs[1], Integer.parseInt(strs[2]), Integer.parseInt(strs[3])));
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sq;
	}
}
