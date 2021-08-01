package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import buttons.Button;
import buttons.TypeButton;
import models.Game;
import models.IGame;
import models.Platform;
import models.Player;
import models.Trap;
import presenters.Command;

public class GamePanel extends JPanel {

	private static final String BUTTON_MUSIC = "/res/media/button.wav";
	private static final String GAME_OVER_MUSIC = "/res/media/gameOver.wav";
	private static final String GAME_MUSIC = "/res/media/gameMusic.wav";

	private static final String IMG_PLAYER_PNG = "/res/img/players.png";
	private static final String IMG_PLATFORM_2_PNG = "/res/img/platform2.png";
	private static final String IMG_PLATFORM_1_PNG = "/res/img/platform1.png";
	private static final String IMG_FIRE_PNG = "/res/img/fire.png";
	private static final String IMG_BACKGROUND_PNG = "/res/img/bg.png";
	private static final String IMG_SPACE_PNG = "/res/img/space.png";

	public static final String PLAY_TEXT = "\u00BB";
	public static final String PAUSED_TEXT = "ll";
	private static final Font BTN_PAUSE_FONT = new Font("Gill Sans Ultra Bold", Font.PLAIN, 15);
	private static final Dimension BTN_PAUSE_DIMENSION = new Dimension(40, 22);

	private static final Font BTN_OPTION_FONT = new Font("Gill Sans Ultra Bold", Font.PLAIN, 15);
	private static final Dimension BTN_OPTION_DIMENSION = new Dimension(120, 22);
	public static final String EXIT_TEXT = "SALIR";

	private static final Font LBL_FONT = new Font("Bell MT", Font.PLAIN, 20);
	private static final Color LBL_COLOR = Color.WHITE;
	private static final String TIME_FORMAT = "00:00:00:00";
	private static final String COINS_FORMAT = " $ ";

	private static final int IMG_CITY_WIDTH = 3700;
	private static final int Y_START_IMG_BG = 0;
	private static final int Y_END_IMG_BG = 900;

	public static final int PLAYERS_QUANTITY = 11;
	public static final int IMG_PLAYER_WIDTH = 30;
	public static final int IMG_PLAYER_HEIGTH = 44;
	private static final String MENU_TEXT = "MENU";
	private static final long serialVersionUID = 1L;
	private static final String SCREENSHOT_TEXT = "CAPTURAS";

	private BufferedImage gameScene;
	private BufferedImage screenshot;
	private BufferedImage imgFire;
	private BufferedImage[] fireSkins;
	private BufferedImage imgSpace;
	private BufferedImage imgCity;
	private BufferedImage skinCity;
	private BufferedImage platformSkin;
	private BufferedImage[] platformSkins;
	private BufferedImage imgPlayer;
	private BufferedImage[][] playerSkins;

	private Timer timer;
	private Clip clip;
	private JLabel lblTime;
	private JLabel lblCoins;
	private GameOverPanel panelGameOver;
	private Button btnMenu;
	private Button btnPause;
	private Button btnExit;
	private int actualSkin;
	private boolean isDown;
	private boolean isPaused;
	private Clip clipGameOver;
	private int count;
	private Button btnScreenshots;

	public GamePanel() {
		setLayout(new BorderLayout());
		initGameOverClip();
	}

	private void initGameOverClip() {
		try {
			clipGameOver = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public void initComponents(ActionListener listener) {
		isDown = true;
		isPaused = false;
		initSkins();
		initPanel(listener);
	}

	private void initPanel(ActionListener listener) {
		addInfoPanel(listener);
		panelGameOver = new GameOverPanel();
		panelGameOver.setVisible(false);
		add(panelGameOver, BorderLayout.CENTER);
		addOptionPanel(listener);
	}

	private void addInfoPanel(ActionListener listener) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);

		initLblTime(panel);
		initBtnPause(panel, listener);
		initLblCoins(panel);

		add(panel, BorderLayout.NORTH);
	}

	private void addOptionPanel(ActionListener listener) {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel.setOpaque(false);

		btnScreenshots = createOptionButton(TypeButton.PRIMARY, SCREENSHOT_TEXT, Command.SCREENSHOT, listener);
		btnScreenshots.setVisible(false);
		panel.add(btnScreenshots);

		btnMenu = createOptionButton(TypeButton.WARNING, MENU_TEXT, Command.MENU, listener);
		panel.add(btnMenu);

		btnExit = createOptionButton(TypeButton.DANGER, EXIT_TEXT, Command.EXIT, listener);
		panel.add(btnExit);

		add(panel, BorderLayout.SOUTH);
	}

	public void showBtnScreenshot(boolean isVisible) {
		btnScreenshots.setVisible(isVisible);
	}

	private Button createOptionButton(TypeButton type, String text, Command command, ActionListener listener) {
		Button btn = new Button(type, text);
		btn.setPreferredSize(BTN_OPTION_DIMENSION);
		btn.setOpaque(false);
		btn.setFocusable(false);
		btn.addActionListener(listener);
		btn.setActionCommand(command.toString());
		btn.setFont(BTN_OPTION_FONT);
		btn.setVisible(true);
		btn.addMouseListener(createMouseListener());
		return btn;
	}

	private MouseListener createMouseListener() {
		return new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				playButtonSound();
			}
		};
	}

	private void initBtnPause(JPanel panel, ActionListener listener) {
		btnPause = new Button(TypeButton.SUCCESS, PAUSED_TEXT);
		btnPause.setPreferredSize(BTN_PAUSE_DIMENSION);
		btnPause.setOpaque(false);
		btnPause.setFocusable(false);
		btnPause.addActionListener(listener);
		btnPause.setActionCommand(Command.PAUSE.toString());
		btnPause.setFont(BTN_PAUSE_FONT);
		btnPause.setVisible(true);
		btnPause.addMouseListener(createMouseListener());
		panel.add(btnPause);
	}

	private void initLblTime(JPanel panel) {
		lblTime = new JLabel(TIME_FORMAT);
		lblTime.setForeground(LBL_COLOR);
		lblTime.setFont(LBL_FONT);
		panel.add(lblTime);

	}

	private void initLblCoins(JPanel panel) {
		lblCoins = new JLabel(COINS_FORMAT);
		lblCoins.setForeground(LBL_COLOR);
		lblCoins.setFont(LBL_FONT);
		panel.add(lblCoins);
	}

	public void setVisibleLblGameOver(boolean value) {
		panelGameOver.setVisible(value);
	}

	private void initSkins() {
		try {
			initPlatformSkins();
			initFireSkins();
			initPlayerSkins();
			initBackGroundSkins();
			initTimer();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initBackGroundSkins() throws IOException {
		this.imgSpace = ImageIO.read(getClass().getResource(IMG_SPACE_PNG));
		this.imgCity = ImageIO.read(getClass().getResource(IMG_BACKGROUND_PNG));
	}

	private void initPlayerSkins() throws IOException {
		BufferedImage image = ImageIO.read(getClass().getResource(IMG_PLAYER_PNG));
		playerSkins = new BufferedImage[PLAYERS_QUANTITY][6];
		createPlayerSkins(image);
	}

	private void createPlayerSkins(BufferedImage image) {
		for (int i = 0; i < playerSkins.length; i++) {
			playerSkins[i][0] = image.getSubimage(0, IMG_PLAYER_HEIGTH * i, IMG_PLAYER_WIDTH, IMG_PLAYER_HEIGTH - 1);
			playerSkins[i][1] = image.getSubimage(IMG_PLAYER_WIDTH, IMG_PLAYER_HEIGTH * i, IMG_PLAYER_WIDTH - 1,
					IMG_PLAYER_HEIGTH - 1);
			playerSkins[i][2] = image.getSubimage(IMG_PLAYER_WIDTH * 2, IMG_PLAYER_HEIGTH * i, IMG_PLAYER_WIDTH,
					IMG_PLAYER_HEIGTH - 1);
			playerSkins[i][3] = image.getSubimage(IMG_PLAYER_WIDTH * 3 + 2, IMG_PLAYER_HEIGTH * i, IMG_PLAYER_WIDTH,
					IMG_PLAYER_HEIGTH - 1);
			playerSkins[i][4] = image.getSubimage(IMG_PLAYER_WIDTH * 4 + 2, IMG_PLAYER_HEIGTH * i, IMG_PLAYER_WIDTH - 1,
					IMG_PLAYER_HEIGTH - 1);
			playerSkins[i][5] = image.getSubimage(IMG_PLAYER_WIDTH * 5 + 3, IMG_PLAYER_HEIGTH * i, IMG_PLAYER_WIDTH,
					IMG_PLAYER_HEIGTH - 1);
		}
	}

	private void initFireSkins() throws IOException {
		BufferedImage image = ImageIO.read(getClass().getResource(IMG_FIRE_PNG));
		fireSkins = new BufferedImage[3];
		fireSkins[0] = image.getSubimage(0, 0, 385 / 3, 537);
		fireSkins[1] = image.getSubimage((385 / 3) + 5, 0, 385 / 3, 537);
		fireSkins[2] = image.getSubimage((385 / 3 * 2) + 10, 0, (385 / 3) - 10, 537);
	}

	private void initPlatformSkins() throws IOException {
		platformSkins = new BufferedImage[2];
		platformSkins[0] = ImageIO.read(getClass().getResource(IMG_PLATFORM_1_PNG));
		platformSkins[1] = ImageIO.read(getClass().getResource(IMG_PLATFORM_2_PNG));
	}

	private void initTimer() {
		timer = new Timer(100, new ActionListener() {
			int count = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!isPaused) {
					btnPause.setText(PAUSED_TEXT);
					changeFireSkin();
					changeCitySkin(count);
					changePlayerSkin();
					count = count + Game.MAP_WIDTH > IMG_CITY_WIDTH ? 0 : count + 1;
				} else {
					btnPause.setText(PLAY_TEXT);
				}
			}
		});
	}

	public void startMusic() {
		try {
			clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(GAME_MUSIC));
			clip.open(inputStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
		}
		timer.start();
	}

	private void changePlayerSkin() {
		if (isDown) {
			imgPlayer = imgPlayer == playerSkins[actualSkin][0] ? playerSkins[actualSkin][1]
					: imgPlayer == playerSkins[actualSkin][1] ? playerSkins[actualSkin][2] : playerSkins[actualSkin][0];
		} else {
			imgPlayer = imgPlayer == playerSkins[actualSkin][3] ? playerSkins[actualSkin][4]
					: imgPlayer == playerSkins[actualSkin][4] ? playerSkins[actualSkin][5] : playerSkins[actualSkin][3];
		}
	}

	private void changeCitySkin(int count) {
		skinCity = imgCity.getSubimage(count, Y_START_IMG_BG, Game.MAP_WIDTH, Y_END_IMG_BG);
	}

	private void changeFireSkin() {
		imgFire = imgFire == fireSkins[0] ? fireSkins[1] : imgFire == fireSkins[1] ? fireSkins[2] : fireSkins[0];
	}

	private void updateLblTime(int[] time) {
		lblTime.setText(formatTime(time));
	}

	private String formatTime(int[] time) {
		int hours = time[0];
		int minuts = time[1];
		int seconds = time[2];
		int millis = time[3];
		return (hours < 10 ? "0" : "") + hours + ":" + (minuts < 10 ? "0" : "") + minuts + ":"
				+ (seconds < 10 ? "0" : "") + seconds + ":" + (millis < 10 ? "0" : "") + millis;
	}

	public void refreshGame(IGame game) {
		verifyState(game);
		paintBackground();
		paintPlataforms(game.getPlatforms());
		paintFloor(game.getFloor());
		paintCeilling(game.getCeilling());
		paintFire(game.getFire());
		paintPlayer(game.getPlayerPosition());
		updateLblTime(game.getTime());
		updateLblCoins(game.getCoins());
		repaint();
		setScreenshot();
	}

	private void verifyState(IGame game) {
		isDown = game.isDown();
		isPaused = game.isPause();
		if (!game.isExecute()) {
			timer.stop();
			clip.stop();
			showBtnScreenshot(true);
			showGameOverPanel(game);
		}
	}

	private void showGameOverPanel(IGame game) {
		panelGameOver.setVisible(true);
		panelGameOver.updateScoreData(formatTime(game.getTime()), formatTime(game.getBestScore()),
				game.getParcialCoins());
		startGameOverMusic();
	}

	public void updateLblCoins(int coins) {
		lblCoins.setText(COINS_FORMAT + coins);
	}

	private void paintFire(Trap fire) {
		Graphics g = gameScene.getGraphics();
		g.drawImage(imgFire, fire.getPosition().x, fire.getPosition().y, fire.getWidth() + (Player.WIDTH * 2),
				fire.getHeigth(), this);

	}

	private void paintFloor(Platform[] floor) {
		Graphics g = gameScene.getGraphics();
		platformSkin = platformSkins[1];
		drawPlatformsObjects(g, floor);
	}

	private void paintCeilling(Platform[] ceilling) {
		Graphics g = gameScene.getGraphics();
		platformSkin = platformSkins[1];
		drawPlatformsObjects(g, ceilling);
	}

	private void paintPlataforms(Platform[] platforms) {
		Graphics g = gameScene.getGraphics();
		platformSkin = platformSkins[0];
		drawPlatformsObjects(g, platforms);
	}

	private void paintPlayer(Point playerPosition) {
		Graphics g = gameScene.getGraphics();
		g.drawImage(imgPlayer, playerPosition.x, playerPosition.y, Player.WIDTH, Player.HEIGTH, this);
	}

	private void paintBackground() {
		if (gameScene == null) {
			gameScene = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		}
		Graphics g = gameScene.getGraphics();
		g.drawImage(imgSpace, 0, 0, getWidth(), getHeight(), this);
		g.drawImage(skinCity, 0, 40, getWidth(), getHeight() - 80, this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(gameScene, 0, 0, this);
	}

	private void drawPlatformsObjects(Graphics g, Platform[] platformObjects) {
		if (platformObjects.length > 0) {
			for (Platform platform : platformObjects) {
				if (platform != null) {
					g.drawImage(platformSkin, platform.getPosition().x, platform.getPosition().y, platform.getWidth(),
							Platform.HEIGTH, this);
				}
			}
		}
	}

	public void stopMusic() {
		clip.stop();
	}

	private void startGameOverMusic() {
		try {
			if (!clipGameOver.isRunning() && count == 1) {
				count++;
				clipGameOver = AudioSystem.getClip();
				AudioInputStream inputStream = AudioSystem
						.getAudioInputStream(getClass().getResourceAsStream(GAME_OVER_MUSIC));
				clipGameOver.open(inputStream);
				clipGameOver.start();
			}

		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
		}
	}

	public void resetCount() {
		count = 1;
	}

	public void setActualSkin(int actualSkin) {
		this.actualSkin = actualSkin;
	}

	private void playButtonSound() {
		Clip buttonClip;
		try {
			buttonClip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(getClass().getResourceAsStream(BUTTON_MUSIC));
			buttonClip.open(inputStream);
			buttonClip.start();
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
		}
	}

	public void setScreenshot() {
		screenshot = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		this.paint(screenshot.getGraphics());
	}

	public BufferedImage getScreenshot() {
		return screenshot;
	}
}