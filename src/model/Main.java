package model;

public class Main {
	public static void main(String[] args) {
		Room room = new Room(5,5);
		Robot robot = new Robot(room);
		
		System.out.println(robot);
		
		for (int i=0; i<10; i++) {
			robot.walk();
			System.out.println(robot);
		}
		
	}
}
