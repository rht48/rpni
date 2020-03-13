package automaton;

import java.util.ArrayList;
import java.util.List;

import exceptions.UndefinedFirstStateException;

public class MCAAutomaton {
	private List<Automaton> autos;
	
	public MCAAutomaton() {
		autos = new ArrayList<>();
	}
	
	public void addAutomaton(Automaton auto) {
		autos.add(auto);
	}
	
	public List<Automaton> getAutos() {
		return autos;
	}
	
	public int getHeight() throws UndefinedFirstStateException {
		int h = 0;
		for(var a : autos) {
			h += a.getHeight();
		}
		return h;
	}
	
	public int getWidth() throws UndefinedFirstStateException {
		int w = 0;
		for(var a : autos) {
			w = Math.max(w, a.getWidth());
		}
		return w;
	}
	
	public MCAAutomaton clone() {
		MCAAutomaton n = new MCAAutomaton();
		for(var a : autos) {
			n.addAutomaton(a.clone());
		}
		return n;
	}
}
