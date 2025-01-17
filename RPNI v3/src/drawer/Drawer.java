package drawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import automaton.Automaton;
import automaton.State;
import automaton.Transition;
import processing.core.PApplet;

/**
 * This class has the aim to draw all automatons.
 * @author Romain
 *
 */
public class Drawer {
	private PApplet parent;
	public static final float DIAMETER = 50;
	private static final float TRIANGLE_POS = 5;
	private static final float OFFSET_CURB = 4;
	private static final float OFFSET_LINE = 5;
	private static final float LINE_LENGTH = 70;
	
	/**
	 * Constructor for the Drawer class
	 * @param parent PApplet
	 */
	public Drawer(PApplet parent) {
		this.parent = parent;
	}
	
	/**
	 * Draws all the states in an automaton
	 * @param auto Automaton
	 */
	public void drawStates(Automaton auto) {
		parent.textSize(11);
		Set<State> states = auto.getStates();
		//For each state in the automaton, draw it
		for(var s : states) {
			if(s.isVisible()) {
				//If the state is at the start, then draw a line coming to it
				if(s.isStart()) {
					parent.fill(0);
					//Params for the corners of the triangle
					float p1_x = -DIAMETER/2 + s.getPosX();
					float p1_y = 0 + s.getPosY();
					
					float p2_x = - DIAMETER/2 - TRIANGLE_POS + s.getPosX();
					float p2_y = TRIANGLE_POS + s.getPosY();
					
					float p3_x = - DIAMETER/2 - TRIANGLE_POS + s.getPosX();
					float p3_y = - TRIANGLE_POS + s.getPosY();
					//Draw the triangle
					parent.triangle(p1_x, p1_y, p2_x, p2_y, p3_x, p3_y);
					parent.fill(255);
					//Draw the line
					parent.line(s.getPosX() - LINE_LENGTH, s.getPosY(), s.getPosX() - DIAMETER/2, s.getPosY());
				}
				//These are very explicit: if the state is a certain color, then the state will be drawn in that color, else 
				//the state will be white
				if(s.isBlue()){
					parent.fill(0, 0, 255);
				}else if(s.isRed()){
					parent.fill(255, 0, 0);
				}else {
					parent.fill(255);
				}
				
				
				//Draw the ellipse of the state
				parent.ellipse(s.getPosX(), s.getPosY(), DIAMETER, DIAMETER);
				//If the state is at the end, then we draw a smaller ellipse above it, so that we have the impression of having
				//a double circle.
				if(s.isFinish()) 
					parent.ellipse(s.getPosX(), s.getPosY(), DIAMETER - 5, DIAMETER - 5);
				//In the next three line, we draw the name of the state onto the circles.
				String id = s.getId();
				parent.fill(0);
				parent.text(id, s.getPosX() - parent.textWidth(id)/2, s.getPosY() + (parent.textAscent() - parent.textDescent())/2);
			}
		}
	}
	
	/**
	 * Draws the transitions between states
	 * @param auto Automaton
	 */
	public void drawTransitions(Automaton auto) {
		parent.textSize(11);
		Set<State> states = auto.getStates();
		/*
		 * For all states in the automaton
		 */
		for(var s : states) {
			if(s.isVisible()) {
				/*
				 * This part is to search every connection to a state, so that visually the names
				 * don't overlap.
				 * Here we generate all names going to a state.
				 */
				Map<State,List<String>> m = new HashMap<>();
				for(var t : auto.getTransitions(s)) {
					if(!m.containsKey(t.getValue())) {
						List<String> l = new ArrayList<>();
						m.put(t.getValue(), l);
					}
					m.get(t.getValue()).add(t.getKey());
				}
				/*
				 * And here, we create a new transition with the new id.
				 */
				List<Transition<String,State>> list = new ArrayList<>();
				for(var st : m.keySet()) {
					String id = "";
					List<String> lst = m.get(st);
					id = lst.get(0);
					for(int i = 1; i < lst.size(); i++) {
						id += "," + lst.get(i);
					}
					list.add(new Transition<String,State>(id,st));
				}
				/*
				 * For all connections in the new list
				 */
				for(var t : list) {
					//Draw the transition between the state s and the target state
					if(t.getValue().isVisible())
						drawTransition(s, t.getValue(), auto, t.getKey());
					
				}
			}
		}
	}
	
	/**
	 * This function determines if the connection is between two distinct states or to the same state.
	 * @param s1 State
	 * @param s2 State
	 * @param auto Automaton
	 * @param name String The name of the transition
	 */
	private void drawTransition(State s1, State s2, Automaton auto, String name) {
		if(s1 == s2)
			drawCurb(s1, name);
		else
			drawLine(s1, s2, auto, name);
	}
	
	/**
	 * Draws a circle to the current state
	 * @param s State
	 * @param name String The name of the transition
	 */
	private void drawCurb(State s, String name) {
		/**
		 * Push the matrix to the current state
		 */
		parent.pushMatrix();
		parent.translate(s.getPosX(), s.getPosY());
		
		/**
		 * Calculating the corners of the triangle on the tip of the line and drawing it
		 */
		float p1_x = 0;
		float p1_y = -DIAMETER/2;
		
		float p2_x = - TRIANGLE_POS;
		float p2_y = -(DIAMETER/2 + TRIANGLE_POS);
		
		float p3_x = + TRIANGLE_POS;
		float p3_y = -(DIAMETER/2 + TRIANGLE_POS);
		parent.noFill();
		parent.arc(-DIAMETER/2, -DIAMETER/2, DIAMETER, DIAMETER, PApplet.PI/2, 2 * PApplet.PI);
		parent.fill(0);
		parent.triangle(p1_x, p1_y, p2_x, p2_y, p3_x, p3_y);
		
		/**
		 * Set the position of the transition's name and display it
		 */
		float posx = -DIAMETER/2 - parent.textWidth(name)/2 - OFFSET_CURB;
		float posy = -(DIAMETER/2 - (parent.textAscent() - parent.textDescent())/2 + OFFSET_CURB);
		parent.text(name, posx, posy);
		parent.popMatrix();
	}
	
	/**
	 * Draws a line to the next state. If there is a state in between the two states,
	 * then it will be curvy, so that it avoids "hitting" the next state and thus be clear to the user.
	 * @param s1
	 * @param s2
	 * @param auto
	 * @param name
	 */
	private void drawLine(State s1, State s2, Automaton auto, String name) {
		/**
		 * Putting necessary values into variables
		 */
		float s_posX = s1.getPosX();
		float s_posY = s1.getPosY();
		float t_posX = s2.getPosX();
		float t_posY = s2.getPosY();
		/**
		 * Calculating all the lengths of the triangle formed by these two points
		 */
		float adj = t_posX - s_posX;
		float op = t_posY - s_posY;
		float hyp = (float) Math.sqrt(op * op + adj * adj);
		
		/**
		 * Pushing the matrix to the current state
		 */
		parent.pushMatrix();
		parent.translate(s_posX, s_posY);
		
		/**
		 * Calculating the angle formed between the two points and rotate the matrix by that angle
		 */
		double ang = adj < 0 ? Math.PI : 0;
		parent.rotate((float) (Math.atan(op/adj) + ang));
		
		/**
		 * Calculating if there are states in between the two states
		 */
		List<State> ct = auto.getAllDescendants(s1);
		float len = hyp;
		for(var state : ct) {
			float st_posX = state.getPosX();
			float st_posY = state.getPosY();
			float adj_tmp = st_posX - s_posX;
			float op_tmp = st_posY - s_posY;
			if(Math.abs(adj_tmp * op - adj * op_tmp) < 0.01) {
				float hyp_tmp = (float) Math.sqrt(op_tmp * op_tmp + adj_tmp * adj_tmp);
				if(hyp_tmp < len) len = hyp_tmp;
			}
		}
		
		/**
		 * Setting values for the triangles on the tip of the line and drawing them
		 */
		
		float p1_x = hyp - DIAMETER/2;
		float p1_y = - OFFSET_LINE;
		
		float p2_x = hyp - DIAMETER/2 - TRIANGLE_POS;
		float p2_y = - TRIANGLE_POS - OFFSET_LINE;
		
		float p3_x = hyp - DIAMETER/2 - TRIANGLE_POS;
		float p3_y = TRIANGLE_POS - OFFSET_LINE;
		
		parent.fill(0);
		parent.triangle(p1_x, p1_y, p2_x, p2_y, p3_x, p3_y);
		
		/**
		 * Calculate the height of the curve and drawing it
		 */
		float h = 75 * (hyp/len - 1);
		parent.noFill();
		parent.arc(hyp/2 - 2.5f, -5, hyp - DIAMETER - 5, h, PApplet.PI, 2 * PApplet.PI);
		
		/**
		 * Setting the name of the transitions 
		 */

		parent.translate(hyp/2, 0);
		float mult = 1;
		float dy = 0;
		if(ang > Math.PI/2) {
			mult = -1;
			parent.rotate((float) -(Math.PI));
			dy = OFFSET_LINE;
		}
		float posx = -parent.textWidth(name)/2;
		float posy = mult * (- OFFSET_LINE - 3 - h/2) + dy;
		parent.text(name, posx, posy);
		parent.popMatrix();
	}
	
	/**
	 * This is for putting text in the window
	 * @param str String The text we want to put on the screen
	 */
	public void inform(String str) {
		parent.textSize(20);
		parent.text(str, parent.width - parent.textWidth(str) - 50, 50);
	}
}
