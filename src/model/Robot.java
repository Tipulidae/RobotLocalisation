package model;

import java.util.Random;

public class Robot {
	private Position p;
	private Heading h;
	private Room room;
	private Sensor sensor;
	private SensorFilter filter;
	private Random rand = new Random();
	public Robot(Room r) {
		this.room = r;
		p = r.getRandomPosition();
		h = r.getRandomHeading(p);
		sensor = new Sensor(room);
		filter = new SensorFilter(room);
	}
	
	public void walk() {
		maybeChangeHeading();
		p = p.nextPositionInHeading(h);
		sensor.updateSensorReading(p);
		filter.updatef(sensor.getReading());
		filter.estimateError(p);
	}
	
	public String toString() {
		return "Robot is at "+p+" heading "+h+"\nRobot thinks it is at "+sensorReading();
	}
	
	public Position getPos() {
		return p;
	}
	
	public Position sensorReading() {
		return sensor.getReading();
	}
	
	public double getProbabilityForPosition(Position p) {
		return filter.getf(p);
	}
	
	private void maybeChangeHeading() {
		if (room.isFacingWall(p,h) || rand.nextDouble() < 0.3) {
			Heading oldHeading = h;
			while (oldHeading == h) {
				h = room.getRandomHeading(p);
			}
		}
	}

	public void printError(){
		filter.printError();
	}

	public double probabilityToObserveEvidenceWhenInPos(Position evidence, Position pos) {
		return filter.getSensorModel(evidence, pos);
	}

	public double probabilityToTransitionFromAtoB(Position a, Heading ha, Position b, Heading hb) {
		return filter.getTransitionModel(a, ha, b, hb);
	}
}
