package views;

import java.awt.Color;
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
	private Platform[] floor;
	private Platform[] ceilling;

	public GamePanel(ActionListener listener) {
		playerPosition = Player.STARTING_POSITION;
		platforms = new Platform[0];
		floor = new Platform[0];
		ceilling = new Platform[0];
		requestFocus();
	}

	public void refreshGame(IGame game) {
		playerPosition = game.getPlayerPosition();
		platforms = game.getPlatforms();
		floor = game.getFloor();
		ceilling = game.getCeilling();
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		drawPlatformsObjects(g, platforms);
		g.setColor(Color.RED);
		drawPlatformsObjects(g, floor);
		g.setColor(Color.ORANGE);
		drawPlatformsObjects(g, ceilling);
		g.setColor(Color.GREEN);
		g.fillRect(playerPosition.x, playerPosition.y, Player.WIDTH, Player.HEIGTH);
	}

	private void drawPlatformsObjects(Graphics g, Platform[] platformObjects) {
		if (platformObjects.length > 0) {
			for (Platform platform : platformObjects) {
				g.fillRect(platform.getPosition().x, platform.getPosition().y, platform.getWidth(), Platform.HIGTH);
			}
		}
	}
}
