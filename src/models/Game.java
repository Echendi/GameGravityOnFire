package models;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Game extends GameThread implements IGame {

	private static final int CHANGE_DIFFICULTY_TIME = 15000;
	public static final int MAP_WIDTH = 900;
	public static final int MAP_HEIGTH = 600;
	public static final int INITIAL_MAX_PLATFORMS = 10;
	private static final long INITIAL_VELOCITY = 5;
	private static final int FLOOR_Y_POSITION = MAP_HEIGTH - 90;
	private static final int CEILLING_Y_POSITION = 30;
	private static final int FIRE_HEIGTH = MAP_HEIGTH;
	private static final int FIRE_WIDTH = MAP_WIDTH / 10;
	private static final int PLATFORM_COLLISION = 0;
	private static final int PLATFORM_FORNT_COLLISION = 1;
	private static final int FLOOR_COLLISION = 2;
	private static final int FLOOR_FRONT_COLLISION = 3;
	private static final int CEILLING_COLLISION = 4;
	private static final int CEILLING_FRONT_COLLISION = 5;
	private static final Random randomGenerator = new Random();

	// Variables del juego
	@JsonIgnore
	private long lapseOfTime;
	private int maxPlatforms;
	private int[] objectCollision;

	// Objetos del juego
	private Player player;
	private ArrayList<Platform> platforms;
	private ArrayList<Platform> floor;
	private ArrayList<Platform> ceilling;
	private Trap fire;
	private Trap abyss[];
	private Chronometer chronometer;

	public Game() {
		super(INITIAL_VELOCITY);
		initGame();
	}

	private void initGame() {
		chronometer = new Chronometer();
		if (!chronometer.isExecute) {
			initRun();
		}
		maxPlatforms = INITIAL_MAX_PLATFORMS;
		player = new Player();
		platforms = new ArrayList<>();
		floor = new ArrayList<>();
		ceilling = new ArrayList<>();
		createAbyss();
		fire = new Trap(0, 0, FIRE_WIDTH, FIRE_HEIGTH);
		objectCollision = new int[6];
		objectCollision[0] = -1; // platformCollision;
		objectCollision[1] = -1; // platformForntCollision
		objectCollision[2] = -1; // floorCollision
		objectCollision[3] = -1; // floorFrontCollision
		objectCollision[4] = -1; // ceillingCollision
		objectCollision[5] = -1; // ceillingFrontCollision
	}

	private void createAbyss() {
		abyss = new Trap[2];
		abyss[0] = new Trap(0, 0, MAP_WIDTH, CEILLING_Y_POSITION);
		abyss[1] = new Trap(0, FLOOR_Y_POSITION + (Platform.HEIGTH), MAP_WIDTH,
				MAP_HEIGTH - FLOOR_Y_POSITION + Platform.HEIGTH);
	}

	@Override
	public void executeTask() {
		player.move();
		movePlatformObjects();
		checkCollisions();
		increaseDifficulty();
	}

	@Override
	public synchronized void resume() {
		chronometer.resume();
		super.resume();
	}

	@Override
	public synchronized void pause() {
		chronometer.pause();
		super.pause();
	}

	private void initRun() {
		chronometer.start();
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
			if (player.checkCollision(trap)) {
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
		isExecute = false;
		chronometer.stop();
	}

	private void movePlatformObjects() {
		moveFloor();
		moveCeilling();
		movePlataforms();
	}

	private void checkCeillingCollisions() {
		searchCollisons(CEILLING_COLLISION, ceilling);
		searchFrontCollisions(CEILLING_FRONT_COLLISION, ceilling);
		verifyCollision(CEILLING_COLLISION, ceilling);
		verifyFrontCollision(CEILLING_FRONT_COLLISION, ceilling);
	}

	private void moveCeilling() {
		if (ceilling.size() > 0) {
			Platform lastCeilling = ceilling.get(ceilling.size() - 1);
			if (lastCeilling.x + lastCeilling.getWidth() < Game.MAP_WIDTH - 100) {
				generateCeillingPlatform();
			}
		} else {
			generateCeillingPlatform();
		}
		ceilling.removeIf(Platform::move);
	}

	private void generateCeillingPlatform() {
		ceilling.add(new Platform(Game.MAP_WIDTH, CEILLING_Y_POSITION,
				randomGenerator.nextInt(Game.MAP_WIDTH) + Platform.MIN_WIDTH));
	}

	private void checkFloorCollisions() {
		searchCollisons(FLOOR_COLLISION, floor);
		searchFrontCollisions(FLOOR_FRONT_COLLISION, floor);
		verifyCollision(FLOOR_COLLISION, floor);
		verifyFrontCollision(FLOOR_FRONT_COLLISION, floor);
	}

	private void moveFloor() {
		if (floor.size() > 0) {
			Platform lastFloor = floor.get(floor.size() - 1);
			if (lastFloor.x + lastFloor.getWidth() < Game.MAP_WIDTH - 100) {
				generateFloorPlatform();
			}
		} else {
			generateFloorPlatform();
		}
		floor.removeIf(Platform::move);
	}

	private void generateFloorPlatform() {
		floor.add(new Platform(Game.MAP_WIDTH, FLOOR_Y_POSITION,
				randomGenerator.nextInt(Game.MAP_WIDTH) + Platform.MIN_WIDTH));
	}

	private void checkPlatformsCollisions() {
		searchCollisons(PLATFORM_COLLISION, platforms);
		searchFrontCollisions(PLATFORM_FORNT_COLLISION, platforms);
		verifyCollision(PLATFORM_COLLISION, platforms);
		verifyFrontCollision(PLATFORM_FORNT_COLLISION, platforms);
	}

	private void verifyFrontCollision(int colliderIndex, ArrayList<Platform> colliders) {
		try {
			if (!player.checkFrontCollision(platforms.get(objectCollision[1]))) {
				player.setFrontColliding(false);
				objectCollision[1] = -1;
			}
		} catch (IndexOutOfBoundsException e) {
		}
	}

	private void searchFrontCollisions(int colliderIndex, ArrayList<Platform> colliders) {
		for (int i = 0; i < platforms.size(); i++) {
			if (player.checkFrontCollision(platforms.get(i))) {
				objectCollision[1] = i;
			}
		}
	}

	private void verifyCollision(int colliderIndex, ArrayList<Platform> colliders) {
		try {
			if (!player.checkCollision(colliders.get(objectCollision[colliderIndex]))) {
				player.setColliding(false);
				objectCollision[colliderIndex] = -1;
			}
		} catch (IndexOutOfBoundsException e) {
		}
	}

	private void searchCollisons(int colliderIndex, ArrayList<Platform> colliders) {
		for (int i = 0; i < colliders.size(); i++) {
			if (player.checkCollision(colliders.get(i))) {
				objectCollision[colliderIndex] = i;
			}
		}
	}

	private void movePlataforms() {
		if (platforms.size() < maxPlatforms) {
			createPlatform();
		}
		platforms.removeIf(Platform::move);
	}

	private void createPlatform() {
		Point point = generatePoint();
		platforms.add(new Platform(point.x, point.y, generatePlatformWidth()));
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

	public boolean isExecute() {
		return isExecute;
	}

	public void increaseDifficulty() {
		if (System.currentTimeMillis() - lapseOfTime > CHANGE_DIFFICULTY_TIME) {
			sleepTime -= sleepTime > 1 ? 1 : 0;
			maxPlatforms++;
			lapseOfTime = System.currentTimeMillis();
		}
	}

	public int getMaxPlatforms() {
		return maxPlatforms;
	}

	public int getPlatformCollision() {
		return objectCollision[0];
	}

	public int getPlatformForntCollision() {
		return objectCollision[1];
	}

	public int getFloorCollision() {
		return objectCollision[2];
	}

	public int getFloorFrontCollision() {
		return objectCollision[3];
	}

	public int getCeillingCollision() {
		return objectCollision[4];
	}

	public int getCeillingFrontCollision() {
		return objectCollision[5];
	}

	@Override
	public Platform[] getPlatforms() {
		return platforms.toArray(new Platform[0]);
	}

	@Override
	@JsonIgnore
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

	@JsonIgnore
	@Override
	public int[] getTime() {
		return chronometer.getTime();
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

	@Override
	public boolean isDown() {
		return player.isDown();
	}

	@Override
	public boolean isPause() {
		return isPaused;
	}
}
