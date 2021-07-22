package views;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import models.Game;
import models.IGame;

public class MainFrame extends JFrame {

	private static final String TITLE = "Gravity On Fire V.1.0";

	private static final String IMG_GRAVITY_ICON_PNG = "/res/img/gravityIcon.png";

	private static final long serialVersionUID = 1L;

	private GamePanel panel;

	public MainFrame(ActionListener listener, KeyListener keyListener) {
		super(TITLE);
		initComponents(listener, keyListener);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(Game.MAP_WIDTH, Game.MAP_HEIGTH);
		this.setResizable(false);
		this.setIconImage(new ImageIcon(getClass().getResource(IMG_GRAVITY_ICON_PNG)).getImage());
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void initComponents(ActionListener listener, KeyListener keyListener) {
		addKeyListener(keyListener);
		panel = new GamePanel(listener);
		this.getContentPane().add(panel);

	}

	public void setBtnPauseText(String text) {
		this.panel.setBtnPauseText(text);
	}

	public void refreshGame(IGame game) {
		panel.refreshGame(game);
	}

}
