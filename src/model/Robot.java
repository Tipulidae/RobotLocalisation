package model;

import java.util.Random;

public class Robot {
	private Position p;
	private Heading h;
	private Room room;
	private Sensor sensor;
	private Random rand = new Random();
	public Robot(Room r) {
		this.room = r;
		p = r.getRandomPosition();
		h = r.getRandomHeading(p);
		sensor = new Sensor(room);
	}
	
	public void walk() {
		maybeChangeHeading();
		p = p.nextPositionInHeading(h);
		sensor.sensePositionChanged(p);
	}
	
	public String toString() {
		return "Robot is at "+p+" heading "+h+"\nRobot thinks it is at "+sensePosition();
	}
	
	public Position getPos() {
		return p;
	}
	
	public Position sensePosition() {
		return sensor.getSensedPosition();
	}
	
	
	private void maybeChangeHeading() {
		if (room.isFacingWall(p,h) || rand.nextDouble() < 0.3) {
			Heading oldHeading = h;
			while (oldHeading == h) {
				h = room.getRandomHeading(p);
			}
		}
	}

	public Sensor getSensor() {
		return sensor;
	}
}
