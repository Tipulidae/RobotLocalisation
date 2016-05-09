package model;

import java.util.Random;

public class Sensor {
	private Room room;
	private Position sensedPosition;
	private Random r = new Random();
	private double[][] T;
	public Sensor(Room room) {
		this.room = room;
		initT();
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
	
	private void initT() {
		int width = room.getWidth();
		int height = room.getHeight();
		int S = height*width*4;
		T = new double[S][S];
		
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++){
				Position pos = new CartesianPosition(x,y);

				for (int nY=0; nY<height; nY++) {
					for (int nX=0; nX<width; nX++) {
						Position newPos = new CartesianPosition(nX,nY);
						
						for (int h=0; h<4; h++) {
							Heading heading = Heading.fromInt(h);
							for (int nH=0; nH<4; nH++) {
								Heading newHeading = Heading.fromInt(nH);

								int tx = h + 4*x + 4*y*width;
								int ty = nH + nX*4 + nY*4*width;
								
								if (!room.neighbours(pos).contains(newPos)) continue;
								if (pos.headingWhenFacing(newPos) != newHeading) continue;
								
								if (room.isFacingWall(pos, heading)) {
									T[tx][ty] = 1.0/room.numValidHeadings(pos);
									
								} else if (heading == newHeading) {
									T[tx][ty] = 0.7;
								} else {
									T[tx][ty] = 0.3/(room.numValidHeadings(pos)-1);
								}
							}
						}
					}
				}
			}
		}
		
		
	}


	public double getT(int tx, int ty) {
		//System.out.println("T["+tx+"]["+ty+"]="+T[tx][ty]);
		return T[tx][ty];
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
