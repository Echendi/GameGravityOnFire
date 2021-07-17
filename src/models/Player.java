package models;

import java.awt.Point;
import java.awt.Rectangle;

public class Player {

	public static final int X_MIN = Game.MAP_WIDTH / 10;
	public static final int X_MOVE_SIZE = 1;
	public static final int Y_MOVE_SIZE = 4;
	public static final int WIDTH = 25;
	public static final int HEIGTH = 50;
	public static final Point STARTING_POSITION = new Point(Game.MAP_WIDTH / 2, Game.MAP_HIGTH / 2);
	private boolean isColliding;
	private boolean isFrontColliding;
//	private boolean isAlive;
	private Point position;
	private boolean isDown;

	public Player() {
		this.position = new Point(STARTING_POSITION);
		this.isDown = true;
//		isAlive = true;
		isColliding = false;
		isFrontColliding = false;
	}

	public void changeGravity() {
		isDown = !isDown;
		position.y += isDown ? Y_MOVE_SIZE + HEIGTH / 2 : (Y_MOVE_SIZE + HEIGTH / 2) * (-1);
	}

	public void move() {
		moveY();
		moveX();
	}

	private void moveX() {
		if (position.x < STARTING_POSITION.x && !isFrontColliding) {
			position.x += X_MOVE_SIZE;
		}
		if (isFrontColliding && position.x > X_MIN) {
			position.x -= Platform.MOVE_SIZE;
		}
	}

	private void moveY() {
		if (checkLimits() && ((!isColliding && isFrontColliding) || (!isFrontColliding && !isColliding))) {
			position.y += isDown ? Y_MOVE_SIZE : Y_MOVE_SIZE * (-1);
		}
	}

	private boolean checkLimits() {
		return (isDown && position.y + HEIGTH + (Y_MOVE_SIZE * 9) < Game.MAP_HIGTH)
				|| (!isDown && position.y - X_MOVE_SIZE > 0);
	}

	public boolean checkCollision(Collider collider) {
		Rectangle playerRec = new Rectangle(position.x, position.y, WIDTH, HEIGTH);
		boolean isCollision = playerRec.intersects(new Rectangle(collider.getPosition().x, collider.getPosition().y,
				collider.getWidth(), collider.getHeigth()));
		return isCollision ? isColliding = true : isCollision;
	}

	public boolean checkFrontCollision(Collider collider) {
		Rectangle playerRec = new Rectangle(position.x, position.y, WIDTH, HEIGTH);
		boolean isCollision = playerRec.intersects(
				new Rectangle(collider.getPosition().x - 2, collider.getPosition().y, 2, collider.getHeigth()))
				&& !checkCollision(collider);
		return isCollision ? isFrontColliding = true : isCollision;
	}

	public void setColliding(boolean isColliding) {
		this.isColliding = isColliding;
	}

	public void setFrontColliding(boolean isFrontColliding) {
		this.isFrontColliding = isFrontColliding;
	}

//	public boolean isAlive() {
//		return isAlive;
//	}
//
//	public void setAlive(boolean isAlive) {
//		this.isAlive = isAlive;
//	}

	public Point getPosition() {
		return position;
	}
}