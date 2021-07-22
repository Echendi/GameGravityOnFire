package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
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

	private static final String IMG_PLATFORM_2_PNG = "/res/img/platform2.png";
	private static final String IMG_PLATFORM_1_PNG = "/res/img/platform1.png";
	private static final String IMG_FIRE_PNG = "/res/img/fire.png";
	private static final String IMG_PLAYER_DOWN_PNG = "/res/img/playerDown.png";
	private static final String IMG_PLAYER_PNG = "/res/img/player.png";
	private static final String IMG_BACKGROUND_PNG = "/res/img/backGround.png";
	private static final String IMG_SPACE_PNG = "/res/img/space.png";
	private static final Font BTN_PAUSE_FONT = new Font("Gill Sans Ultra Bold", Font.PLAIN, 15);
	private static final Dimension BTN_PAUSE_DIMENSION = new Dimension(40, 22);
	private static final Font TIME_FONT = new Font("Bell MT", Font.PLAIN, 20);
	private static final Color LBL_TIME_COLOR = Color.WHITE;
	private static final int IMG_CITY_WIDTH = 3500;
	private static final int X_START_IMG_BG = 50;
	private static final int X_END_IMG_BG = 850;
	public static final String PLAY_TEXT = "\u00BB";
	public static final String PAUSED_TEXT = "ll" + "";
	private static final long serialVersionUID = 1L;
	private static final String TIME_FORMAT = "00:00:00:00";

	private BufferedImage gameScene;
	private BufferedImage imgFire;
	private BufferedImage[] fireSkins;
	private BufferedImage imgPlayer;
	private BufferedImage[] playerSkins;
	private BufferedImage imgSpace;
	private BufferedImage imgCity;
	private BufferedImage skinCity;
	private BufferedImage platformSkin;
	private BufferedImage[] platformSkins;
	private Timer timer;
	private JLabel lblTime;
	private Button btnPause;
	private boolean isDown;
	private boolean isPaused;
	private boolean isExecute;

	public GamePanel(ActionListener listener) {
		setLayout(new BorderLayout());
		initComponents(listener);
	}

	private void initComponents(ActionListener listener) {
		isDown = true;
		isPaused = false;
		initSkins();
		initPanel(listener);
	}

	private void initPanel(ActionListener listener) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);

		initLblTime(panel);
		initBtnPause(listener);

		panel.add(btnPause);
		add(panel, BorderLayout.NORTH);
	}

	private void initBtnPause(ActionListener listener) {
		btnPause = new Button(TypeButton.DANGER, PAUSED_TEXT);
		btnPause.setPreferredSize(BTN_PAUSE_DIMENSION);
		btnPause.setFocusable(false);
		btnPause.addActionListener(listener);
		btnPause.setActionCommand(Command.PAUSE.toString());
		btnPause.setFont(BTN_PAUSE_FONT);
		btnPause.setVisible(true);
	}

	private void initLblTime(JPanel panel) {
		lblTime = new JLabel(TIME_FORMAT);
		lblTime.setForeground(LBL_TIME_COLOR);
		lblTime.setFont(TIME_FONT);
		panel.add(lblTime);
	}

	public void setBtnPauseText(String text) {
		this.btnPause.setText(text);
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
		timer = new Timer(150, new ActionListener() {
			int count = 0;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isExecute && !isPaused) {
					changeFireSkin();
					changeCitySkin(count);
					changePlayerSkin();
					count = count + Game.MAP_WIDTH > IMG_CITY_WIDTH ? 0 : count + 1;
				}
			}
		});
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
		skinCity = imgCity.getSubimage(count, X_START_IMG_BG, Game.MAP_WIDTH, X_END_IMG_BG);
	}

	private void changeFireSkin() {
		imgFire = imgFire == fireSkins[0] ? fireSkins[1] : imgFire == fireSkins[1] ? fireSkins[2] : fireSkins[0];
	}

	private void updateLblTime(int[] time) {
		int hours = time[0];
		int minuts = time[1];
		int seconds = time[2];
		int millis = time[3];
		lblTime.setText((hours < 10 ? "0" : "") + hours + ":" + (minuts < 10 ? "0" : "") + minuts + ":"
				+ (seconds < 10 ? "0" : "") + seconds + ":" + (millis < 10 ? "0" : "") + millis);
	}

	public void refreshGame(IGame game) {
		isDown = game.isDown();
		isPaused = game.isPause();
		isExecute = game.isExecute();
		paintBackground();
		paintPlataforms(game.getPlatforms());
		paintFloor(game.getFloor());
		paintCeilling(game.getCeilling());
		paintFire(game.getFire());
		paintPlayer(game.getPlayerPosition());
		updateLblTime(game.getTime());
		repaint();
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
		g.setColor(Color.GREEN);
		g.drawImage(imgPlayer, playerPosition.x, playerPosition.y, Player.WIDTH, Player.HEIGTH, this);
	}

	private void paintBackground() {
		if (gameScene == null) {
			gameScene = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
		Graphics g = gameScene.getGraphics();
		g.setColor(LBL_TIME_COLOR);
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
}
