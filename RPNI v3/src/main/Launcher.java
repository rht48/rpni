package main;

import algorithms.PTA;
import algorithms.RPNI;
import automaton.Automaton;
import automaton.MCAAutomaton;
import drawer.Drawer;
import exceptions.UndefinedFirstStateException;
import exceptions.UnknownStateException;
import loaders.Loader;
import managers.IdManager;
import managers.PositionManager;
import processing.core.PApplet;

public class Launcher extends PApplet{
	
	private MCAAutomaton mca;
	private Automaton pta;
	private Automaton rpni;
	private Drawer drawer = new Drawer(this);
	
	public void settings() {
		size(800, 600);
	}
	
	public void setup() {
		try {
			mca = Loader.loadMCA();
			//pta = PTA.pta(mca.clone());
		} catch (UnknownStateException e) {
			e.printStackTrace();
		}
		try {
			IdManager.setIds(mca);
			//PositionManager.setPositions(mca, width, height);
		} catch (UndefinedFirstStateException e) {
			e.printStackTrace();
		}
		
		try {
			pta = PTA.pta(mca.clone());
			PositionManager.setPositions(pta, width, height);
			IdManager.setIds(pta);
		} catch (UndefinedFirstStateException e) {
			e.printStackTrace();
		}
		
		try {
			rpni = RPNI.rpni(pta.clone(), Loader.loadExamples("-"));
			PositionManager.setPositions(rpni, width, height);
			IdManager.setIds(rpni);
		} catch (UndefinedFirstStateException e) {
			e.printStackTrace();
		}
		

	}
	
	public void draw() {
		background(190);
		
//		for(var a : mca.getAutos()) {
//			drawer.drawTransitions(a);
//			drawer.drawStates(a);
//		}
		
		drawer.drawTransitions(rpni);
		drawer.drawStates(rpni);
	}
	
	public static void main(String[] args) {
		PApplet.main("main.Launcher");
	}
	
}
