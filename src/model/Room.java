package model;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Room {
	private int width;
	private int height;
	private Random r = new Random();
	public Room(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public Heading getRandomHeading(Position pos) {
		List<Heading> headings = headingsNotFacingWalls(pos);
		return headings.get(r.nextInt(headings.size()));
	}
	
	public boolean isFacingWall(Position pos, Heading h) {
		return !headingsNotFacingWalls(pos).contains(h);
	}
	
	public Position getRandomPosition() {
		return new CartesianPosition(r.nextInt(width), r.nextInt(height));
	}
	
	public boolean isInsideRoom(Position pos) {
		return pos.isWithinBounds(0,0,width,height);
	}
	
	public List<Position> neighbours(Position pos) {
		return pos.neighbours().stream().filter(n -> isInsideRoom(n)).collect(Collectors.toList());
	}
	
	public int numValidHeadings(Position pos) {
		return headingsNotFacingWalls(pos).size();
		//if (isCorner(pos)) return 2;
		//if (isWall(pos)) return 3;
		//return 4;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	private boolean isWall(Position pos) {
		return (pos.getX() == 0 || pos.getX() == width-1) || (pos.getY() == 0 || pos.getY() == height-1);
	}
	
	private boolean isCorner(Position pos) {
		return (pos.getX() == 0 || pos.getX() == width-1) && (pos.getY() == 0 || pos.getY() == height-1);
	}
	
	private List<Heading> headingsNotFacingWalls(Position pos) {
		return pos.neighbours().stream().filter(n -> isInsideRoom(n)).
				map(n -> pos.headingWhenFacing(n)).collect(Collectors.toList());
	}
}

