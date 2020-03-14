package sequence;

public class Move {
	private String type;
	private int code1;
	private int code2;
	private String com;
	
	public Move(String type, String com) {
		this(type, com, -1, -1);
	}
	
	public Move(String type, String com, int code1, int code2) {
		this.type = type;
		this.com = com;
		this.code1 = code1;
		this.code2 = code2;
	}
	
	public String getType() {
		return this.type;
	}
	
	public int getCode1() {
		return this.code1;
	}
	
	public int getCode2() {
		return this.code2;
	}
	
	public String toString() {
		return this.com;
	}
	
}
