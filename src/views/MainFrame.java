package views;

import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import models.Game;
import models.IGame;

public class MainFrame extends JFrame {

	private static final String CHANGE_GRAVITY_MUSIC = "/res/media/changeGravity.wav";
	public static final String MENU_CARD = "menu";
	public static final String GAME_CARD = "game";
	private static final String TITLE = "Gravity On Fire V.1.0";
	private static final String IMG_GRAVITY_ICON_PNG = "/res/img/gravityIcon.png";

	private static final long serialVersionUID = 1L;

	private GamePanel gamePanel;
	private JPanel mainPanel;
	private MainMenuPanel menuPanel;
	private Timer timerUpdate;
	private StoreDialog store;

	public MainFrame(ActionListener listener, KeyListener keyListener) {
		super(TITLE);
		initComponents(listener, keyListener);
		this.setSize(Game.MAP_WIDTH, Game.MAP_HEIGTH);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setIconImage(new ImageIcon(getClass().getResource(IMG_GRAVITY_ICON_PNG)).getImage());
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
		this.setShape(new RoundRectangle2D.Double(0, 0, this.getBounds().width, this.getBounds().height, 20, 20));
		this.setVisible(true);
	}

	private void initComponents(ActionListener listener, KeyListener keyListener) {
		store = new StoreDialog(this);
		addKeyListener(keyListener);

		mainPanel = new JPanel(new CardLayout());

		menuPanel = new MainMenuPanel(listener);
		mainPanel.add(MENU_CARD, menuPanel);

		gamePanel = new GamePanel();
		gamePanel.initComponents(listener);
		mainPanel.add(GAME_CARD, gamePanel);

		this.getContentPane().add(mainPanel);
	}

	public void changeCard(String cardName) {
		CardLayout card = (CardLayout) mainPanel.getLayout();
		if (cardName.equals(MENU_CARD)) {
			menuPanel.startTimer();
		} else {
			menuPanel.stopTimer();
		}
		card.show(mainPanel, cardName);
		requestFocus();
	}

	public void setVisibleLblGameOver(boolean value) {
		this.gamePanel.setVisibleLblGameOver(value);
	}

	public void refreshGame(IGame game) {
		gamePanel.startMusic();
		timerUpdate = new Timer(1, e -> {
			gamePanel.refreshGame(game);
		});
		timerUpdate.start();
	}

	public void stopGame() {
		gamePanel.stopMusic();
		timerUpdate.stop();
	}

	public void resetCount() {
		gamePanel.resetCount();
	}

	public void exit() {
		this.dispose();
		System.exit(0);
	}

	public void setActualSkin(int actualSkin) {
		gamePanel.setActualSkin(actualSkin);
	}

	public void playChangeGravitySound() {
		Clip clip;
		try {
			clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(getClass().getResourceAsStream(CHANGE_GRAVITY_MUSIC));
			clip.open(inputStream);
			clip.start();
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
		}
	}

	public void loadStore(IGame game, ActionListener listener) {
		store.loadStore(game, listener);
	}

	public void showStore(IGame game, ActionListener listener) {
		loadStore(game, listener);
		store.setVisible(true);
	}
}
