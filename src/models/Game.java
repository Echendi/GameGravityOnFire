package models;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Game extends Thread implements IGame {

	public static final int MAP_WIDTH = 900;
	public static final int MAP_HIGTH = 600;
	public static final int MAX_PLATFORMS = 7;
	private static final int PLAYER_VELOCITY = 8;
	private static final Random randomGenerator = new Random();

	private Player player;
	private ArrayList<Platform> platforms;
	private boolean play;
	int platformCollision;
	int platformForntCollision;

	public Game() {
		player = new Player();
		platforms = new ArrayList<>();
		play = true;
		platformCollision = -1;
		platformForntCollision = -1;
		start();
	}

	private void checkCollisions() {
		searchCollisons();
		searchFrontCollisions();
		verifyCollision();
		verifyFrontCollision();
	}

	private void verifyFrontCollision() {
		try {
			if (!player.checkFrontCollision(platforms.get(platformForntCollision))) {
				player.setFrontColliding(false);
				platformForntCollision = -1;
			}
		} catch (IndexOutOfBoundsException e) {
		}
	}

	private void searchFrontCollisions() {
		for (int i = 0; i < platforms.size(); i++) {
			if (player.checkFrontCollision(platforms.get(i))) {
				platformForntCollision = i;
			}
		}
	}

	private void verifyCollision() {
		try {
			if (!player.checkCollision(platforms.get(platformCollision))) {
				player.setColliding(false);
				platformCollision = -1;
			}
		} catch (IndexOutOfBoundsException e) {
		}
	}

	private void searchCollisons() {
		for (int i = 0; i < platforms.size(); i++) {
			if (player.checkCollision(platforms.get(i))) {
				platformCollision = i;
			}
		}
	}

	@Override
	public void run() {
		while (play) {
			player.move();
			movePlataforms();
			checkCollisions();
			sleeping();
		}
	}

	private void sleeping() {
		try {
			Thread.sleep(PLAYER_VELOCITY);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void movePlataforms() {
		if (platforms.size() < MAX_PLATFORMS) {
			createPlatform();
		}
		platforms.removeIf(Platform::move);
	}

	private void createPlatform() {
		platforms.add(new Platform(generatePoint(), generatePlatformWidth()));
	}

	private int generatePlatformWidth() {
		return randomGenerator.nextInt(Platform.MAX_WIDTH - Platform.MIN_WIDTH) + Platform.MIN_WIDTH;
	}

	private Point generatePoint() {
		int y = randomGenerator.nextInt(Platform.MAX_Y_POSITION - Platform.MIN_Y_POSITION) + Platform.MIN_Y_POSITION;
		int x = Game.MAP_WIDTH + randomGenerator.nextInt(Game.MAP_WIDTH);
		return new Point(x, y);
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
