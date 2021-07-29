package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
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

	public static final int VALUE_INCREMENT = 700;
	private static final String IMG_PLAYER_PNG = "/res/img/storeSkins.png";
	private JScrollPane pane;
	private JPanel panel;
	private BufferedImage image;
	private JLabel lblCoins;

	public StoreDialog(JFrame frame) {
		super(frame, "TIENDA ESPACIAL", true);
		setSize(500, 500);
		setLocationRelativeTo(frame);
		setUndecorated(true);
		setBackground(new Color(255, 100, 0, 200));
		init();
		readImg();
	}

	private void readImg() {
		try {
			image = ImageIO.read(getClass().getResource(IMG_PLAYER_PNG));
		} catch (IOException e) {
		}
	}

	private void init() {
		setLayout(new BorderLayout());
		
		lblCoins = new JLabel("$", SwingConstants.CENTER);
		lblCoins.setFont(new Font("Bell MT", Font.BOLD, 30));
		lblCoins.setForeground(Color.WHITE);
		add(lblCoins,BorderLayout.NORTH);

		panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout((new BoxLayout(panel, BoxLayout.Y_AXIS)));
		pane = new JScrollPane();
		pane.setOpaque(false);
		pane.setViewportView(panel);
		pane.getViewport().setOpaque(false);

		add(pane, BorderLayout.CENTER);

		Button btnExit = new Button(TypeButton.PRIMARY, "Volver al Menu Principal");
		btnExit.setPreferredSize(new Dimension(200, 35));
		btnExit.setFont(new Font("Bell MT", Font.PLAIN, 20));
		btnExit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		add(btnExit, BorderLayout.SOUTH);
	}

	public void loadStore(IGame game, ActionListener listener) {
		lblCoins.setText("$ "+String.valueOf(game.getCoins()));

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

		JLabel skin = new JLabel(new ImageIcon(image.getSubimage(0, i * GamePanel.IMG_PLAYER_HEIGTH,
				GamePanel.IMG_PLAYER_WIDTH, GamePanel.IMG_PLAYER_HEIGTH - 1)));
		card.add(skin);

		JLabel lblPrice = new JLabel("$ " + value, SwingConstants.CENTER);
		lblPrice.setPreferredSize(new Dimension(100, 35));
		lblPrice.setFont(new Font("Bell MT", Font.BOLD, 20));
		lblPrice.setForeground(Color.WHITE);
		card.add(lblPrice);

		if (isPurshased) {
			if (i == actualSkin) {
				Button btnUse = createButton(i, TypeButton.WARNING, "USANDO", Command.USE, listener);
				card.add(btnUse);
			} else {
				Button btnUse = createButton(i, TypeButton.SUCCESS, "USAR", Command.USE, listener);
				card.add(btnUse);
			}
		} else {
			if (coins >= value) {
				Button btnBuy = createButton(i, TypeButton.DANGER, "COMPRAR", Command.BUY, listener);
				card.add(btnBuy);
			} else {
				Button btnBuy = createButton(i, TypeButton.SECONDARY, "DINERO INSUFICIENTE", Command.NONE, listener);
				card.add(btnBuy);
			}
		}

		panel.add(card);
		pane.repaint();
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
		btn.addActionListener(listener);
		btn.setActionCommand(command.toString());
		btn.setFont(new Font("Gill Sans Ultra Bold", Font.PLAIN, 20));
		btn.setVisible(true);
		return btn;
	}
}
