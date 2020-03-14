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
import sequence.Sequence;
import writers.Writer;

public class Launcher extends PApplet{
	
	private MCAAutomaton mca;
	private Automaton pta;
	private Automaton rpni;
	private Drawer drawer = new Drawer(this);
	private Sequence sq;
	public static final float WIDTH = 800;
	public static final float HEIGHT = 600;
	
	public void settings() {
		size((int)WIDTH, (int)HEIGHT);
	}
	
	public void setup() {
		Writer.clear("data/dat");
		Writer.write("data/dat", "NT;Loaded MCA\n");
		try {
			mca = Loader.loadMCA();
		} catch (UnknownStateException e) {
			e.printStackTrace();
		}
		try {
			IdManager.setIds(mca);
			PositionManager.setPositions(mca, width, height);
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
		
		sq = Loader.loadSequence();
		sq.initAuto(mca.clone());

	}
	
	public void draw() {
		background(190);
		
		drawer.drawTransitions(sq.getAuto());
		drawer.drawStates(sq.getAuto());
		drawer.inform(sq.toString());
	}
	
	public void keyPressed() {
		if(keyCode == RIGHT) {
			if(sq.hasNext()) {
				try {
					sq.nextAuto();
				} catch (UndefinedFirstStateException e) {
					e.printStackTrace();
				}
			}
		}else if(keyCode == LEFT) {
			if(sq.hasPrev()) {
				try {
					sq.prevAuto();
				} catch (UndefinedFirstStateException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void main(String[] args) {
		PApplet.main("main.Launcher");
	}
	
}
