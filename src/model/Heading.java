package model;

public enum Heading {
	NORTH, WEST, SOUTH, EAST, NONE;
	
	public static Heading fromInt(int h) {
		switch(h) {
		case 0: return NORTH;
		case 1: return EAST;
		case 2: return SOUTH;
		case 3: return WEST;
		default: return NONE;
		}
	}
}
