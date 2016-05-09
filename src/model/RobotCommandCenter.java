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
		return robot.getSensor().getT(h + x*4 + y*4*width, nH + nX*4 + nY*4*width);
	}
}
