package models;

import java.awt.Point;

public interface IGame {

	public Point getPlayerPosition();

	public Platform[] getPlatforms();

	public Platform[] getFloor();

	public Platform[] getCeilling();

	public Trap getFire();

	public int[] getTime();

	public Trap[] getAbyss();

	public boolean isDown();

	public boolean isPause();

	public boolean isExecute();

}
