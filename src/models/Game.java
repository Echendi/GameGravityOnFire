package models;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;

import persistence.FileManager;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Game extends GameThread implements IGame {

	private static final int SECONDS_PER_COIN = 2;
	public static final int MAP_WIDTH = 900;
	public static final int MAP_HEIGTH = 600;
	public static final int INITIAL_MAX_PLATFORMS = 10;
	private static final int NO_COLLISION = -1;
	private static final int CHANGE_DIFFICULTY_TIME = 15000;
	private static final long INITIAL_VELOCITY = 5;
	private static final int FLOOR_Y_POSITION = MAP_HEIGTH - 55;
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

	@JsonIgnore
	private long lapseOfTime;
	private int maxPlatforms;
	private int[] objectCollisionIndex;
	private Player player;
	private GameData data;
	private ArrayList<Platform> platforms;
	private ArrayList<Platform> floor;
	private ArrayList<Platform> ceilling;
	private Trap fire;
	private Trap abyss[];
	private Chronometer chronometer;

	public Game(GameData data) {
		super(INITIAL_VELOCITY);
		chronometer = new Chronometer();
		this.data = data;
	}

	@Override
	public void executeTask() {
		player.move();
		movePlatformObjects();
		checkCollisions();
		increaseDifficulty();
	}

	private void initGame() {
		maxPlatforms = INITIAL_MAX_PLATFORMS;
		player = new Player();
		isExecute = true;
		fire = new Trap(0, 0, FIRE_WIDTH, FIRE_HEIGTH);
		initCollidersPlatforms();
		createAbyss();
		initObjectCollisionsIndex();
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

	@Override
	public void start() {
		initGame();
		chronometer.resetTime();
		initChronometer();
		super.start();
	}

	private void gameOver() {
		isExecute = false;
		chronometer.stop();
		data.addCoins(calculateCoins());
		data.addScore(chronometer.getTime());
		FileManager.saveScores(data);
	}

	private int calculateCoins() {
		return chronometer.getTimeInSeconds() / SECONDS_PER_COIN;
	}

	private void initChronometer() {
		if (!chronometer.isExecute) {
			initRun();
		}
	}

	private void initCollidersPlatforms() {
		platforms = new ArrayList<>();
		floor = new ArrayList<>();
		ceilling = new ArrayList<>();
	}

	private void initObjectCollisionsIndex() {
		objectCollisionIndex = new int[6];
		objectCollisionIndex[PLATFORM_COLLISION] = NO_COLLISION;
		objectCollisionIndex[PLATFORM_FORNT_COLLISION] = NO_COLLISION;
		objectCollisionIndex[FLOOR_COLLISION] = NO_COLLISION;
		objectCollisionIndex[FLOOR_FRONT_COLLISION] = NO_COLLISION;
		objectCollisionIndex[CEILLING_COLLISION] = NO_COLLISION;
		objectCollisionIndex[CEILLING_FRONT_COLLISION] = NO_COLLISION;
	}

	private void initRun() {
		chronometer.start();
		lapseOfTime = System.currentTimeMillis();
	}

	private void createAbyss() {
		abyss = new Trap[2];
		abyss[0] = new Trap(0, 0, MAP_WIDTH, CEILLING_Y_POSITION);
		abyss[1] = new Trap(0, FLOOR_Y_POSITION + (Platform.HEIGTH), MAP_WIDTH,
				MAP_HEIGTH - FLOOR_Y_POSITION + Platform.HEIGTH);
	}

	public void changeGravity() {
		player.changeGravity();
	}

	public void increaseDifficulty() {
		if (System.currentTimeMillis() - lapseOfTime > CHANGE_DIFFICULTY_TIME) {
			sleepTime -= sleepTime > 1 ? 1 : 0;
			maxPlatforms++;
			lapseOfTime = System.currentTimeMillis();
		}
	}

	private void checkCollisions() {
		checkCollisions(PLATFORM_COLLISION, PLATFORM_FORNT_COLLISION, platforms);
		checkCollisions(FLOOR_COLLISION, FLOOR_FRONT_COLLISION, floor);
		checkCollisions(CEILLING_COLLISION, CEILLING_FRONT_COLLISION, ceilling);
		checkFireCollision();
		checkAbyssColissions();
	}

	private void checkCollisions(int collisionIndex, int frontCollisionIndex, ArrayList<Platform> colliders) {
		searchCollisons(collisionIndex, colliders);
		searchFrontCollisions(frontCollisionIndex, colliders);
		verifyCollision(collisionIndex, colliders);
		verifyFrontCollision(frontCollisionIndex, colliders);
	}

	private void verifyFrontCollision(int colliderIndex, ArrayList<Platform> colliders) {
		try {
			if (!player.checkFrontCollision(colliders.get(objectCollisionIndex[colliderIndex]))) {
				player.setFrontColliding(false);
				objectCollisionIndex[colliderIndex] = NO_COLLISION;
			}
		} catch (IndexOutOfBoundsException e) {
		}
	}

	private void searchFrontCollisions(int colliderIndex, ArrayList<Platform> colliders) {
		for (int i = 0; i < colliders.size(); i++) {
			if (player.checkFrontCollision(colliders.get(i))) {
				objectCollisionIndex[colliderIndex] = i;
			}
		}
	}

	private void verifyCollision(int colliderIndex, ArrayList<Platform> colliders) {
		try {
			if (!player.checkCollision(colliders.get(objectCollisionIndex[colliderIndex]))) {
				player.setColliding(false);
				objectCollisionIndex[colliderIndex] = NO_COLLISION;
			}
		} catch (IndexOutOfBoundsException e) {
		}
	}

	private void searchCollisons(int colliderIndex, ArrayList<Platform> colliders) {
		for (int i = 0; i < colliders.size(); i++) {
			if (player.checkCollision(colliders.get(i))) {
				objectCollisionIndex[colliderIndex] = i;
			}
		}
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

	private void movePlatformObjects() {
		moveFloor();
		moveCeilling();
		movePlataforms();
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

	private void movePlataforms() {
		if (platforms.size() < maxPlatforms) {
			generatePlatform();
		}
		platforms.removeIf(Platform::move);
	}

	private void generateCeillingPlatform() {
		ceilling.add(new Platform(Game.MAP_WIDTH, CEILLING_Y_POSITION,
				randomGenerator.nextInt(Game.MAP_WIDTH) + Platform.MIN_WIDTH));
	}

	private void generateFloorPlatform() {
		floor.add(new Platform(Game.MAP_WIDTH, FLOOR_Y_POSITION,
				randomGenerator.nextInt(Game.MAP_WIDTH) + Platform.MIN_WIDTH));
	}

	private void generatePlatform() {
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

	public boolean isExecute() {
		return isExecute;
	}

	@JsonIgnore
	@Override
	public boolean isDown() {
		return player.isDown();
	}

	@JsonIgnore
	@Override
	public boolean isPause() {
		return isPaused;
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

	@JsonIgnore
	@Override
	public int[] getBestScore() {
		return data.getBestScore();
	}

	@JsonIgnore
	@Override
	public int getCoins() {
		return data.getCoins();
	}

	@JsonIgnore
	@Override
	public int getParcialCoins() {
		return calculateCoins();
	}

	@Override
	public int getActualSkin() {
		return data.getActualSkin();
	}
}