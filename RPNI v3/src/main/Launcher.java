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
	
	/**
	 * MCA automaton.
	 */
	private MCAAutomaton mca;
	/**
	 * PTA Automaton.
	 */
	private Automaton pta;
	/**
	 * RPNI Automaton.
	 */
	private Automaton rpni;
	/**
	 * Drawer for the various automatons.
	 */
	private Drawer drawer = new Drawer(this);
	/**
	 * The sequence generated by the various algorithms.
	 */
	private Sequence sq;
	/**
	 * Width of the screen.
	 */
	public static final float WIDTH = 800;
	/**
	 * Height of the screen.
	 */
	public static final float HEIGHT = 600;
	
	public void settings() {
		size((int)WIDTH, (int)HEIGHT);
	}
	
	/**
	 * Initializes all Automatons (mca, pta and rpni)
	 */
	public void setup() {
		Writer.clear("data/dat");
		Writer.write("data/dat", "NT;Loaded MCA\n");
		/**
		 * Seeting up the MCA
		 */
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
		
		/**
		 * Setting up the pta
		 */
		try {
			pta = PTA.pta(mca.clone());
			PositionManager.setPositions(pta, width, height);
			IdManager.setIds(pta);
		} catch (UndefinedFirstStateException e) {
			e.printStackTrace();
		}
		
		/**
		 * Setting up the rpni
		 */
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
	
	/**
	 * Draws the automatons.
	 */
	public void draw() {
		background(190);
		
		drawer.drawTransitions(sq.getAuto());
		drawer.drawStates(sq.getAuto());
		drawer.inform(sq.toString());
	}
	
	/**
	 * Is a key is pressed then advance in the sequence.
	 */
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
