package control;

import view.*;

public class LocalisationDriver extends Thread {
	
	private RobotLocalisationViewer l;
	long timer;
	
	public LocalisationDriver(long stepTime, RobotLocalisationViewer v) {
		this.l = v;
		this.timer = stepTime;
	}
	
	public void run() {
		while (!isInterrupted()) {
			try{
				l.updateContinuously();
				sleep(timer);
			} catch (InterruptedException e) {
				System.out.println("oops");
			}

		}
	}
	
}