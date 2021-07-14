package presenters;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import views.MainFrame;

public class Presenter implements ActionListener {

	private MainFrame view;

	public Presenter() {
		view = new MainFrame(this);
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

}
