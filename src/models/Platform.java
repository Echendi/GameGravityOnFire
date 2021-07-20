package models;

import java.awt.Point;

public class Platform extends Collider {
	public static final int HIGTH = 25;
	public static final int MAX_WIDTH = 250;
	public static final int MIN_WIDTH = 50;
	public static final int MAX_Y_POSITION = Game.MAP_HEIGTH - HIGTH - 100;
	public static final int MIN_Y_POSITION = 75;
	public static int moveSize = 1;

	public static void setMoveSize(int moveSize) {
		Platform.moveSize = moveSize;
	}

	public Platform(Point position, int width) {
		super(position, width, HIGTH);
	}

	public boolean move() {
		super.position.x -= moveSize;
		return position.x + width - moveSize < 0;
	}
}
