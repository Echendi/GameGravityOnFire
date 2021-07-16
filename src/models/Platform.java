package models;

import java.awt.Point;

public class Platform {
	public static final int HIGTH = 30;
	public static final int MAX_WIDTH = 200;
	public static final int MIN_WIDTH = 50;
	public static final int MAX_Y_POSITION = Game.MAP_HIGTH - HIGTH-100;
	public static final int MIN_Y_POSITION = 100;
	public static final int MOVE_SIZE = 10;
	private Point position;
	private int width;

	public Platform(Point position, int width) {
		this.position = position;
		this.width = width;
	}

	public boolean move() {
		position.x -= MOVE_SIZE;
		return position.x + width - MOVE_SIZE < 100;
	}

	public Point getPosition() {
		return position;
	}

	public int getWidth() {
		return width;
	}
}
