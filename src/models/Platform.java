package models;

import java.awt.Point;

public class Platform extends Collider {
	public static final int HIGTH = 25;
	public static final int MAX_WIDTH = 250;
	public static final int MIN_WIDTH = 50;
	public static final int MAX_Y_POSITION = Game.MAP_HIGTH - HIGTH - 100;
	public static final int MIN_Y_POSITION = 75;
	public static final int MOVE_SIZE = 1;

	public Platform(Point position, int width) {
		super(position, width, HIGTH);
	}

	public boolean move() {
		super.position.x -= MOVE_SIZE;
		return position.x + width - MOVE_SIZE < 0;
	}
}
