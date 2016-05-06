package model;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Room {
	private int width;
	private int height;
	private Random r;
	public Room(int width, int height) {
		this.width = width;
		this.height = height;
		r = new Random();
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
	
	public boolean isInsideRoom(Position sensedPosition) {
		return sensedPosition.isWithinBounds(0,0,width,height);
	}

	private List<Heading> headingsNotFacingWalls(Position pos) {
		return pos.neighbours().stream().filter(n -> isInsideRoom(n)).
				map(n -> pos.headingWhenFacing(n)).collect(Collectors.toList());
	}
}

