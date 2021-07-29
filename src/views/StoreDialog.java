package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import buttons.Button;
import buttons.TypeButton;
import models.IGame;
import presenters.Command;

@SuppressWarnings("serial")
public class StoreDialog extends JDialog {

	private static final String BTN_MUSIC = "/res/media/button.wav";
	private static final Font BTN_FONT = new Font("Gill Sans Ultra Bold", Font.PLAIN, 20);
	private static final String USE_TEXT = "USAR";
	private static final String USING_TEXT = "USANDO";
	private static final String INSUF_TEXT = "DINERO INSUFICIENTE";
	private static final String BUY_TEXT = "COMPRAR";
	private static final Font FONT_BOLD = new Font("Bell MT", Font.BOLD, 20);
	private static final String COINS_FORMAT = "$ ";
	private static final Font FONT = new Font("Bell MT", Font.PLAIN, 20);
	private static final String BACK_TO_MENU_TEXT = "Volver al Menu Principal";
	private static final String TITLE = "TIENDA ESPACIAL";
	public static final int VALUE_INCREMENT = 700;
	private static final String IMG_PLAYER_PNG = "/res/img/storeSkins.png";
	private JScrollPane pane;
	private JPanel panel;
	private BufferedImage image;
	private JLabel lblCoins;

	public StoreDialog(JFrame frame) {
		super(frame, TITLE, true);
		setLayout(new BorderLayout());
		setSize(500, 500);
		setLocationRelativeTo(frame);
		setUndecorated(true);
		setBackground(new Color(255, 100, 0, 200));
		init();
	}

	private void readImg() {
		try {
			image = ImageIO.read(getClass().getResource(IMG_PLAYER_PNG));
		} catch (IOException e) {
		}
	}

	private void init() {
		createLblCoins();
		initPanel();
		initScrollPane();
		createBtnExit();
		readImg();
	}

	private void createBtnExit() {
		Button btnExit = new Button(TypeButton.PRIMARY, BACK_TO_MENU_TEXT);
		btnExit.setPreferredSize(new Dimension(200, 35));
		btnExit.setFont(FONT);
		btnExit.addActionListener(createActionListener());
		btnExit.addMouseListener(createMouseListener());
		add(btnExit, BorderLayout.SOUTH);
	}

	private ActionListener createActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		};
	}

	private void initPanel() {
		panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout((new BoxLayout(panel, BoxLayout.Y_AXIS)));
	}

	private void initScrollPane() {
		pane = new JScrollPane();
		pane.setOpaque(false);
		pane.setViewportView(panel);
		pane.getViewport().setOpaque(false);
		add(pane, BorderLayout.CENTER);
	}

	private void createLblCoins() {
		lblCoins = new JLabel(COINS_FORMAT, SwingConstants.CENTER);
		lblCoins.setFont(FONT);
		lblCoins.setForeground(Color.WHITE);
		add(lblCoins, BorderLayout.NORTH);
	}

	public void loadStore(IGame game, ActionListener listener) {
		lblCoins.setText(COINS_FORMAT + String.valueOf(game.getCoins()));
		panel.removeAll();
		ArrayList<Integer> purshasedSkins = game.getPurchasedSkins();
		for (int i = 0; i < GamePanel.PLAYERS_QUANTITY; i++) {
			createCard(i, listener, purshasedSkins.contains(i), game.getActualSkin(), game.getCoins());
		}
		pane.updateUI();
	}

	private void createCard(int i, ActionListener listener, boolean isPurshased, int actualSkin, int coins) {
		JPanel card = new JPanel();
		card.setOpaque(false);
		int value = calculateValue(i);

		createLblSkin(i, card);
		createLblPrice(card, value);
		createBtnStore(i, listener, isPurshased, actualSkin, coins, card, value);

		panel.add(card);
		pane.repaint();
	}

	private void createLblSkin(int i, JPanel card) {
		JLabel skin = new JLabel(new ImageIcon(image.getSubimage(0, i * GamePanel.IMG_PLAYER_HEIGTH,
				GamePanel.IMG_PLAYER_WIDTH, GamePanel.IMG_PLAYER_HEIGTH - 1)));
		card.add(skin);
	}

	private void createLblPrice(JPanel card, int value) {
		JLabel lblPrice = new JLabel(COINS_FORMAT + value, SwingConstants.CENTER);
		lblPrice.setPreferredSize(new Dimension(100, 35));
		lblPrice.setFont(FONT_BOLD);
		lblPrice.setForeground(Color.WHITE);
		card.add(lblPrice);
	}

	private void createBtnStore(int i, ActionListener listener, boolean isPurshased, int actualSkin, int coins,
			JPanel card, int value) {
		if (isPurshased) {
			createBtnUse(i, listener, actualSkin, card);
		} else {
			createBtnBuy(i, listener, coins, card, value);
		}
	}

	private void createBtnBuy(int i, ActionListener listener, int coins, JPanel card, int value) {
		if (coins >= value) {
			Button btnBuy = createButton(i, TypeButton.DANGER, BUY_TEXT, Command.BUY, listener);
			card.add(btnBuy);
		} else {
			Button btnBuy = createButton(i, TypeButton.SECONDARY, INSUF_TEXT, null, listener);
			card.add(btnBuy);
		}
	}

	private void createBtnUse(int i, ActionListener listener, int actualSkin, JPanel card) {
		if (i == actualSkin) {
			Button btnUse = createButton(i, TypeButton.WARNING, USING_TEXT, null, listener);
			card.add(btnUse);
		} else {
			Button btnUse = createButton(i, TypeButton.SUCCESS, USE_TEXT, Command.USE, listener);
			card.add(btnUse);
		}
	}

	private int calculateValue(int i) {
		return i * VALUE_INCREMENT;
	}

	private Button createButton(int elementPosition, TypeButton type, String text, Command command,
			ActionListener listener) {
		Button btn = new Button(type, text);
		btn.setName(String.valueOf(elementPosition));
		btn.setPreferredSize(new Dimension(300, 35));
		btn.setOpaque(false);
		btn.setFocusable(false);
		if (command != null) {
			btn.addActionListener(listener);
			btn.setActionCommand(command.toString());
			btn.addMouseListener(createMouseListener());
		}
		btn.setFont(BTN_FONT);
		btn.setVisible(true);
		return btn;
	}

	private MouseListener createMouseListener() {
		return new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				playButtonSound();
			}
		};
	}

	private void playButtonSound() {
		Clip buttonClip;
		try {
			buttonClip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(BTN_MUSIC));
			buttonClip.open(inputStream);
			buttonClip.start();
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
		}
	}
}
