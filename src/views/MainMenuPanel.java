package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import buttons.Button;
import buttons.TypeButton;
import presenters.Command;

public class MainMenuPanel extends JPanel {

	private static final int TITLE_HEIGTH = 150;
	private static final int TITLE_WIDTH = 900;
	private static final Dimension BTN_PREFERRED_SIZE = new Dimension(200, 150);
	private static final String IMG_MAIN_MENU_TITLE_PNG = "/res/img/mainMenuTitle.png";
	private static final String IMG_HISTORY_PNG = "/res/img/history.png";
	private static final String IMG_MAIN_MENU_BG_PNG = "/res/img/mainMenuBg.png";
	private static final long serialVersionUID = 1L;

	private BufferedImage backgroung;
	private BufferedImage fire;
	private BufferedImage[] fireSkins;
	private JLabel lblTitle;
	private JLabel lblHistory;
	private Button btnStart;
	private Button btnIntructions;
	private Button btnStore;
	private Button btnExit;
	private Timer timer;
	private Clip clip;

	public MainMenuPanel(ActionListener listener) {
		initBackground();
		this.setLayout(new BorderLayout());
		initLblTitle();
		initButtons(listener);
		try {
			BufferedImage image = ImageIO.read(getClass().getResource("/res/img/fireMenu.png"));
			fireSkins = new BufferedImage[3];
			fireSkins[0] = image.getSubimage(0, 0, 537, 128);
			fireSkins[1] = image.getSubimage(0, 128, 537, 128);
			fireSkins[2] = image.getSubimage(0, 256, 537, 128);
			fire = fireSkins[0];
		} catch (IOException e) {
			e.printStackTrace();
		}
		initTimer();
	}

	private void initTimer() {
		timer = new Timer(150, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				fire = fire == fireSkins[0] ? fireSkins[1] : fire == fireSkins[1] ? fireSkins[2] : fireSkins[0];
				updateUI();
			}
		});
	}

	public void startTimer() {
		startMenuMusic();
		timer.start();
	}

	public void stopTimer() {
		stopMenuMusic();
		timer.stop();
	}

	private void initButtons(ActionListener listener) {
		JPanel panel = new JPanel();
		panel.setOpaque(false);

		btnStart = createButton(TypeButton.SUCCESS, "PLAY", listener, Command.START.toString());
		panel.add(btnStart);

		btnIntructions = createButton(TypeButton.INFO, "INSTRUCCIONES", listener, Command.INSTRUCTIONS.toString());
		panel.add(btnIntructions);

		btnStore = createButton(TypeButton.PRIMARY, "TIENDA", listener, Command.STORE.toString());
		panel.add(btnStore);

		btnExit = createButton(TypeButton.DANGER, "SALIR", listener, Command.EXIT.toString());
		panel.add(btnExit);

		initLblHistory(panel);

		add(panel, BorderLayout.CENTER);
	}

	private void initLblTitle() {
		lblTitle = new JLabel();
		Image icon = new ImageIcon(getClass().getResource(IMG_MAIN_MENU_TITLE_PNG)).getImage()
				.getScaledInstance(TITLE_WIDTH, TITLE_HEIGTH, Image.SCALE_SMOOTH);
		lblTitle.setIcon(new ImageIcon(icon));
		add(lblTitle, BorderLayout.NORTH);
	}

	private void initLblHistory(JPanel panel) {
		lblHistory = new JLabel();
		Image icon = new ImageIcon(getClass().getResource(IMG_HISTORY_PNG)).getImage().getScaledInstance(TITLE_WIDTH,
				TITLE_HEIGTH, Image.SCALE_SMOOTH);
		lblHistory.setIcon(new ImageIcon(icon));
		lblHistory.setOpaque(true);
		lblHistory.setBackground(new Color(0, 0, 0, 100));
		panel.add(lblHistory);
	}

	private Button createButton(TypeButton type, String text, ActionListener listener, String command) {
		Button button = new Button(type, text);
		button.addMouseListener(addMouseListener());
		button.addActionListener(listener);
		button.setActionCommand(command);
		button.setPreferredSize(BTN_PREFERRED_SIZE);
		button.setOpaque(false);
		button.setFont(new Font("Showcard Gothic", Font.PLAIN, 22));
		return button;
	}

	private MouseListener addMouseListener() {
		return new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				playButtonSound();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		};
	}

	private void initBackground() {
		try {
			backgroung = ImageIO.read(getClass().getResource(IMG_MAIN_MENU_BG_PNG));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroung, 0, 0, getWidth(), getHeight(), this);
		g.drawImage(fire, 0, getHeight() - 300, getWidth(), 330, this);
	}

	private void startMenuMusic() {
		try {
			clip = AudioSystem.getClip();
			if (clip.isRunning()) {
				clip.stop();
			}
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(getClass().getResourceAsStream("/res/media/menu.wav"));
			clip.open(inputStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
		}
	}

	private void stopMenuMusic() {
		clip.stop();
	}

	private void playButtonSound() {
		Clip buttonClip;
		try {
			buttonClip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(getClass().getResourceAsStream("/res/media/button.wav"));
			buttonClip.open(inputStream);
			buttonClip.start();
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
		}
	}
}
