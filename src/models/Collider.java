package models;

import java.awt.Point;

public abstract class Collider {
	protected Point position;
	protected int width;
	protected int heigth;

	public Collider(Point position, int width, int heigth) {
		this.position = position;
		this.width = width;
		this.heigth = heigth;
	}

	public int getHeigth() {
		return heigth;
	}

	public Point getPosition() {
		return position;
	}

	public int getWidth() {
		return width;
	}
}
