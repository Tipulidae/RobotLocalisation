package model;

import java.util.Collections;
import java.util.List;

public interface Position {

	public int getX();
	public int getY();
	public int[] toInt();
	public boolean isWithinBounds(int x, int y, int width, int height);
	public List<Position> neighbours();
	public List<Position> positionsWithinRadius(int r);
	public Heading headingWhenFacing(Position pos);
	public Position nextPositionInHeading(Heading h);
	public Position add(int x, int y);
	public int manhattanDistance(Position p);
	
	public static final Position NULL = new Position() {
		@Override
		public List<Position> neighbours() {
			return Collections.emptyList();
		}

		@Override
		public Heading headingWhenFacing(Position pos) {
			return Heading.NONE;
		}

		@Override
		public Position nextPositionInHeading(Heading h) {
			return this;
		}

		@Override
		public int[] toInt() {
			return null;
		}
		
		@Override
		public String toString() {
			return "NULL";
		}

		@Override
		public boolean isWithinBounds(int x, int y, int width, int height) {
			return false;
		}

		@Override
		public int getX() {
			return -1;
		}

		@Override
		public int getY() {
			return -1;
		}

		@Override
		public Position add(int x, int y) {
			return this;
		}

		@Override
		public List<Position> positionsWithinRadius(int r) {
			return Collections.emptyList();
		}

		@Override
		public int manhattanDistance(Position p) {
			return Integer.MAX_VALUE;
		}
	};
}