package model;

public enum Heading {
	NORTH(0), EAST(1), SOUTH(2), WEST(3), NONE(4);
	
	private int heading;
	private Heading(int h) {
		heading = h;
	}
	public static Heading fromInt(int h) {
		switch(h) {
		case 0: return NORTH;
		case 1: return EAST;
		case 2: return SOUTH;
		case 3: return WEST;
		default: return NONE;
		}
	}
	
	public int toInt() {
		return heading;
	}
}
