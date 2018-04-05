package sw4.team2.client;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
	private JButton practiceBtn, wanBtn, examBtn, beverBtn, exitBtn;
	
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
		MouseListener listener=new MouseAdapter() {
			public void  mouseReleased(MouseEvent event) {
				if(event.getSource()==practiceBtn) {
					practiceBtn.setIcon( new ImageIcon("img/PracticeImg.png"));
				}
				else if(event.getSource()==wanBtn) {
					wanBtn.setIcon(new ImageIcon("img/wanBtnImg.png"));
				}
				else if(event.getSource()==examBtn) {
					examBtn.setIcon(new ImageIcon("img/examImg.png"));
				}
				else if(event.getSource()==beverBtn) {
					beverBtn.setIcon(new ImageIcon("img/BeverBtn.png"));
				}
				else if(event.getSource()==exitBtn) {
					exitBtn.setIcon(new ImageIcon("img/exitBtn3.png"));
				}
			}
			
			public void mousePressed(MouseEvent event) {
				if(event.getSource()==practiceBtn) {
					practiceBtn.setIcon(new ImageIcon("img/PracticeImg1.png"));
				}
				else if(event.getSource()==wanBtn) {
					wanBtn.setIcon(new ImageIcon("img/wanBtnImg1.png"));
				}
				else if(event.getSource()==examBtn) {
					examBtn.setIcon(new ImageIcon("img/examImg1.png"));
				}
				else if(event.getSource()==beverBtn) {
					beverBtn.setIcon(new ImageIcon("img/BeverBtn1.png"));
				}
				else if(event.getSource()==exitBtn) {
					exitBtn.setIcon(new ImageIcon("img/exitBtn4.png"));
				}
			}
		};
		practiceBtn.addMouseListener(listener);
		wanBtn.addMouseListener(listener);
		examBtn.addMouseListener(listener);
		beverBtn.addMouseListener(listener);
		exitBtn.addMouseListener(listener);
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
		
		practiceBtn = new JButton("");
		practiceBtn.setBounds(150, 200,400, 250);
		practiceBtn.setBorder(null);
		practiceBtn.setContentAreaFilled(false);
		practiceBtn.setIcon(new ImageIcon("img/PracticeImg.png"));
		practiceBtn.addActionListener(e->{
			RecepieForm recf = new RecepieForm(SelectForm.this, true);
		});
		selectPanel.add(practiceBtn);
		
		beverBtn = new JButton("");
		beverBtn.setBorder(null);
		beverBtn.setContentAreaFilled(false);
		beverBtn.setIcon(new ImageIcon("img/BeverBtn.png"));
		beverBtn.setBounds(150,200+250,400,250);
		beverBtn.addActionListener(e->{
			BeverageForm bf = new BeverageForm(SelectForm.this.getUserId());
			SelectForm.this.dispose();
		});
		selectPanel.add(beverBtn);
		
		wanBtn = new JButton("");
		wanBtn.setBorder(null);
		wanBtn.setContentAreaFilled(false);
		wanBtn.setBounds(150 + 450, 200, 400, 500);
		wanBtn.setIcon(new ImageIcon("img/wanBtnImg.png"));
		wanBtn.addActionListener(e->{
			try {
				WANote wanote = new WANote(SelectForm.this, true);
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			});
		selectPanel.add(wanBtn);
		
		examBtn = new JButton("");
		examBtn.setBorder(null);
		examBtn.setContentAreaFilled(false);
		examBtn.setBounds(150 + 900, 200, 400, 500);
		examBtn.setIcon(new ImageIcon("img/examImg.png"));
		examBtn.addActionListener(e->{
			QuizForm qf = new QuizForm(SelectForm.this.getUserId(), QuizForm.MODE_EXAM, null);
			SelectForm.this.dispose();
		});
		selectPanel.add(examBtn);
		
		exitBtn = new JButton("");
		exitBtn.setBorder(null);
		exitBtn.setContentAreaFilled(false);
		exitBtn.setIcon(new ImageIcon("img/exitBtn3.png"));
		exitBtn.setBounds(1300, 775, 250, 75);
		exitBtn.addActionListener(e->{System.exit(0);});
		selectPanel.add(exitBtn);
	}
	
}
