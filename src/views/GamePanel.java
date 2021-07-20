package views;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JPanel;
import models.IGame;
import models.Platform;
import models.Player;
import models.Trap;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private static final String TIME_FORMAT = "00:00:00:00";
	private BufferedImage gameScene;
	private JLabel lblTime;

	public GamePanel(ActionListener listener) {
		lblTime = new JLabel(TIME_FORMAT);
		add(lblTime);
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
		paintBackground();
		paintPlataforms(game.getPlatforms());
		paintAbyss(game.getAbyss());
		paintFloor(game.getFloor());
		paintCeilling(game.getCeilling());
		paintFire(game.getFire());
		paintHero(game.getPlayerPosition());
		updateLblTime(game.getTime());
		repaint();
	}

	private void paintAbyss(Trap[] abyss) {
		Graphics g = gameScene.getGraphics();
		g.setColor(Color.BLACK);
		for (Trap trap : abyss) {
			if (trap != null) {
				g.fillRect(trap.getPosition().x, trap.getPosition().y, trap.getWidth(), trap.getHeigth());
			}
		}
	}

	private void paintFire(Trap fire) {
		Graphics g = gameScene.getGraphics();
		g.setColor(Color.RED);
		g.fillRect(fire.getPosition().x, fire.getPosition().y, fire.getWidth(), fire.getHeigth());
	}

	private void paintFloor(Platform[] floor) {
		Graphics g = gameScene.getGraphics();
		g.setColor(Color.MAGENTA);
		drawPlatformsObjects(g, floor);
	}

	private void paintCeilling(Platform[] ceilling) {
		Graphics g = gameScene.getGraphics();
		g.setColor(Color.ORANGE);
		drawPlatformsObjects(g, ceilling);
	}

	private void paintPlataforms(Platform[] platforms) {
		Graphics g = gameScene.getGraphics();
		g.setColor(Color.BLUE);
		drawPlatformsObjects(g, platforms);
	}

	private void paintHero(Point playerPosition) {
		Graphics g = gameScene.getGraphics();
		g.setColor(Color.GREEN);
		g.fillRect(playerPosition.x, playerPosition.y, Player.WIDTH, Player.HEIGTH);
	}

	private void paintBackground() {
		if (gameScene == null) {
			gameScene = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
		Graphics g = gameScene.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
//		g.drawImage(imgBack, 0, 0, getWidth(), getHeight(), this);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(gameScene, 0, 0, this);
	}

	private void drawPlatformsObjects(Graphics g, Platform[] platformObjects) {
		if (platformObjects.length > 0) {
			for (Platform platform : platformObjects) {
				g.fillRect(platform.getPosition().x, platform.getPosition().y, platform.getWidth(), Platform.HIGTH);
			}
		}
	}
}
