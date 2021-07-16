package views;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import models.IGame;
import models.Platform;
import models.Player;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Point playerPosition;
	private Platform[] platforms;

	public GamePanel(ActionListener listener) {
		playerPosition = Player.STARTING_POSITION;
		platforms = new Platform[0];
		requestFocus();
	}

	public void refreshGame(IGame game) {
		playerPosition = game.getPlayerPosition();
		platforms = game.getPlatforms();
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(playerPosition.x, playerPosition.y, Player.WIDTH, Player.HEIGTH);
		if (platforms.length > 0) {
			for (Platform platform : platforms) {
				g.fillRect(platform.getPosition().x, platform.getPosition().y, platform.getWidth(), Platform.HIGTH);
			}
		}
	}
}
