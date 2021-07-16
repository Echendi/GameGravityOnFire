package views;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import models.Game;
import models.IGame;
import models.Player;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Point playerPosition;

	public GamePanel(ActionListener listener) {
		playerPosition = Game.PLAYER_STARTING_POSITION;
		requestFocus();
	}

	public void refreshGame(IGame game) {
		playerPosition = game.getPlayerPosition();
		repaint();
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(playerPosition.x, playerPosition.y, Player.WIDTH, Player.HEIGTH);
	}
}
