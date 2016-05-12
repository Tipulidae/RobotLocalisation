package model;

import java.util.ArrayList;
import java.util.Collections;
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
	
	public List<Position> positionsWithinRadius(int r) {
		List<Position> positions = new ArrayList<Position>();
		for (int nx=x-r; nx<=x+r; nx++) {
			for (int ny=y-r; ny<=y+r; ny++) {
				positions.add(new Position(nx,ny));
			}
		}
		return positions;
	}
	
	public Heading headingWhenFacing(Position pos) {
		if (pos.x == x && pos.y < y) return Heading.NORTH;
		else if (pos.x < x && pos.y == y) return Heading.WEST;
		else if (pos.x == x && pos.y > y) return Heading.SOUTH;
		else if (pos.x > x && pos.y == y) return Heading.EAST;
		
		return Heading.NONE;
	}

	public Position nextPositionInHeading(Heading h) {
		switch (h) {
		case NORTH: return new Position(x,y-1);
		case WEST: return new Position(x-1,y);
		case SOUTH:	return new Position(x,y+1);
		case EAST: return new Position(x+1,y);
		default: return this;
		}
	}
	
	public int[] toInt() {
		return new int[]{x,y};
	}
	
	public Position add(int x, int y) {
		return new Position(this.x + x, this.y + y);
	}
	
	public boolean isWithinBounds(int x, int y, int width, int height) {
		return this.x >= x && this.x < x+width && this.y >= y && this.y < y+height;
	}
	
	public String toString() {
		return "("+x+","+y+")";
	}
	
	public boolean equals(Object o) {
		if (o instanceof Position) {
			Position p = (Position)o;
			return x == p.x && y == p.y;
		}
		return false;
	}
	
	public static int taxiNorm(Position a, Position b) {
		return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
	}
	
	public static int maxNorm(Position a, Position b) {
		return Math.max(Math.abs(a.x-b.x), Math.abs(a.y-b.y));
	}
	
	
	
	public static final Position NULL = new Position(0,0) {
		@Override
		public List<Position> neighbours() {
			return Collections.emptyList();
		}
		
		@Override
		public List<Position> positionsWithinRadius(int r) {
			return Collections.emptyList();
		}

		@Override
		public Position nextPositionInHeading(Heading h) {
			return this;
		}
		
		@Override
		public Position add(int x, int y) {
			return this;
		}
		
		@Override
		public Heading headingWhenFacing(Position pos) {
			return Heading.NONE;
		}

		@Override
		public boolean isWithinBounds(int x, int y, int width, int height) {
			return false;
		}

		@Override
		public int[] toInt() {
			return null;
		}
		
		@Override
		public String toString() {
			return "NULL";
		}
	};
}
