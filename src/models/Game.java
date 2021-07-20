package models;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Game extends Thread implements IGame {

	private static final int CHANGE_DIFFICULTY_TIME = 20000;
	public static final int MAP_WIDTH = 900;
	public static final int MAP_HEIGTH = 600;
	public static final int INITIAL_MAX_PLATFORMS = 10;
	private static final int INITIAL_VELOCITY = 5;
	private static final int FLOOR_Y_POSITION = MAP_HEIGTH - 90;
	private static final int CEILLING_Y_POSITION = 30;
	private static final int FIRE_HEIGTH = MAP_HEIGTH;
	private static final int FIRE_WIDTH = MAP_WIDTH / 10;
	private static final Random randomGenerator = new Random();

	// Variables del juego
	private static boolean play;
	private int maxPlatforms;
	private int velocity;
	private long lapseOfTime;

	// Objetos del juego
	private Player player;
	private ArrayList<Platform> platforms;
	private ArrayList<Platform> floor;
	private ArrayList<Platform> ceilling;
	private Chronometer chronometer;
	private Trap fire;
	private Trap abyss[];

	// Detecci�n de colisiones
	private int platformCollision;
	private int platformForntCollision;
	private int floorCollision;
	private int floorFrontCollision;
	private int ceillingCollision;
	private int ceillingFrontCollision;

	public Game() {
		initGame();
	}

	private void initGame() {
		velocity = INITIAL_VELOCITY;
		maxPlatforms = INITIAL_MAX_PLATFORMS;
		chronometer = new Chronometer();
		chronometer.start();
		player = new Player();
		platforms = new ArrayList<>();
		floor = new ArrayList<>();
		ceilling = new ArrayList<>();
		createAbyss();
		fire = new Trap(new Point(0, 0), FIRE_WIDTH, FIRE_HEIGTH);
		play = true;
		platformCollision = -1;
		platformForntCollision = -1;
		floorCollision = -1;
		floorFrontCollision = -1;
		start();

	}

	private void createAbyss() {
		abyss = new Trap[2];
		abyss[0] = new Trap(new Point(0, 0), MAP_WIDTH, CEILLING_Y_POSITION+(Platform.HIGTH/2));
		abyss[1] = new Trap(new Point(0, FLOOR_Y_POSITION +(Platform.HIGTH/2)), MAP_WIDTH,
				MAP_HEIGTH - FLOOR_Y_POSITION + Platform.HIGTH);
	}

	@Override
	public void run() {
		initRun();
		while (play) {
			player.move();
			movePlatformObjects();
			checkCollisions();
			sleeping();
			increaseDifficulty();
		}
	}

	private void initRun() {
		lapseOfTime = System.currentTimeMillis();
	}

	private void checkCollisions() {
		checkPlatformsCollisions();
		checkFloorCollisions();
		checkCeillingCollisions();
		checkFireCollision();
		checkAbyssColissions();
	}

	private void checkAbyssColissions() {
		for (Trap trap : abyss) {
			if(player.checkCollision(trap)) {
				gameOver();
			}
		}

	}

	private void checkFireCollision() {
		if (player.checkCollision(fire)) {
			gameOver();
		}
	}

	private void gameOver() {
		play = false;
		chronometer.pause();
	}

	private void movePlatformObjects() {
		moveFloor();
		moveCeilling();
		movePlataforms();
	}

	private void checkCeillingCollisions() {
		searcchCeillingCollisions();
		searchCeillingFrontCollisions();
		verifyCeillingCollision();
		verifyCeillingFrontCollision();
	}

	private void searchCeillingFrontCollisions() {
		for (int i = 0; i < ceilling.size(); i++) {
			if (player.checkFrontCollision(ceilling.get(i))) {
				ceillingFrontCollision = i;
			}
		}
	}

	private void verifyCeillingFrontCollision() {
		try {
			if (!player.checkFrontCollision(ceilling.get(ceillingFrontCollision))) {
				player.setFrontColliding(false);
				ceillingFrontCollision = -1;
			}
		} catch (IndexOutOfBoundsException e) {
		}
	}

	private void verifyCeillingCollision() {
		try {
			if (!player.checkCollision(ceilling.get(ceillingCollision))) {
				player.setColliding(false);
				ceillingCollision = -1;
			}
		} catch (IndexOutOfBoundsException e) {
		}
	}

	private void searcchCeillingCollisions() {
		for (int i = 0; i < ceilling.size(); i++) {
			if (player.checkCollision(ceilling.get(i))) {
				ceillingCollision = i;
			}
		}
	}

	private void moveCeilling() {
		if (ceilling.size() > 0) {
			Platform lastCeilling = ceilling.get(ceilling.size() - 1);
			if (lastCeilling.position.x + lastCeilling.getWidth() < Game.MAP_WIDTH - 100) {
				generateCeillingPlatform();
			}
		} else {
			generateCeillingPlatform();
		}
		ceilling.removeIf(Platform::move);
	}

	private void generateCeillingPlatform() {
		ceilling.add(new Platform(new Point(Game.MAP_WIDTH, CEILLING_Y_POSITION),
				randomGenerator.nextInt(Game.MAP_WIDTH) + Platform.MIN_WIDTH));
	}

	private void checkFloorCollisions() {
		searcchFloorCollisions();
		searchFloorFrontCollisions();
		verifyFloorCollision();
		verifyFloorFrontCollision();
	}

	private void searchFloorFrontCollisions() {
		for (int i = 0; i < floor.size(); i++) {
			if (player.checkFrontCollision(floor.get(i))) {
				floorFrontCollision = i;
			}
		}
	}

	private void verifyFloorFrontCollision() {
		try {
			if (!player.checkFrontCollision(floor.get(floorFrontCollision))) {
				player.setFrontColliding(false);
				floorFrontCollision = -1;
			}
		} catch (IndexOutOfBoundsException e) {
		}
	}

	private void verifyFloorCollision() {
		try {
			if (!player.checkCollision(floor.get(floorCollision))) {
				player.setColliding(false);
				floorCollision = -1;
			}
		} catch (IndexOutOfBoundsException e) {
		}
	}

	private void searcchFloorCollisions() {
		for (int i = 0; i < floor.size(); i++) {
			if (player.checkCollision(floor.get(i))) {
				floorCollision = i;
			}
		}
	}

	private void moveFloor() {
		if (floor.size() > 0) {
			Platform lastFloor = floor.get(floor.size() - 1);
			if (lastFloor.position.x + lastFloor.getWidth() < Game.MAP_WIDTH - 100) {
				generateFloorPlatform();
			}
		} else {
			generateFloorPlatform();
		}
		floor.removeIf(Platform::move);
	}

	private void generateFloorPlatform() {
		floor.add(new Platform(new Point(Game.MAP_WIDTH, FLOOR_Y_POSITION),
				randomGenerator.nextInt(Game.MAP_WIDTH) + Platform.MIN_WIDTH));
	}

	private void checkPlatformsCollisions() {
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

	private void sleeping() {
		try {
			Thread.sleep(velocity);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void movePlataforms() {
		if (platforms.size() < maxPlatforms) {
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

	public static boolean isPlay() {
		return play;
	}

	public void increaseDifficulty() {
		if (System.currentTimeMillis() - lapseOfTime > CHANGE_DIFFICULTY_TIME) {
			velocity -= velocity > 1 ? 1 : 0;
			maxPlatforms++;
			initRun();
		}
	}

	@Override
	public Platform[] getPlatforms() {
		return platforms.toArray(new Platform[0]);
	}

	@Override
	public Point getPlayerPosition() {
		return player.getPosition();
	}

	@Override
	public Platform[] getFloor() {
		return floor.toArray(new Platform[0]);
	}

	@Override
	public Platform[] getCeilling() {
		return ceilling.toArray(new Platform[0]);
	}

	@Override
	public int[] getTime() {
		return new int[] { chronometer.getHours(), chronometer.getMinuts(), chronometer.getSeconds(),
				chronometer.getMillis() };
	}

	@Override
	public Trap getFire() {
		return fire;
	}

	@Override
	public Trap[] getAbyss() {
		Trap[] copyAbyss = new Trap[2];
		System.arraycopy(abyss, 0, copyAbyss, 0, abyss.length);
		return copyAbyss;
	}
}
