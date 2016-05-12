package model;

import java.util.ArrayList;
import java.util.List;

public class SensorFilter {
	private Room room;
	private double[][] T;
	private double[][] O;
	private int width;
	private int height;
	private int S;
	private double[] f;
	private List<Integer> error;
	
	public SensorFilter(Room room) {
		this.room = room;
		width = room.getWidth();
		height = room.getHeight();
		S = height*width*4;
		initT();
		initO();
		initf();
		error = new ArrayList<Integer>();
	}
	
	
	private void initT() {
		T = new double[S][S];
		
		for (int y=0; y<height; y++) {
			for (int x=0; x<width; x++){
				Position pos = new Position(x,y);

				for (int nY=0; nY<height; nY++) {
					for (int nX=0; nX<width; nX++) {
						Position newPos = new Position(nX,nY);
						
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
		double o = 0;
		int m = Position.maxNorm(a,b);
		if (m == 2) {
			o = 0.025;
		} else if (m == 1) {
			o = 0.05;
		}
		
		for (int h=0; h<4; h++) {
			O[h+4*a.x+4*width*a.y][width*height] += o;
		}
	}
	
	public void initO() {
		O = new double[S][width*height+1];
		
		
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				for (int h=0; h<4; h++) {
					O[h+4*x+4*width*y][width*height] = 0.1;
				}
				Position p = new Position(x,y);
				p.positionsWithinRadius(2).stream().filter(n -> !room.isInsideRoom(n)).
					forEach(n -> doit(p, n));
			}
		}
		
		
		for (int rx=0; rx<width; rx++) {
			for (int ry=0; ry<height; ry++) {
				for (int x=0; x<width; x++) {
					for (int y=0; y<height; y++) {
						
						int m = Position.maxNorm(new Position(x,y), new Position(rx,ry));
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
	
	public double getTransitionModel(Position fromPos, Heading fromHeading, Position toPos, Heading toHeading) {
		//getT(h + x*4 + y*4*width, nH + nX*4 + nY*4*width)
		return T[fromHeading.toInt() + 4*fromPos.x + 4*width*fromPos.y][toHeading.toInt() + 4*toPos.x + 4*width*toPos.y];
		//return 0;
	}
	
	public double getO(int rx, int ry, int x, int y) {
		if (!room.isInsideRoom(new Position(rx,ry))) {
			return O[x*4+y*width*4][width*height];
		}
		return O[x*4+y*width*4][rx+ry*width];
	}
	
	public double getSensorModel(Position evidence, Position p) {
		if (!room.isInsideRoom(evidence)) {
			return O[p.x*4+p.y*width*4][width*height];
		}
		return O[p.x*4+p.y*width*4][evidence.x+evidence.y*width];
	}
	
	private void initf() {
		f = new double[S];
		double t = 1.0/S;
		for (int i = 0; i < S; i++) {
			f[i] = t;
		}
	}
	
	public void updatef(Position reading) {
		double[] newf = new double[S];
				
		for (int i = 0; i < S; i++) {
			for (int j = 0; j < S; j++) {
				newf[i] += T[j][i] * f[j]; 
			}
		}
		
		int ind = 0;
		if (reading == Position.NULL) {
			ind = width*height;
		} else {
			ind = reading.x + width*reading.y;
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
	
	public double getf(Position p) {
		double sum = 0;
		for (int i = 4*(p.x + width*p.y); i < 4*(p.x + width*p.y) + 4; i++) {
			sum += f[i];
		}
		return sum;
	}
	
	public void estimateError(Position correctPosition) {
		error.add(Position.taxiNorm(correctPosition, mostLikelyPosition()));
	}
	
	public Position mostLikelyPosition() {
		int maxX=-1, maxY=-1;
		double maxF = 0;
		
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				double summedF = 0;
				for (int h=0; h<4; h++) {
					summedF += f[h+4*x+4*width*y];
				}
				if (summedF > maxF) {
					maxF = summedF;
					maxX = x;
					maxY = y;
				}
			}
		}
		return new Position(maxX, maxY);
	}
	
	public void printError() {
		for (int i : error) {
			System.out.print(i+" ");
		}
		System.out.println();
	}
}
