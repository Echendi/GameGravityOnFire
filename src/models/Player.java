package models;

import java.awt.Point;
import java.awt.Rectangle;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Player {

	public static final int X_MIN = 10;
	public static final int X_MOVE_SIZE = 1;
	public static final int Y_MOVE_SIZE = 1;
	public static final int WIDTH = 35;
	public static final int HEIGTH = 50;
	public static final Point STARTING_POSITION = new Point(Game.MAP_WIDTH / 2, 50);
	private boolean isColliding;
	private boolean isFrontColliding;
	private boolean isDown;
	private int x;
	private int y;

	public Player() {
		x = STARTING_POSITION.x;
		y = STARTING_POSITION.y;
		this.isDown = true;
		isColliding = false;
		isFrontColliding = false;
	}

	public Player(Point position) {
		x = position.x;
		y = position.y;
		this.isDown = true;
		isColliding = false;
		isFrontColliding = false;
	}

	public void changeGravity() {
		isDown = !isDown;
		y += isDown ? Y_MOVE_SIZE : Y_MOVE_SIZE * (-1);
	}

	public void move() {
		moveY();
		moveX();
	}

	private void moveX() {
		if (x < STARTING_POSITION.x && !isFrontColliding) {
			x += X_MOVE_SIZE;
		}
		if (isFrontColliding && x > X_MIN) {
			x -= Platform.moveSize;
		}
	}

	private void moveY() {
		if (checkLimits() && ((!isColliding && isFrontColliding) || (!isFrontColliding && !isColliding))) {
			y += isDown ? Y_MOVE_SIZE : Y_MOVE_SIZE * (-1);
		}
	}

	private boolean checkLimits() {
		return ((isDown && (y + HEIGTH + Y_MOVE_SIZE ) < Game.MAP_HEIGTH)) || (!isDown && y - Y_MOVE_SIZE > 0);
	}

	public boolean checkCollision(Collider collider) {
		Rectangle playerRec = new Rectangle(x, y, WIDTH, HEIGTH);
		boolean isCollision = playerRec.intersects(new Rectangle(collider.getPosition().x, collider.getPosition().y,
				collider.getWidth(), collider.getHeight()));
		return isCollision ? isColliding = true : isCollision;
	}

	public boolean checkFrontCollision(Collider collider) {
		Rectangle playerRec = new Rectangle(x, y, WIDTH, HEIGTH);
		boolean isCollision = playerRec.intersects(
				new Rectangle(collider.getPosition().x - 2, collider.getPosition().y, 2, collider.getHeight()))
				&& !checkCollision(collider);
		return isCollision ? isFrontColliding = true : isCollision;
	}

	public void setColliding(boolean isColliding) {
		this.isColliding = isColliding;
	}

	public void setFrontColliding(boolean isFrontColliding) {
		this.isFrontColliding = isFrontColliding;
	}

	@JsonIgnore
	public Point getPosition() {
		return new Point(x, y);
	}

	@JsonIgnore
	public boolean isColliding() {
		return isColliding;
	}

	@JsonIgnore
	public boolean isDown() {
		return isDown;
	}
}