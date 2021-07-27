package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameOverPanel extends JPanel {

	private static final String IMG_GAME_OVER_PNG = "/res/img/gameOver.png";
	private static final long serialVersionUID = 1L;

	private Image gameOverImg;
	private Image scoreImg;
	private Image bestScoreImg;
	private Image coinsImg;
	private JLabel lblGameOverTitle;
	private JLabel lblScoreTitle;
	private JLabel lblScore;
	private JLabel lblBestScoreTitle;
	private JLabel lblBestScore;
	private JLabel lblCoinsTitle;
	private JLabel lblCoins;

	public GameOverPanel() {
		setLayout(new GridBagLayout());
		setBackground(new Color(0, 0, 0, 200));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		try {
			BufferedImage image = ImageIO.read(getClass().getResource(IMG_GAME_OVER_PNG));
			gameOverImg = image.getSubimage(0, 0, 605, 100);
			scoreImg = image.getSubimage(0, 145, 350, 45);
			bestScoreImg = image.getSubimage(0, 190, 350, 45);
			coinsImg = image.getSubimage(0, 235, 350, 45);

			gbc.gridwidth = 2;
			gbc.gridx = 0;
			gbc.gridy = 0;
			lblGameOverTitle = new JLabel(new ImageIcon(gameOverImg));
			add(lblGameOverTitle, gbc);

			gbc.gridwidth = 1;
			gbc.gridx = 0;
			gbc.gridy = 1;
			lblScoreTitle = new JLabel(new ImageIcon(scoreImg));
			add(lblScoreTitle, gbc);

			gbc.gridx = 0;
			gbc.gridy = 2;
			lblBestScoreTitle = new JLabel(new ImageIcon(bestScoreImg));
			add(lblBestScoreTitle, gbc);

			gbc.gridx = 0;
			gbc.gridy = 3;
			lblCoinsTitle = new JLabel(new ImageIcon(coinsImg));
			add(lblCoinsTitle, gbc);

			gbc.gridx = 1;
			gbc.gridy = 1;
			lblScore = createLabel(Color.ORANGE.brighter());
			add(lblScore, gbc);

			gbc.gridx = 1;
			gbc.gridy = 2;
			lblBestScore = createLabel(Color.ORANGE);
			add(lblBestScore, gbc);

			gbc.gridx = 1;
			gbc.gridy = 3;
			lblCoins = createLabel(Color.ORANGE.darker());
			add(lblCoins, gbc);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private JLabel createLabel(Color color) {
		JLabel label = new JLabel();
		label.setFont(new Font("Goudy Stout", Font.PLAIN, 25));
		label.setForeground(color);
		return label;
	}

	public void updateScoreData(String score, String bestScore, int coins) {
		lblScore.setText(score);
		lblBestScore.setText(bestScore);
		lblCoins.setText("$ "+String.valueOf(coins));
	}
}
