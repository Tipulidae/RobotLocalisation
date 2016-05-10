package control;

import model.Robot;
import model.RobotCommandCenter;
import model.Room;
import view.RobotLocalizationViewer;

public class Main {
	public static void main(String[] args) {
		
		EstimatorInterface l = new RobotCommandCenter(10,10);
		RobotLocalizationViewer viewer = new RobotLocalizationViewer(l);
		new LocalizationDriver(500, viewer).start();
		
		
		/*
		for (int j = 0; j < 1; j++) {
			Room room = new Room(12, 12);
			Robot robot = new Robot(room);
				for (int i=0; i<1000; i++) {
					robot.walk();
				}
			robot.getSensor().printError();
		}*/
		
	}
}