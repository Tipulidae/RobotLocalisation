package control;

import model.RobotCommandCenter;
import view.RobotLocalizationViewer;

public class Main {
	public static void main(String[] args) {
		EstimatorInterface l = new RobotCommandCenter(6,6);
		RobotLocalizationViewer viewer = new RobotLocalizationViewer(l);
		new LocalizationDriver(500, viewer).start();
	}
}	