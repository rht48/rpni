package automaton;

/**
 * This models a state in the automaton
 * @author Romain
 *
 */
public class State {
	
	/**
	 * Position X on the screen.
	 */
	private float posX;
	/**
	 * Position Y on the screen.
	 */
	private float posY;
	/**
	 * Is visible on screen.
	 */
	private boolean isVisible;
	/**
	 * Is at the start of the automaton.
	 */
	private boolean isStart;
	/**
	 * Is at the end of the automaton.
	 */
	private boolean isFinish;
	/**
	 * Id of the state (not a unique ID, it will change over time).
	 * Prefer to use code for a stable ID.
	 * This is what will be show on screen.
	 */
	private String id;
	/**
	 * A static counter in order to generate the code of the state.
	 */
	private static int NUM_CODE = 0;
	/**
	 * Code of the state, unique for each state not cloned.
	 */
	private int code;
	/**
	 * Is the state blue ? (used during the RPNI visual).
	 */
	private boolean isBlue;
	/**
	 * Is the state red ? (used during the RPNI visual).
	 */
	private boolean isRed;
	
	/**
	 * Empty constructor for the State class.
	 * Sets the position X and position Y to 0, and id = "".
	 * Uses the full constructor.
	 */
	public State() {
		this(0, 0, "");
	}
	
	/**
	 * Full constructor for the State class. Sets isVisible to true, isRed, isBlue, isStart, isFinish to false.
	 * Also sets the code for the state.
	 * @param posX float
	 * @param posY float
	 * @param id String
	 */
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
	
	/**
	 * Merges the current state with the next state.
	 * @param s State
	 */
	public void merge(State s) {
		id += "," + s.id;
		isStart = isStart || s.isStart;
		isFinish = isFinish || s.isFinish;
		isRed = isRed || s.isRed;
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
