package presenters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import models.Game;
import views.MainFrame;

public class Presenter extends KeyAdapter implements ActionListener {

	private MainFrame view;
	private Game game;

	public Presenter() {
		this.game = new Game();
		view = new MainFrame(this, this);
		updateUi();
	}

	private void updateUi() {
		final Timer timerUpdate = new Timer(10, e -> {
			view.refreshGame(game);
		});
		timerUpdate.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "":

			break;

		default:
			break;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getExtendedKeyCode();
		switch (code) {
		case KeyEvent.VK_SPACE -> changeGravity();
		}
	}

	private void changeGravity() {
		if (Game.isPlay()) {
			game.changeGravity();
		}
	}

}
