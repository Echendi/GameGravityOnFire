package models;

import java.awt.Point;

public class Game extends Thread implements IGame {
	private static final int PLAYER_VELOCITY = 25;
	public static final int MAP_WIDTH = 800;
	public static final int MAP_HIGTH = 700;
	public static final Point PLAYER_STARTING_POSITION = new Point(100, MAP_HIGTH / 2);

	private Player player;
	private boolean play;

	public Game() {
		player = new Player(PLAYER_STARTING_POSITION);
		play = true;
		start();
	}

	@Override
	public Point getPlayerPosition() {
		return player.getPosition();
	}

	@Override
	public void run() {
		while (play) {
			player.moveVertically();
			try {
				Thread.sleep(PLAYER_VELOCITY);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void changeGravity() {
		player.changeGravity();
	}

}
