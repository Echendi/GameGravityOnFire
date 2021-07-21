package models;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIgnore;

@JsonAutoDetect(fieldVisibility = Visibility.ANY)
public class Game extends Thread implements IGame {

	private static final int CHANGE_DIFFICULTY_TIME = 15000;
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
	private boolean play;
	private int maxPlatforms;
	private int velocity;

	@JsonIgnore
	private long lapseOfTime;

	// Objetos del juego
	private Player player;
	private ArrayList<Platform> platforms;
	private ArrayList<Platform> floor;
	private ArrayList<Platform> ceilling;
	private Trap fire;
	private Trap abyss[];
	private Chronometer chronometer;

	// Detección de colisiones
	private int platformCollision;
	private int platformForntCollision;
	private int floorCollision;
	private int floorFrontCollision;
	private int ceillingCollision;
	private int ceillingFrontCollision;

	public Game() {
		initGame();
	}

//	public Game(boolean play, int maxPlatforms, int velocity, long lapseOfTime, Player player,
//			ArrayList<Platform> platforms, ArrayList<Platform> floor, ArrayList<Platform> ceilling, Trap fire,
//			Trap[] abyss, Chronometer chronometer, int platformCollision, int platformForntCollision,
//			int floorCollision, int floorFrontCollision, int ceillingCollision, int ceillingFrontCollision) {
//		super();
//		this.play = play;
//		this.maxPlatforms = maxPlatforms;
//		this.velocity = velocity;
//		this.lapseOfTime = lapseOfTime;
//		this.player = player;
//		this.platforms = platforms;
////		for (Platform platform : platforms) {
////			this.platforms.add(platform);
////		}
//		this.floor = floor;
//		this.ceilling = ceilling;
//		this.fire = fire;
//		this.abyss = abyss;
//		this.chronometer = chronometer;
//		this.platformCollision = platformCollision;
//		this.platformForntCollision = platformForntCollision;
//		this.floorCollision = floorCollision;
//		this.floorFrontCollision = floorFrontCollision;
//		this.ceillingCollision = ceillingCollision;
//		this.ceillingFrontCollision = ceillingFrontCollision;
//	}

	private void initGame() {
		velocity = INITIAL_VELOCITY;
		maxPlatforms = INITIAL_MAX_PLATFORMS;
		chronometer = new Chronometer();
		player = new Player();
		platforms = new ArrayList<>();
		floor = new ArrayList<>();
		ceilling = new ArrayList<>();
		createAbyss();
		fire = new Trap(0, 0, FIRE_WIDTH, FIRE_HEIGTH);
		play = true;
		platformCollision = -1;
		platformForntCollision = -1;
		floorCollision = -1;
		floorFrontCollision = -1;
		ceillingCollision = -1;
		ceillingFrontCollision = -1;
	}

	private void createAbyss() {
		abyss = new Trap[2];
		abyss[0] = new Trap(0, 0, MAP_WIDTH, CEILLING_Y_POSITION);
		abyss[1] = new Trap(0, FLOOR_Y_POSITION + (Platform.HEIGTH), MAP_WIDTH,
				MAP_HEIGTH - FLOOR_Y_POSITION + Platform.HEIGTH);
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
//		synchronized (this) {
//			FileManager.saveGame(this);
////			FileManager.loadGame();
//		}
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

	public boolean isPlay() {
		return play;
	}

	public void increaseDifficulty() {
		if (System.currentTimeMillis() - lapseOfTime > CHANGE_DIFFICULTY_TIME) {
			velocity -= velocity > 1 ? 1 : 0;
			maxPlatforms++;
			lapseOfTime = System.currentTimeMillis();
		}
	}

	public int getMaxPlatforms() {
		return maxPlatforms;
	}

	public int getVelocity() {
		return velocity;
	}

	public int getPlatformCollision() {
		return platformCollision;
	}

	public int getPlatformForntCollision() {
		return platformForntCollision;
	}

	public int getFloorCollision() {
		return floorCollision;
	}

	public int getFloorFrontCollision() {
		return floorFrontCollision;
	}

	public int getCeillingCollision() {
		return ceillingCollision;
	}

	public int getCeillingFrontCollision() {
		return ceillingFrontCollision;
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
}
