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
		return new Position(r.nextInt(width), r.nextInt(height));
	}
	
	private List<Heading> headingsNotFacingWalls(Position pos) {
		return pos.neighbours().stream().filter(p -> isInsideRoom(p)).
				map(p -> pos.headingTo(p)).collect(Collectors.toList());
	}
	
	private boolean isInsideRoom(Position pos) {
		return pos.x >= 0 && pos.x < width && pos.y >= 0 && pos.y < height;
	}
}
/*


Room room = new Room(5,5);
Position p = room.getRandomPosition();
Robot robot = new Robot();
robot.setPosition(p);
robot.setHeading(Heading.random());

robot.walk(room);

walk(Room room) {
	
}

if (room.isBlocked(robot)) {
	Heading free = room.getFreeHeading(robot.getPosition());
	robot.setHeading(free);
}

boolean isEncounteringWall = room.isBlocked(robot.getPosition(), robot.getHeading());
robot.pickNewHeading(isEncounteringWall);
robot.walk();


*/
