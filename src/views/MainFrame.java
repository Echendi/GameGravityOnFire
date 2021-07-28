package views;

import java.awt.CardLayout;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import models.Game;
import models.IGame;

public class MainFrame extends JFrame {

	public static final String MENU_CARD = "menu";
	public static final String GAME_CARD = "game";
	private static final String TITLE = "Gravity On Fire V.1.0";
	private static final String IMG_GRAVITY_ICON_PNG = "/res/img/gravityIcon.png";

	private static final long serialVersionUID = 1L;

	private GamePanel gamePanel;
	private JPanel mainPanel;
	private MainMenuPanel menuPanel;
	private Timer timerUpdate;

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

	public void setBtnPauseText(String text) {
		this.gamePanel.setBtnPauseText(text);
	}

	public void setVisibleLblGameOver(boolean value) {
		this.gamePanel.setVisibleLblGameOver(value);
	}

	public void refreshGame(IGame game) {
		gamePanel.startMusic();
		timerUpdate = new Timer(5, e -> {
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

}
