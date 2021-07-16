package models;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Game extends Thread implements IGame {

	public static final int MAP_WIDTH = 900;
	public static final int MAP_HIGTH = 600;
	public static final int MAX_PLATFORMS = 7;
	private static final int PLATFORM_VELOCITY = 25;
	private static final int PLAYER_VELOCITY = 15;
	private static final Random randomGenerator = new Random();

	private Player player;
	private ArrayList<Platform> platforms;
	private Thread platformsThread;
	private boolean play;

	public Game() {
		player = new Player();
		platforms = new ArrayList<>();
		play = true;
		start();
		createPlatformsThread();
	}

	private void createPlatformsThread() {
		platformsThread = new Thread(() -> {

			while (play) {
				if (platforms.size() < MAX_PLATFORMS) {
					createPlatform();
				}
				platforms.removeIf(Platform::move);

				try {
					Thread.sleep(PLATFORM_VELOCITY);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		});
		platformsThread.start();
	}

	@Override
	public void run() {
		while (play) {
			player.moveVertically();
			try {
				Thread.sleep(PLAYER_VELOCITY);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void createPlatform() {
		platforms.add(new Platform(generatePoint(), generatePlatformWidth()));

	}

	private int generatePlatformWidth() {
		return randomGenerator.nextInt(Platform.MAX_WIDTH - Platform.MIN_WIDTH) + Platform.MIN_WIDTH;
	}

	private Point generatePoint() {
		int y = randomGenerator.nextInt(Platform.MAX_Y_POSITION - Platform.MIN_Y_POSITION) + Platform.MIN_Y_POSITION;
		return new Point(Game.MAP_WIDTH, y);
	}

	public void changeGravity() {
		player.changeGravity();
	}

	@Override
	public Platform[] getPlatforms() {
		return platforms.toArray(new Platform[0]);
	}

	@Override
	public Point getPlayerPosition() {
		return player.getPosition();
	}
}
