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

public class Loader {
	
	public static MCAAutomaton loadMCA() throws UnknownStateException {
		MCAAutomaton auto = new MCAAutomaton();
		try {
			BufferedReader br = new BufferedReader(new FileReader("res/+"));
			String str;
			while((str = br.readLine()) != null) {
				Automaton a = new Automaton();
				String[] strs = str.split(";");
				State prev = new State();
				prev.setStart(true);
				a.setFirst(prev);
				a.addState(prev);
				if(!strs[0].equals("")) {
					for(int i = 0; i < strs.length; i++) {
						State temp = new State();
						a.addState(temp);
						a.connect(prev, temp, strs[i]);
						prev = temp;
					}
				}
				prev.setFinish(true);
				auto.addAutomaton(a);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return auto;
	}
	
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
	
	public static Sequence loadSequence() {
		Sequence sq = new Sequence();
		try {
			BufferedReader br = new BufferedReader(new FileReader("data/dat"));
			String str;
			while((str = br.readLine()) != null) {
				String[] strs = str.split(";");
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
