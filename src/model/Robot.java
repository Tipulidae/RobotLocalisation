package model;

public class Robot {
	private Position p;
	private Heading h;
	private Room r;
	public Robot() {
	}
	
	public void initialize(Room r) {
		this.r = r;
		p = r.getRandomPosition();
		h = r.getRandomHeading(p);
	}
	
	public void walk() {
		maybeChangeHeading();
		p = p.nextPositionInHeading(h);
	}
	
	private void maybeChangeHeading() {
		if (r.isFacingWall(p,h)) {
			h = r.getRandomHeading(p);
		} else {
			// Roll dice...
			//h = r.getRandomHeading(p);
		}
	}
	
	public String toString() {
		return "Robot is at "+p+" heading "+h;
	}
}
