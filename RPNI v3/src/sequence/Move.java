package sequence;

/**
 * This stores a given move.
 * @author Romain
 *
 */
public class Move {
	/**
	 * Type of the move (useful for determining the action to be done)
	 */
	private String type;
	/**
	 * Code of the state s1.
	 */
	private int code1;
	/**
	 * Code for the state s2.
	 */
	private int code2;
	/**
	 * Commentary for the action.
	 */
	private String com;
	
	/**
	 * Constructor with only the type and commentary. Sets the two codes to -1.
	 * @param type
	 * @param com
	 */
	public Move(String type, String com) {
		this(type, com, -1, -1);
	}
	
	/**
	 * Full constructor for the Move class.
	 * @param type
	 * @param com
	 * @param code1
	 * @param code2
	 */
	public Move(String type, String com, int code1, int code2) {
		this.type = type;
		this.com = com;
		this.code1 = code1;
		this.code2 = code2;
	}
	
	/**
	 * A simple getter for the type.
	 * @return String
	 */
	public String getType() {
		return this.type;
	}
	
	/**
	 * A simple getter for code1.
	 * @return int
	 */
	public int getCode1() {
		return this.code1;
	}
	
	/**
	 * A simple getter for the code2
	 * @return
	 */
	public int getCode2() {
		return this.code2;
	}
	
	/**
	 * Returns the commentary of the move.
	 */
	public String toString() {
		return this.com;
	}
	
}
