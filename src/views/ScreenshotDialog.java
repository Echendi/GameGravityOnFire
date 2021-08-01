package views;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import buttons.Button;
import buttons.TypeButton;
import models.Game;
import presenters.Command;

@SuppressWarnings("serial")
public class ScreenshotDialog extends JDialog {

	private static final int DIALOG_HEIGHT = Game.MAP_HEIGTH - 100;
	private static final int DIALOG_WIDTH = Game.MAP_WIDTH - 200;
	private static final String BTN_MUSIC = "/res/media/button.wav";
	private static final Dimension BTN_OPTION_DIMENSION = new Dimension(50, 50);
	private static final Font BTN_OPTION_FONT = new Font("Bell MT", Font.PLAIN, 20);
	JLabel lblScreenshot;
	ArrayList<ImageIcon> gallery;
	int count;

	public ScreenshotDialog(JFrame frame) {
		super(frame, true);
		setLayout(new BorderLayout());
		setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
		setLocationRelativeTo(frame);
		setUndecorated(true);
		setBackground(new Color(255, 255, 255, 50));
		setShape(new RoundRectangle2D.Double(0, 0, this.getBounds().width, this.getBounds().height, 20, 20));
	}

	public void init(ActionListener listener) {
		add(createOptionButton(TypeButton.DARK, "<", Command.BACK, listener), BorderLayout.WEST);
		add(createOptionButton(TypeButton.DARK, ">", Command.NEXT, listener), BorderLayout.EAST);
		add(createOptionButton(TypeButton.PRIMARY, "VOLVER AL MENÚ PRINCIPAL", null, createActionListener()),
				BorderLayout.SOUTH);
		lblScreenshot = new JLabel();
		lblScreenshot.setPreferredSize(new Dimension(DIALOG_WIDTH - 100, DIALOG_HEIGHT - 50));
		add(lblScreenshot, BorderLayout.CENTER);
	}

	public void showScreenshots(ArrayList<ImageIcon> gallery) {
		count = 0;
		this.gallery = gallery;
		if (gallery.size() > 0) {
			setImage();
		} else {
			lblScreenshot.setIcon(null);
		}
		setVisible(true);
	}

	private Button createOptionButton(TypeButton type, String text, Command command, ActionListener listener) {
		Button btn = new Button(type, text);
		btn.setPreferredSize(BTN_OPTION_DIMENSION);
		btn.setOpaque(false);
		btn.setFocusable(false);
		btn.addActionListener(listener);
		if (command != null) {
			btn.setActionCommand(command.toString());
		}
		btn.setFont(BTN_OPTION_FONT);
		btn.setVisible(true);
		btn.addMouseListener(createMouseListener());
		return btn;
	}

	public void nextImage() {
		if (gallery.size() > 0) {
			if (++count >= gallery.size()) {
				count = 0;
			}
			setImage();
		}
	}

	public void backImage() {
		if (gallery.size() > 0) {
			if (--count < 0) {
				count = gallery.size() - 1;
			}
			setImage();
		}
	}

	private void setImage() {
		Image image = gallery.get(count).getImage().getScaledInstance(lblScreenshot.getPreferredSize().width,
				lblScreenshot.getPreferredSize().height, Image.SCALE_SMOOTH);
		lblScreenshot.setIcon(new ImageIcon(image));
	}

	private MouseListener createMouseListener() {
		return new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				playButtonSound();
			}
		};
	}

	private ActionListener createActionListener() {
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
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

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(4));
		g2d.setColor(Color.ORANGE);
		g2d.drawRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
	}
}
