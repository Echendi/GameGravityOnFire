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

	private static final String COINS_FORMAT = "$ 0";
	private static final String IMG_PLATFORM_2_PNG = "/res/img/platform2.png";
	private static final String IMG_PLATFORM_1_PNG = "/res/img/platform1.png";
	private static final String IMG_FIRE_PNG = "/res/img/fire.png";
	private static final String IMG_PLAYER_DOWN_PNG = "/res/img/playerDown.png";
	private static final String IMG_PLAYER_PNG = "/res/img/player.png";
	private static final String IMG_BACKGROUND_PNG = "/res/img/backGround.png";
	private static final String IMG_SPACE_PNG = "/res/img/space.png";

	private static final Font BTN_PAUSE_FONT = new Font("Gill Sans Ultra Bold", Font.PLAIN, 15);
	private static final Dimension BTN_PAUSE_DIMENSION = new Dimension(40, 22);
	public static final String PLAY_TEXT = "\u00BB";
	public static final String PAUSED_TEXT = "ll";

	private static final Font BTN_OPTION_FONT = new Font("Gill Sans Ultra Bold", Font.PLAIN, 15);
	private static final Dimension BTN_OPTION_DIMENSION = new Dimension(100, 22);
	public static final String EXIT_TEXT = "SALIR";

	private static final Font LBL_FONT = new Font("Bell MT", Font.PLAIN, 20);
	private static final Color LBL_COLOR = Color.WHITE;
	private static final String TIME_FORMAT = "00:00:00:00";

	private static final int IMG_CITY_WIDTH = 3400;
	private static final int Y_START_IMG_BG = 0;
	private static final int Y_END_IMG_BG = 850;

	private static final long serialVersionUID = 1L;
	private static final String MENU_TEXT = "MENU";

	private BufferedImage gameScene;
	private BufferedImage imgFire;
	private BufferedImage[] fireSkins;
	private BufferedImage imgSpace;
	private BufferedImage imgCity;
	private BufferedImage skinCity;
	private BufferedImage platformSkin;
	private BufferedImage[] platformSkins;
	private BufferedImage imgPlayer;
	private BufferedImage[] playerSkins;

	private Timer timer;
	private Clip clip;
	private JLabel lblTime;
	private JLabel lblCoins;
	private GameOverPanel panelGameOver;
	private Button btnMenu;
	private Button btnPause;
	private Button btnExit;
	private boolean isDown;
	private boolean isPaused;

	public GamePanel() {
		setLayout(new BorderLayout());
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

		initBtnPause(listener);
		panel.add(btnPause);

		initLblCoins(panel);

		add(panel, BorderLayout.NORTH);
	}

	private void addOptionPanel(ActionListener listener) {
		JPanel panel2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panel2.setOpaque(false);

		btnMenu = new Button(TypeButton.WARNING, MENU_TEXT);
		btnMenu.setPreferredSize(BTN_OPTION_DIMENSION);
		btnMenu.setOpaque(false);
		btnMenu.setFocusable(false);
		btnMenu.addActionListener(listener);
		btnMenu.setActionCommand(Command.MENU.toString());
		btnMenu.setFont(BTN_OPTION_FONT);
		btnMenu.setVisible(true);
		panel2.add(btnMenu);

		btnExit = new Button(TypeButton.DANGER, EXIT_TEXT);
		btnExit.setPreferredSize(BTN_OPTION_DIMENSION);
		btnExit.setOpaque(false);
		btnExit.setFocusable(false);
		btnExit.addActionListener(listener);
		btnExit.setActionCommand(Command.EXIT.toString());
		btnExit.setFont(BTN_OPTION_FONT);
		btnExit.setVisible(true);
		panel2.add(btnExit);

		add(panel2, BorderLayout.SOUTH);
	}

	private void initBtnPause(ActionListener listener) {
		btnPause = new Button(TypeButton.SUCCESS, PAUSED_TEXT);
		btnPause.setPreferredSize(BTN_PAUSE_DIMENSION);
		btnPause.setOpaque(false);
		btnPause.setFocusable(false);
		btnPause.addActionListener(listener);
		btnPause.setActionCommand(Command.PAUSE.toString());
		btnPause.setFont(BTN_PAUSE_FONT);
		btnPause.setVisible(true);

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

	public void setBtnPauseText(String text) {
		this.btnPause.setText(text);
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
		this.imgSpace = ImageIO.read(getClass().getResource(IMG_SPACE_PNG)).getSubimage(0, 0, Game.MAP_WIDTH,
				Game.MAP_HEIGTH);
		this.imgCity = ImageIO.read(getClass().getResource(IMG_BACKGROUND_PNG));
	}

	private void initPlayerSkins() throws IOException {
		BufferedImage image = ImageIO.read(getClass().getResource(IMG_PLAYER_PNG));
		playerSkins = new BufferedImage[6];
		initDownSkins(image);
		image = ImageIO.read(getClass().getResource(IMG_PLAYER_DOWN_PNG));
		initUpSkins(image);
	}

	private void initUpSkins(BufferedImage image) {
		playerSkins[3] = image.getSubimage(0, 0, 123 / 3 - 15, 43);
		playerSkins[4] = image.getSubimage((123 / 3) + 8, 0, 123 / 3 - 15, 43);
		playerSkins[5] = image.getSubimage((123 / 3 * 2) + 14, 0, 123 / 3 - 15, 43);
	}

	private void initDownSkins(BufferedImage image) {
		playerSkins[0] = image.getSubimage(0, 0, 123 / 3 - 15, 43);
		playerSkins[1] = image.getSubimage((123 / 3) + 8, 0, 123 / 3 - 15, 43);
		playerSkins[2] = image.getSubimage((123 / 3 * 2) + 14, 0, 123 / 3 - 15, 43);
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
					changeFireSkin();
					changeCitySkin(count);
					changePlayerSkin();
					count = count + Game.MAP_WIDTH > IMG_CITY_WIDTH ? 0 : count + 1;
				}
			}
		});
	}

	public void startMusic() {
		try {
			clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(getClass().getResourceAsStream("/res/media/gameMusic.wav"));
			clip.open(inputStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
		}
		timer.start();
	}

	private void changePlayerSkin() {
		if (isDown) {
			imgPlayer = imgPlayer == playerSkins[0] ? playerSkins[1]
					: imgPlayer == playerSkins[1] ? playerSkins[2] : playerSkins[0];
		} else {
			imgPlayer = imgPlayer == playerSkins[3] ? playerSkins[4]
					: imgPlayer == playerSkins[4] ? playerSkins[5] : playerSkins[3];
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
	}

	private void verifyState(IGame game) {
		isDown = game.isDown();
		isPaused = game.isPause();
		if (!game.isExecute()) {
			timer.stop();
			clip.stop();
			showGameOverPanel(game);
		}
	}

	private void showGameOverPanel(IGame game) {
		panelGameOver.setVisible(true);
		panelGameOver.updateScoreData(formatTime(game.getTime()), formatTime(game.getBestScore()),
				game.getParcialCoins());
	}

	public void updateLblCoins(int coins) {
		lblCoins.setText(" $ " + coins);
	}

	private void paintFire(Trap fire) {
		Graphics g = gameScene.getGraphics();
		g.drawImage(imgFire, fire.getPosition().x, fire.getPosition().y, fire.getWidth() + (Player.WIDTH * 2),
				fire.getHeight(), this);

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
			gameScene = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
		Graphics g = gameScene.getGraphics();
		g.setColor(LBL_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());
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
}