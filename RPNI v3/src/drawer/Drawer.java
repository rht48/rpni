package drawer;

import java.util.List;
import java.util.Set;

import automaton.Automaton;
import automaton.State;
import processing.core.PApplet;

public class Drawer {
	private PApplet parent;
	public static final float DIAMETER = 50;
	private static final float TRIANGLE_POS = 5;
	private static final float OFFSET_CURB = 4;
	private static final float OFFSET_LINE = 5;
	private static final float LINE_LENGTH = 70;
	
	public Drawer(PApplet parent) {
		this.parent = parent;
	}
	
	public void drawStates(Automaton auto) {
		parent.textSize(11);
		Set<State> states = auto.getStates();
		for(var s : states) {
			if(s.isVisible()) {
				if(s.isStart()) {
					parent.fill(0);
					float p1_x = -DIAMETER/2 + s.getPosX();
					float p1_y = 0 + s.getPosY();
					
					float p2_x = - DIAMETER/2 - TRIANGLE_POS + s.getPosX();
					float p2_y = TRIANGLE_POS + s.getPosY();
					
					float p3_x = - DIAMETER/2 - TRIANGLE_POS + s.getPosX();
					float p3_y = - TRIANGLE_POS + s.getPosY();
					parent.triangle(p1_x, p1_y, p2_x, p2_y, p3_x, p3_y);
					parent.fill(255);
					parent.line(s.getPosX() - LINE_LENGTH, s.getPosY(), s.getPosX() - DIAMETER/2, s.getPosY());
				}
				if(s.isBlue()){
					parent.fill(0, 0, 255);
				}else if(s.isRed()){
					parent.fill(255, 0, 0);
				}else {
					parent.fill(255);
				}
				String id = s.getId();
				parent.ellipse(s.getPosX(), s.getPosY(), DIAMETER, DIAMETER);
				if(s.isFinish()) 
					parent.ellipse(s.getPosX(), s.getPosY(), DIAMETER - 5, DIAMETER - 5);
				parent.fill(0);
				parent.text(id, s.getPosX() - parent.textWidth(id)/2, s.getPosY() + (parent.textAscent() - parent.textDescent())/2);
			}
		}
	}
	
	public void drawTransitions(Automaton auto) {
		parent.textSize(11);
		Set<State> states = auto.getStates();
		for(var s : states) {
			if(s.isVisible()) {
				for(var t : auto.getTransitions(s)) {
					if(t.getValue().isVisible())
						drawTransition(s, t.getValue(), auto, t.getKey());
					
				}
			}
		}
	}
	
	private void drawTransition(State s1, State s2, Automaton auto, String name) {
		if(s1 == s2)
			drawCurb(s1, name);
		else
			drawLine(s1, s2, auto, name);
	}
	
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
	
	public void inform(String str) {
		parent.textSize(20);
		parent.text(str, parent.width - parent.textWidth(str) - 50, 50);
	}
}
