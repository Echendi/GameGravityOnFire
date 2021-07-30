package models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Platform extends Collider {
	public static final int HEIGTH = 25;
	public static final int MAX_WIDTH = 250;
	public static final int MIN_WIDTH = 50;
	public static final int MAX_Y_POSITION = Game.MAP_HEIGTH - HEIGTH - 75;
	public static final int MIN_Y_POSITION = 75;
	public static int moveSize = 1;

	public static void setMoveSize(int moveSize) {
		Platform.moveSize = moveSize;
	}

	public Platform(int x, int y, int width, int heigth) {
		super(x, y, width, heigth);
	}

	public boolean move() {
		x -= moveSize;
		return x + width - moveSize < 0;
	}
}
