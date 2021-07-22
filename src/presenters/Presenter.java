package presenters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import models.Game;
import views.GamePanel;
import views.MainFrame;

public class Presenter extends KeyAdapter implements ActionListener {

	private MainFrame view;
	private Game game;

	public Presenter() {
		this.game = new Game();
		view = new MainFrame(this, this);
		game.start();
		updateUi();
//		try {
//			game.join();
//			FileManager.saveGame(game);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	private void updateUi() {
		final Timer timerUpdate = new Timer(10, e -> {
			view.refreshGame(game);
		});
		timerUpdate.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (Command.valueOf(e.getActionCommand())) {
		case PAUSE -> pause();
		}
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
		if (game.isExecute () && !game.isPause()) {
			game.changeGravity();
		}
	}

}
