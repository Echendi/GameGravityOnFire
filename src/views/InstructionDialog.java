package views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JDialog;
import javax.swing.JFrame;

import buttons.Button;
import buttons.TypeButton;

@SuppressWarnings("serial")
public class InstructionDialog extends JDialog {

	private static final String BTN_MUSIC = "/res/media/button.wav";
	private static final Font FONT = new Font("Bell MT", Font.PLAIN, 20);
	private static final String BACK_TO_MENU_TEXT = "Volver al Menu Principal";
	private static final String IMG_BG = "/res/img/instructions.png";
	private BufferedImage image;

	public InstructionDialog(JFrame frame) {
		super(frame, true);
		setLayout(new BorderLayout());
		initImg();
		createBtnExit();
		setSize(900, 600);
		setLocationRelativeTo(frame);
		setUndecorated(true);
		setShape(new RoundRectangle2D.Double(0, 0, this.getBounds().width, this.getBounds().height, 20, 20));
	}

	private void initImg() {
		try {
			image = ImageIO.read(getClass().getResource(IMG_BG));
		} catch (IOException e) {
		}
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

	@Override
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}
}
