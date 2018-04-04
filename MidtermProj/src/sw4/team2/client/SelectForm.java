package sw4.team2.client;

import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SelectForm extends JFrame {
	private String userID;
	private JPanel mainPanel;
	
	private JPanel introPanel;
	
	private JPanel selectPanel;
	private JButton practiceBtn, wanBtn, examBtn;
	
	public SelectForm(String userID) {
		this.userID = userID;
		
		
		init();
		event();
		
		this.setBounds(0, 0, 1600, 900);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setVisible(true);
		
	}

	private void event() {
		
	}

	private void init() {
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		this.setContentPane(mainPanel);
		
		initIntroPanel();
		initSelectPanel();
		
		showIntroPanel();
	}
	
	private void showIntroPanel() {
		mainPanel.add(introPanel);
		
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				changePanel(selectPanel);
			}
		};
		
		timer.schedule(task, 3000);
	}
	
	private void initIntroPanel() {
		introPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(new ImageIcon("img/intro.gif").getImage(), 0, 0, 1600, 900, this);
			}
		};
		introPanel.setBounds(0, 0, 1600, 900);
		introPanel.setLayout(null);
	}
	
	private void changePanel(JPanel panel) {
		mainPanel.removeAll();
		mainPanel.add(panel);
		mainPanel.revalidate();
		mainPanel.repaint();
	}
	
	public String getUserId() {
		return this.userID;
	}
	
//	================================== select =================================
	private void initSelectPanel() {
		selectPanel = new JPanel();
		selectPanel.setLayout(null);
		selectPanel.setBounds(0, 0, 1600, 900);
		
		JButton practiceBtn = new JButton("연습모드");
		practiceBtn.setBounds(150, 200, 400, 500);
		selectPanel.add(practiceBtn);
		
		JButton wanBtn = new JButton("오답노트");
		wanBtn.setBounds(150 + 450, 200, 400, 500);
		selectPanel.add(wanBtn);
		
		JButton examBtn = new JButton("실전모드");
		examBtn.setBounds(150 + 900, 200, 400, 500);
		examBtn.addActionListener(e->{
			QuizForm qf = new QuizForm(SelectForm.this.getUserId(), QuizForm.MODE_EXAM, null);
//			qf.setVisible(true);
			SelectForm.this.dispose();
		});
		selectPanel.add(examBtn);
		
		JButton exitBtn = new JButton("종료하기");
		exitBtn.setBounds(1300, 775, 250, 75);
		exitBtn.addActionListener(e->{System.exit(0);});
		selectPanel.add(exitBtn);
	}
}
