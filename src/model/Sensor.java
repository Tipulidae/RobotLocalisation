package model;

import java.util.Random;

public class Sensor {
	private Room room;
	private Position reading = Position.NULL;
	private Random r = new Random();
	
	public Sensor(Room room) {
		this.room = room;
	}

	public void updateSensorReading(Position truePosition) {
		double roll = r.nextDouble();
		if (roll < 0.625) {
			int n = r.nextInt(25);
			reading = truePosition.add(n%5-2, n/5-2);
		} else if (roll < 0.85) {
			int n = r.nextInt(9);
			reading = truePosition.add(n%3-1, n/3-1);
		} else if (roll < 0.9) {
			reading = truePosition;
		} else {
			reading = Position.NULL;
		}
		
		if (!room.isInsideRoom(reading)) {
			reading = Position.NULL;
		}
	}

	public Position getReading() {
		return reading;
	}
}
