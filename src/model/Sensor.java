package model;

import java.util.Random;

public class Sensor {
	private Room room;
	private Position sensedPosition = Position.NULL;
	private Random r = new Random();
	private double[][] T;
	private double[][] O;
	private int width;
	private int height;
	private int S;
	private double[] f;
	public Sensor(Room room) {
		this.room = room;
		width = room.getWidth();
		height = room.getHeight();
		S = height*width*4;
		initT();
		initO();
		initf();
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
		updatef();
		
	}

	public Position getSensedPosition() {
		return sensedPosition;
	}
	
	private void initT() {
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
	
	private void doit(Position a, Position b) {
		double o = 0.1;
		int dx = Math.abs(a.getX() - b.getX());
		int dy = Math.abs(a.getY() - b.getY());
		int m = Math.max(dx, dy);
		if (m == 2) {
			o += 0.025;
		} else if (m == 1) {
			o += 0.05;
		}
		for (int h=0; h<4; h++) {
			O[h+4*a.getX()+4*width*a.getY()][width*height] = o;
		}
	}
	
	public void initO() {
		O = new double[S][width*height+1];
		
		
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				Position p = new CartesianPosition(x,y);
				p.positionsWithinRadius(2).stream().filter(n -> !room.isInsideRoom(n)).
					forEach(n -> doit(p, n));
			}
		}
		
		
		for (int rx=0; rx<width; rx++) {
			for (int ry=0; ry<height; ry++) {
				
				for (int x=0; x<width; x++) {
					for (int y=0; y<height; y++) {
						
						int dx = Math.abs(rx - x);
						int dy = Math.abs(ry - y);
						int m = Math.max(dx, dy);
						double o=0;
						if (m == 2) {
							o = 0.025;
						} else if (m == 1) {
							o = 0.05;
						} else if (m == 0) {
							o = 0.1;
						}
						for (int h=0; h<4; h++) {
							O[h+4*x+4*width*y][rx+ry*width] = o;
						}
					}
				}
				
			}
		}
		
	}

	public double getT(int tx, int ty) {
		return T[tx][ty];
	}
	
	public double getO(int rx, int ry, int x, int y) {
		if (!room.isInsideRoom(new CartesianPosition(rx,ry))) {
			return O[x*4+y*width*4][width*height];
		}
		return O[x*4+y*width*4][rx+ry*width];
	}
	
	private void initf() {
		f = new double[S];
		double t = 1.0/S;
		for (int i = 0; i < S; i++) {
			f[i] = t;
		}
	}
	
	private void updatef() {
		double[] newf = new double[S];
				
		for (int i = 0; i < S; i++) {
			for (int j = 0; j < S; j++) {
				newf[i] += T[j][i] * f[j]; 
			}
		}
		
		int ind = 0;
		if (sensedPosition == Position.NULL) {
			ind = width*height;
		} else {
			ind = sensedPosition.getX() + width*sensedPosition.getY();
		}
		
		double alpha = 0;
		for (int i = 0; i < S; i++) {
			f[i] = O[i][ind]*newf[i];
			alpha += f[i];
		}
		
		for (int i = 0; i < S; i++) {
			f[i] /= alpha;
		}
		
	}
	
	public double getf(int x, int y) {
		double sum = 0;
		for (int i = 4*(x + width*y); i < 4*(x + width*y) + 4; i++) {
			sum += f[i];
		}
		return sum;
	}
	
}
