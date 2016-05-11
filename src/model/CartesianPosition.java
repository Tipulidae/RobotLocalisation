package model;

import java.util.ArrayList;
import java.util.List;

public class CartesianPosition implements Position {
	private final int x;
	private final int y;
	
	public CartesianPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public List<Position> neighbours() {
		List<Position> nbs = new ArrayList<Position>();

		nbs.add(new CartesianPosition(x-1,y));
		nbs.add(new CartesianPosition(x+1,y));
		nbs.add(new CartesianPosition(x,y-1));
		nbs.add(new CartesianPosition(x,y+1));
		
		return nbs;
	}
	
	@Override
	public List<Position> positionsWithinRadius(int r) {
		List<Position> positions = new ArrayList<Position>();
		for (int nx=x-r; nx<=x+r; nx++) {
			for (int ny=y-r; ny<=y+r; ny++) {
				positions.add(new CartesianPosition(nx,ny));
			}
		}
		return positions;
	}
	
	@Override
	public Heading headingWhenFacing(Position pos) {
		if (pos.getX() == x && pos.getY() < y) return Heading.NORTH;
		else if (pos.getX() < x && pos.getY() == y) return Heading.WEST;
		else if (pos.getX() == x && pos.getY() > y) return Heading.SOUTH;
		else if (pos.getX() > x && pos.getY() == y) return Heading.EAST;
		
		return Heading.NONE;
	}

	@Override
	public Position nextPositionInHeading(Heading h) {
		switch (h) {
		case NORTH: return new CartesianPosition(x,y-1);
		case WEST: return new CartesianPosition(x-1,y);
		case SOUTH:	return new CartesianPosition(x,y+1);
		case EAST: return new CartesianPosition(x+1,y);
		default: return this;
		}
	}

	

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}
	
	@Override
	public int[] toInt() {
		return new int[]{x,y};
	}

	@Override
	public Position add(int x, int y) {
		return new CartesianPosition(this.x + x, this.y + y);
	}
	
	@Override
	public boolean isWithinBounds(int x, int y, int width, int height) {
		return this.x >= x && this.x < x+width && this.y >= y && this.y < y+height;
	}
	
	@Override
	public String toString() {
		return "("+x+","+y+")";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Position) {
			Position p = (Position)o;
			return x == p.getX() && y == p.getY();
		}
		return false;
	}

	@Override
	public int manhattanDistance(Position p) {
		return Math.abs(p.getX() - x) + Math.abs(p.getY() - y);
	}
}
