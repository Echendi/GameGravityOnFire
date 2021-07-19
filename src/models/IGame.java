package models;

import java.awt.Point;

public interface IGame {

	public Point getPlayerPosition();

	public Platform[] getPlatforms();

	public Platform[] getFloor();

	public Platform[] getCeilling();

	public int[] getTime();

}