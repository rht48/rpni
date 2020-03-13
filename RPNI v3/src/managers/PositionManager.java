package managers;


import automaton.Automaton;
import automaton.MCAAutomaton;
import automaton.State;
import buffer.StateBuffer;
import exceptions.UndefinedFirstStateException;

public class PositionManager {
	
	public static void setPositions(MCAAutomaton auto, float width, float height) throws UndefinedFirstStateException {
		int h = auto.getHeight();
		int w = auto.getWidth();
		
		float incrX = width/((float) w + 1.0f);
		float incrY = height/((float) h + 1.0f);
		float posX = incrX;
		float posY = incrY;
		for(var a : auto.getAutos()) {
			setPositions(a.getFirst(), a, posX, posY, incrX, incrY, new StateBuffer());
			posY += a.getHeight() * incrY;
		}
	}
	
	public static void setPositions(Automaton auto, float width, float height) throws UndefinedFirstStateException {
		StateBuffer buff = new StateBuffer();
		State first = auto.getFirst();
		
		int h = auto.getHeight();
		int w = auto.getWidth();

		float incrX = width/((float) w + 1.0f);
		float incrY = height/((float) h + 1.0f);
		
		setPositions(first, auto, incrX, incrY, incrX, incrY, buff);
	}

	private static void setPositions(State s, Automaton auto, float posX, float posY, float incrX, float incrY, StateBuffer buff) {
		buff.add(s);
		s.setPosX(posX);
		s.setPosY(posY);
		posX += incrX;
		for(var t : auto.getTransitions(s)) {
			if(!buff.contains(t.getValue())) {
				setPositions(t.getValue(), auto, posX, posY, incrX, incrY, buff);
				posY += incrY;
			}
		}
	}
	
}
