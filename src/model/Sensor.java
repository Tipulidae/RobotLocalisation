package model;

import java.util.Random;

public class Sensor {
	private Room room;
	private Position sensedPosition;
	private Random r = new Random();
	public Sensor(Room room) {
		this.room = room;
		System.out.println("test");
	}

	public void sensePositionChanged(Position p) {
		double roll = r.nextDouble();
		if (roll < 0.625) {
			int n = r.nextInt(25);
			sensedPosition = p.add(n%5-2, n/5-2);
		} else if (roll < 0.85) {
			int n = r.nextInt(9);
			sensedPosition = p.add(n%3-1, n/3-1);
		} else if (roll < 0.9) {
			sensedPosition = p;
		} else {
			sensedPosition = Position.NULL;
		}
		
		if (!room.isInsideRoom(sensedPosition)) {
			sensedPosition = Position.NULL;
		}
	}

	public Position getSensedPosition() {
		return sensedPosition;
	}
	
	
	// Just a quick test to verify that the probabilities are ok.
	public static void main(String[] args) {
		new Sensor(new Room(5,5)).test();
	}
	
	private void test() {
		int[][] count = new int[5][5];
		CartesianPosition middle = new CartesianPosition(2,2);
		int trials = 10000;
		double dtrials = trials;
		for (int i=0; i<trials; i++) {
			sensePositionChanged(middle);
			int[] p = sensedPosition.toInt();
			if (p != null)
				count[p[0]][p[1]]++;
		}
		
		for (int y=0; y<5; y++) {
			for (int x=0; x<5; x++) {
				System.out.print(count[x][y]/dtrials+" ");
			}
			System.out.println();
		}
	}
}
