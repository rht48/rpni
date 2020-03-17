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
	
	/**
	 * Clones the current state.
	 * @return State
	 */
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
	
	/**
	 * A simple getter for the code.
	 * @return int code
	 */
	public int getCode() {
		return code;
	}
	
	/**
	 * A simple getter for the position X.
	 * @return float posX
	 */
	public float getPosX() {
		return posX;
	}
	
	/**
	 * Setter for the position X.
	 * @param float posX
	 */
	public void setPosX(float posX) {
		this.posX = posX;
	}

	/**
	 * A simple getter for the position Y.
	 * @return float posY
	 */
	public float getPosY() {
		return posY;
	}
	
	/**
	 * Setter for the position Y.
	 * @param float posY
	 */
	public void setPosY(float posY) {
		this.posY = posY;
	}
	
	/**
	 * Is the current state visible ?
	 * @return boolean
	 */
	public boolean isVisible() {
		return isVisible;
	}
	
	/**
	 * Setter for isVisible variable.
	 * @param isVisible
	 */
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	/**
	 * Is the current state at the start ?
	 * @return boolean
	 */
	public boolean isStart() {
		return isStart;
	}
	
	/**
	 * Setter for isStart variable.
	 * @param isStart
	 */
	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}
	
	/**
	 * Is the current state at the end ?
	 * @return boolean
	 */
	public boolean isFinish() {
		return isFinish;
	}
	
	/**
	 * Setter for the isFinish variable.
	 * @param isFinish
	 */
	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}
	
	/**
	 * A simple getter for the id of the state
	 * @return String
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id of the state (not a unique ID).
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Sets the isBlue variable.
	 * @param b
	 */
	public void setBlue(boolean b) {
		this.isBlue = b;
	}
	
	/**
	 * Is the current state blue ?
	 * @return boolean
	 */
	public boolean isBlue() {
		return isBlue;
	}
	
	/**
	 * Sets the isRed variable.
	 * @param b
	 */
	public void setRed(boolean b) {
		this.isRed = b;
	}
	
	/**
	 * Is the current state red ?
	 * @return boolean
	 */
	public boolean isRed() {
		return this.isRed;
	}
	
	/**
	 * Two states are equal when their codes are the same.
	 * Note that it isn't based on the id, only the code.
	 * @return boolean
	 */
	public boolean equals(Object o) {
		if(o instanceof State) {
			State s = (State)o;
			return this.code == s.code;
		}
		return false;
	}
	
}
