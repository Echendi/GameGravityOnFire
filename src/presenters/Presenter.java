package presenters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import models.Game;
import persistence.FileManager;
import views.GamePanel;
import views.MainFrame;

public class Presenter extends KeyAdapter implements ActionListener {

	private MainFrame view;
	private Game game;

	public Presenter() {
		view = new MainFrame(this, this);
		view.changeCard(MainFrame.MENU_CARD);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Command.valueOf(e.getActionCommand())) {
		case PAUSE -> pause();
		case EXIT -> exit();
		case INSTRUCTIONS -> showInstrutions();
		case START -> start();
		case STORE -> showStore();
		case MENU -> showMenu();
		}
	}

	private void showMenu() {
		view.stopGame();
		view.changeCard(MainFrame.MENU_CARD);
		game.stop();
		game = null;
	}

	private void start() {
		view.changeCard(MainFrame.GAME_CARD);
		view.setVisibleLblGameOver(false);
		view.resetCount();
		game = new Game(FileManager.loadScoreList());
		game.start();
		view.setActualSkin(game.getActualSkin());
		view.refreshGame(game);
	}

	private void showStore() {
		// TODO Auto-generated method stub
	}

	private void showInstrutions() {
		// TODO Auto-generated method stub
	}

	private void exit() {
		view.exit();
	}

	private synchronized void pause() {
		if (game.isPause()) {
			game.resume();
			view.setBtnPauseText(GamePanel.PAUSED_TEXT);
		} else {
			game.pause();
			view.setBtnPauseText(GamePanel.PLAY_TEXT);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getExtendedKeyCode();
		switch (code) {
		case KeyEvent.VK_SPACE -> changeGravity();
		case KeyEvent.VK_P -> pause();
		}
	}

	private void changeGravity() {
		if (game.isExecute() && !game.isPause()) {
			game.changeGravity();
			playChangeGravitySound();
		}
	}

	private void playChangeGravitySound() {
		Clip clip;
		try {
			clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(getClass().getResourceAsStream("/res/media/changeGravity.wav"));
			clip.open(inputStream);
			clip.start();
		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
		}
	}

}
