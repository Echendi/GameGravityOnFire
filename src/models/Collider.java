package models;

import java.awt.Point;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Collider {
	protected int x;
	protected int y;
	protected int width;
	protected int heigth;

	public Collider(int x, int y, int width, int heigth) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.heigth = heigth;
	}

	public int getHeight() {
		return heigth;
	}

	@JsonIgnore
	public Point getPosition() {
		return new Point(x, y);
	}

	public int getWidth() {
		return width;
	}
}
