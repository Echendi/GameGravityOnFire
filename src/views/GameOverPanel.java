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

	private static final String COINS_FORMAT = "$ ";
	private static final Font FONT = new Font("Goudy Stout", Font.PLAIN, 25);
	private static final Color LABEL_BEST_SCORE_COLOR = Color.ORANGE;
	private static final Color LABEL_SCORE_COLOR = LABEL_BEST_SCORE_COLOR.brighter();
	private static final Color LABEL_COINS_COLOR = LABEL_BEST_SCORE_COLOR.darker();
	private static final Color BG = new Color(0, 0, 0, 200);
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
		setBackground(BG);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		initImgs();
		initComponents(gbc);
	}

	private void initImgs() {
		try {
			BufferedImage image = ImageIO.read(getClass().getResource(IMG_GAME_OVER_PNG));
			initImgs(image);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initComponents(GridBagConstraints gbc) {
		initGameOverTitle(gbc);
		initLblScoreTitle(gbc);
		initLblBestScoreTitle(gbc);
		initLblCoinsTitle(gbc);
		initLblScore(gbc);
		initLblBestScore(gbc);
		initLblCoins(gbc);
	}

	private void initLblCoins(GridBagConstraints gbc) {
		gbc.gridx = 1;
		gbc.gridy = 3;
		lblCoins = createLabel(LABEL_COINS_COLOR);
		add(lblCoins, gbc);
	}

	private void initLblBestScore(GridBagConstraints gbc) {
		gbc.gridx = 1;
		gbc.gridy = 2;
		lblBestScore = createLabel(LABEL_BEST_SCORE_COLOR);
		add(lblBestScore, gbc);
	}

	private void initLblScore(GridBagConstraints gbc) {
		gbc.gridx = 1;
		gbc.gridy = 1;
		lblScore = createLabel(LABEL_SCORE_COLOR);
		add(lblScore, gbc);
	}

	private void initLblCoinsTitle(GridBagConstraints gbc) {
		gbc.gridx = 0;
		gbc.gridy = 3;
		lblCoinsTitle = new JLabel(new ImageIcon(coinsImg));
		add(lblCoinsTitle, gbc);
	}

	private void initLblBestScoreTitle(GridBagConstraints gbc) {
		gbc.gridx = 0;
		gbc.gridy = 2;
		lblBestScoreTitle = new JLabel(new ImageIcon(bestScoreImg));
		add(lblBestScoreTitle, gbc);
	}

	private void initLblScoreTitle(GridBagConstraints gbc) {
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		lblScoreTitle = new JLabel(new ImageIcon(scoreImg));
		add(lblScoreTitle, gbc);
	}

	private void initGameOverTitle(GridBagConstraints gbc) {
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		lblGameOverTitle = new JLabel(new ImageIcon(gameOverImg));
		add(lblGameOverTitle, gbc);
	}

	private void initImgs(BufferedImage image) {
		gameOverImg = image.getSubimage(0, 0, 605, 100);
		scoreImg = image.getSubimage(0, 145, 350, 45);
		bestScoreImg = image.getSubimage(0, 190, 350, 45);
		coinsImg = image.getSubimage(0, 235, 350, 45);
	}

	private JLabel createLabel(Color color) {
		JLabel label = new JLabel();
		label.setFont(FONT);
		label.setForeground(color);
		return label;
	}

	public void updateScoreData(String score, String bestScore, int coins) {
		lblScore.setText(score);
		lblBestScore.setText(bestScore);
		lblCoins.setText(COINS_FORMAT + String.valueOf(coins));
	}
}
