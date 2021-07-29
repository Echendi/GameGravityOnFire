package presenters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import buttons.Button;
import models.Game;
import persistence.FileManager;
import views.GamePanel;
import views.MainFrame;
import views.StoreDialog;

public class Presenter extends KeyAdapter implements ActionListener {

	private MainFrame view;
	private Game game;

	public Presenter() {
		view = new MainFrame(this, this);
		game = new Game(FileManager.loadGameData());
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
		case BUY -> buyElement(e);
		case USE -> useElement(e);
		case NONE -> {
		}
		}
	}

	private void useElement(ActionEvent e) {
		Button source = (Button) e.getSource();
		game.setSkin(Integer.valueOf(source.getName()));
		view.loadStore(game, this);
		game.saveData();
	}

	private void buyElement(ActionEvent e) {
		Button source = (Button) e.getSource();
		game.addSkin(Integer.valueOf(source.getName()));
		game.discountCoins(Integer.valueOf(source.getName())*StoreDialog.VALUE_INCREMENT);
		view.loadStore(game, this);
		game.saveData();
	}

	private void showMenu() {
		view.stopGame();
		view.changeCard(MainFrame.MENU_CARD);
		game.stop();
		game = new Game(FileManager.loadGameData());
	}

	private void start() {
		view.changeCard(MainFrame.GAME_CARD);
		view.setVisibleLblGameOver(false);
		view.resetCount();
		game.start();
		view.setActualSkin(game.getActualSkin());
		view.refreshGame(game);
	}

	private void showStore() {
		view.showStore(game, this);
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
			view.playChangeGravitySound();
		}
	}

}
