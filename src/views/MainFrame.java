package views;

import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private MainPanel panel;

	public MainFrame(ActionListener listener) {
		super("");
		initComponents(listener);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.setSize(400, 200);
//		this.setIconImage(new ImageIcon("").getImage());
		this.setVisible(true);
	}

	private void initComponents(ActionListener listener) {
		panel = new MainPanel(listener);

		this.getContentPane().add(panel);

	}

}
