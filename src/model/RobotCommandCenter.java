package model;

import control.EstimatorInterface;

public class RobotCommandCenter implements EstimatorInterface {
	private int width, height;
	private Robot robot;
	private Room room;
	
	public RobotCommandCenter(int width, int height) {
		this.width = width;
		this.height = height;
		room = new Room(width, height);
		robot = new Robot(room);
	}
	
	@Override
	public int getNumRows() {
		return height;
	}

	@Override
	public int getNumCols() {
		return width;
	}

	@Override
	public int getNumHead() {
		return 4;
	}

	@Override
	public void update() {
		robot.walk();
	}

	@Override
	public int[] getCurrentTruePosition() {
		return robot.getPos().toInt();
	}

	@Override
	public int[] getCurrentReading() {
		return robot.sensePosition().toInt();
	}

	@Override
	public double getCurrentProb(int x, int y) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getOrXY(int rX, int rY, int x, int y) {
		// TODO Auto-generated method stub
		return 0.1;
	}

	@Override
	public double getTProb(int x, int y, int h, int nX, int nY, int nH) {
		
		return robot.getSensor().getT(h + x*4 + y*4*width, h + nX*4 + nY*4*width);
		/*
		Position pos = new CartesianPosition(x,y);
		Position newPos = new CartesianPosition(nX,nY);
		
		Heading heading = Heading.fromInt(h);
		Heading newHeading = Heading.fromInt(nH);
		
		if (!room.neighbours(pos).contains(newPos)) return 0;
		if (pos.headingWhenFacing(newPos) != newHeading) return 0;
		
		if (room.isFacingWall(pos, heading)) {
			return 1.0/room.numValidHeadings(pos);
		} else if (heading == newHeading) {
			return 0.7;
		}
		
		return 0.3/(room.numValidHeadings(pos)-1);
		*/
		/*if (!contai)
		
		// Probability to go from x,y,h to nX, nY, nH
		int dx = x-nX;
		int dy = y-nY;
		if (Math.abs(dx)+Math.abs(dy) != 1) return 0;
		
		int[] correct = {-2,-1,1,2};
		if (correct[nH] != 2*dx+dy) return 0;
		
		*/
		//return 0;
	}
}
