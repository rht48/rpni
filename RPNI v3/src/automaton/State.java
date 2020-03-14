package automaton;

public class State {
	
	private float posX;
	private float posY;
	private boolean isVisible;
	private boolean isStart;
	private boolean isFinish;
	private String id;
	private static int NUM_CODE = 0;
	private int code;
	private boolean isBlue;
	private boolean isRed;
	
	public State() {
		this(0, 0, "");
	}
	
	public State(float posX, float posY, String id) {
		this.posX = posX;
		this.posY = posY;
		this.id = id;
		this.isVisible = true;
		this.isStart = false;
		this.isFinish = false;
		this.code = NUM_CODE++;
		this.isBlue = false;
		this.isRed = false;
	}
	
	public void merge(State s) {
		id += "," + s.id;
		isStart = isStart || s.isStart;
		isFinish = isFinish || s.isFinish;
		isRed = isRed || s.isRed;
		//isBlue = isBlue || s.isBlue;
	}
	
	public State clone() {
		State s = new State(posX, posY, id);
		s.isFinish = isFinish;
		s.isStart = isStart;
		s.isVisible = s.isVisible;
		s.code = code;
		s.isRed = isRed;
		s.isBlue = isBlue;
		NUM_CODE--;
		return s;
	}
	
	public int getCode() {
		return code;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
	}

	public float getPosY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setBlue(boolean b) {
		this.isBlue = b;
	}
	
	public boolean isBlue() {
		return isBlue;
	}
	
	public void setRed(boolean b) {
		this.isRed = b;
	}
	
	public boolean isRed() {
		return this.isRed;
	}
	
	public boolean equals(Object o) {
		if(o instanceof State) {
			State s = (State)o;
			return this.code == s.code;
		}
		return false;
	}
	
}
