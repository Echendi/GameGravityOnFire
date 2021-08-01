package presenters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JOptionPane;
import buttons.Button;
import models.Game;
import models.GameThread;
import persistence.FileManager;
import views.MainFrame;
import views.StoreDialog;

public class Presenter extends KeyAdapter implements ActionListener {

	private static final int AUTO_SAVE_TIME = 10000;
	private MainFrame view;
	private Game game;
	private GameThread threadAutoSave;

	public Presenter() {
		game = new Game(FileManager.loadGameData());
		view = new MainFrame(this, this);
		view.changeCard(MainFrame.MENU_CARD);
		createAutoSaveThread();
	}

	private void createAutoSaveThread() {
		threadAutoSave = new GameThread(AUTO_SAVE_TIME) {
			int count = 0;

			@Override
			protected void executeTask() {
				if (count != 0 && !game.isOver()) {
					view.screenshot(count++);
				} else {
					count++;
				}
				saveGame();
			}
		};
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Command.valueOf(e.getActionCommand())) {
		case PAUSE -> pause();
		case EXIT -> exit();
		case INSTRUCTIONS -> showInstructions();
		case START -> start();
		case STORE -> showStore();
		case MENU -> showMenu();
		case BUY -> buyElement(e);
		case USE -> useSkin(e);
		case BACK -> back();
		case NEXT -> next();
		case SCREENSHOT -> showScreenshots();
		case SOUND_DISABLE -> soundDisable();
		case SOUND_ENABLE ->soundEnable();
		}
	}

	private void soundEnable() {
		view.startMusic();
	}

	private void soundDisable() {
		view.stopMusic();
	}

	private void showScreenshots() {
		view.showScreenshots(FileManager.getImgsList());
	}

	private void next() {
		view.nextImage();
	}

	private void back() {
		view.backImage();
	}

	private void useSkin(ActionEvent e) {
		Button source = (Button) e.getSource();
		game.setSkin(Integer.valueOf(source.getName()));
		view.loadStore(game, this);
		game.saveData();
	}

	private void buyElement(ActionEvent e) {
		Button source = (Button) e.getSource();
		game.addSkin(Integer.valueOf(source.getName()));
		game.discountCoins(Integer.valueOf(source.getName()) * StoreDialog.VALUE_INCREMENT);
		view.loadStore(game, this);
		game.saveData();
	}

	private void showMenu() {
		saveGame();
		view.stopGame();
		view.changeCard(MainFrame.MENU_CARD);
		game.stop();
		threadAutoSave.pause();
	}

	private void start() {
		initGame();
		view.changeCard(MainFrame.GAME_CARD);
		view.setVisibleLblGameOver(false);
		view.resetCount();
		initAutoSave();
		view.setActualSkin(game.getActualSkin());
		view.showBtnScreenshot(false);
		view.refreshGame(game);
	}

	private void initGame() {
		game = FileManager.loadGame();
		if (game != null) {
			if (!game.isOver() && game.isExecute()) {
				int option = view.showMessageGameSaved(game.getTime());
				if (option == JOptionPane.NO_OPTION) {
					newGame();
				} else {
					game.start(false);
				}
			} else {
				newGame();
			}
		} else {
			newGame();
		}
	}

	private void newGame() {
		FileManager.resetGalleryFolder();
		game = new Game(FileManager.loadGameData());
		game.start(true);
	}

	private void initAutoSave() {
		if (threadAutoSave.isExecute()) {
			threadAutoSave.resume();
		} else {
			threadAutoSave.start();
		}
	}

	private void showStore() {
		view.showStore(game, this);
	}

	private void showInstructions() {
		view.showInstructions();
	}

	private void exit() {
		saveGame();
		view.exit();
	}

	private void saveGame() {
		if (!game.isOver() && game.isExecute()) {
			FileManager.saveGame(game);
		} else if (game.isOver()) {
			game = new Game(FileManager.loadGameData());
			FileManager.saveGame(game);
			threadAutoSave.pause();
		}
	}

	private synchronized void pause() {
		if (game.isPause()) {
			game.resume();
			threadAutoSave.resume();
		} else {
			game.pause();
			threadAutoSave.pause();
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
