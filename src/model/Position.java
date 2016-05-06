package model;

import java.util.ArrayList;
import java.util.List;

public class Position {
	public final int x;
	public final int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public List<Position> neighbours() {
		List<Position> nbs = new ArrayList<Position>();

		nbs.add(new Position(x-1,y));
		nbs.add(new Position(x+1,y));
		nbs.add(new Position(x,y-1));
		nbs.add(new Position(x,y+1));
		
		return nbs;
	}
	
	public Heading headingTo(Position pos) {
		if (pos.x < x && pos.y == y) return Heading.WEST;
		else if (pos.x > x && pos.y == y) return Heading.EAST;
		else if (pos.x == x && pos.y < y) return Heading.NORTH;
		else if (pos.x == x && pos.y > y) return Heading.SOUTH;
		
		return Heading.NONE;
	}

	public Position nextPositionInHeading(Heading h) {
		switch (h) {
		case NORTH:
			return new Position(x,y-1);
		case WEST:
			return new Position(x-1,y);
		case SOUTH:
			return new Position(x,y+1);
		case EAST:
			return new Position(x+1,y);
		default:
			return this;
		}
	}
	
	public String toString() {
		return "("+x+","+y+")";
	}
}
