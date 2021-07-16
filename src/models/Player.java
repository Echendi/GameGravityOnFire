package models;

import java.awt.Point;

public class Player {

	public static final int X_MOVE_SIZE = 5;
	public static final int Y_MOVE_SIZE = 5;
	public static final int WIDTH = 25;
	public static final int HEIGTH = 50;
	public static final Point STARTING_POSITION = new Point(Game.MAP_WIDTH / 3, Game.MAP_HIGTH / 2);
	private boolean isAlive;
	private Point position;
	private boolean isDown;

	public Player() {
		this.position = STARTING_POSITION;
		this.isDown = true;
		isAlive = true;
	}

	public void changeGravity() {
		isDown = !isDown;
	}

	public void moveVertically() {
		if ((isDown && position.y + HEIGTH + (Y_MOVE_SIZE * 9) < Game.MAP_HIGTH)
				|| (!isDown && position.y - X_MOVE_SIZE > 0)) {
			position.y += isDown ? Y_MOVE_SIZE : Y_MOVE_SIZE * (-1);
		} else {
			isAlive = false;
		}
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public Point getPosition() {
		return position;
	}
}