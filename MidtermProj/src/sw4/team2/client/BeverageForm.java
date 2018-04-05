package sw4.team2.client;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class BeverageForm extends JFrame {
	private String userId;
	private Image img;
	private Image[] imgs = new Image[3];
	private JPanel mainPanel, bottomPanel;
	private JButton[] btn = new JButton[4];

	private ActionListener listener = e->{
		if (e.getSource() == btn[btn.length - 1]) {
			SelectForm sf = new SelectForm(userId);
			BeverageForm.this.dispose();
		}
		for (int i = 0; i < btn.length - 1; i++) {
			if (i < btn.length - 1 && btn[i] == e.getSource()) {
				img = imgs[i];
				RepaintThread t = new RepaintThread();
				t.setDaemon(true);
				t.start();
				break;
			}
		}

	};

	public BeverageForm(String userId) {
		this.userId = userId;

		bottomPanel = new JPanel();
		bottomPanel.setBounds(0, 840, 1600, 60);
		bottomPanel.setLayout(null);

		for (int i = 0; i < imgs.length; i++) {
			imgs[i] = Toolkit.getDefaultToolkit().getImage("img/beverageIcon" + i + ".png");
		}
		
		int x = 80, btnWidth = 300;
		for (int i = 0; i < btn.length; i++) {
			btn[i] = new JButton();
			btn[i].setText(i + "");
			btn[i].setBounds((i + 1) * x + (i * btnWidth), 0, btnWidth, 60);
			btn[i].addActionListener(listener);
			bottomPanel.add(btn[i]);
		}
		img = imgs[0];
		
		mainPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(img, 0, 0, 1600, 840, this);
			}
		};
		mainPanel.setLayout(null);
		mainPanel.setBounds(0, 0, 1600, 900);
		this.setContentPane(mainPanel);
		
		mainPanel.add(bottomPanel);
		
		this.setBounds(0, 0, 1600, 900);
		this.setUndecorated(true);
		this.setVisible(true);
	}


	class RepaintThread extends Thread {
		@Override
		public void run() {
			mainPanel.repaint();
		}
	}
}
