package sw4.team2.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import sw4.team2.common.Cocktail;
import sw4.team2.common.Item;
import sw4.team2.common.RequestMessage;

public class QuizForm extends JFrame {
	private JPanel mainPanel;
	private String userId;
	
	// ========================= Intro Panel =========================
	private JPanel introPanel;
	private boolean itemInfoDownloaded = false;
	private Map<Integer, Item> itemInfoMap = null;
	
	// ========================= Select Panel =========================
	private JPanel selectPanel;
	
	// ========================= Quiz Panel =========================
	private JPanel examPanel, quizPanel, rightPanel;
	
	private boolean cocktailInfoDownloaded = false, wanInfoDownloaded = false;
	private Map<String, Cocktail> cocktailMap = null;
	private int presentCocktailIndex = 0;
	
	private JPanel topPanel, cocktailNamePanel, timePanel;
	private List<Cocktail> cocktailList = new ArrayList<>();
	private List<JLabel> cocktailLabelList = new ArrayList<>();
	// TODO add List<CocktailNamePanel>
	
	private JPanel leftPanel, itemPanel;
	private ItemButton currentSelectedBtn = null;
	private List<ItemButton> selectedBtnList = new ArrayList<>();
	private List<ItemButton> itemBtnList = new ArrayList<>();
	private int selectedItemIndex = 0;
	
	private int topPadding = 11, bottomMarginPdding = 11, leftPadding = 71, rightPadding = 71;
	private int gap = 10;
	private int itemBtnSize = 74;
	private int x = leftPadding, y;
	
	private Color pinkColor = new Color(255, 202, 219);
	
	public QuizForm(String uId) {
		this.userId = uId;
		
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		this.setContentPane(mainPanel);
		
		initIntroPanel();
		initSelectPanel();
		// TODO init examPanel
		initExamPanel();
		
		
//		selectDisplay();
//		quizDisplay();
//		event();
		
		showIntroPanel();
		
		this.setBounds(0, 0, 1600, 900);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setVisible(true);
	}

	private void showExamPanel() {
		mainPanel.removeAll();
		
		presentCocktailIndex = 0;
		// TODO create CocktailNamePanel Class and allocation
//		for (String key : cocktailQuiz.keySet()) {
//			JLabel label = new JLabel(key);
//			label.setSize(160, 160);
//			cocktailNamePanel.add(comp)
//		}
		
		mainPanel.add(examPanel);
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	private void initExamPanel() {
		examPanel = new JPanel();
		examPanel.setBounds(0, 0, 1600, 900);
		examPanel.setBackground(Color.WHITE);
		examPanel.setLayout(null);
		
		initTopPanel();
		initLeftPanel();
		initRightPanel();
		initQuizPanel();
	}

	private void initQuizPanel() {
		quizPanel = new JPanel();
		quizPanel.setBounds(300, 200, 1000, 700);
		quizPanel.setBackground(Color.GREEN);
		examPanel.add(quizPanel);
	}

	private void initRightPanel() {
		rightPanel = new JPanel();
		rightPanel.setBounds(1300, 200, 300, 700);
		rightPanel.setBackground(Color.CYAN);
		examPanel.add(rightPanel);
	}

	private void initTopPanel() {
		topPanel = new JPanel();
		topPanel.setLayout(null);
		topPanel.setBounds(0, 0, 1600, 200);
		topPanel.setBackground(Color.WHITE);
		examPanel.add(topPanel);
		
		JLabel logoLabel = new JLabel();
		logoLabel.setOpaque(true);
		logoLabel.setBounds(0, 0, 300, 200);
		logoLabel.setIcon(ImageResizer.fit(logoLabel, new ImageIcon("img/logo.png")));
		topPanel.add(logoLabel);
		
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.CENTER);
		fl.setVgap(0);
		fl.setHgap(30);
		
		cocktailNamePanel = new JPanel();
		cocktailNamePanel.setLayout(fl);
		cocktailNamePanel.setBounds(300, 20, 1000, 160);
		cocktailNamePanel.setBackground(Color.CYAN);
		topPanel.add(cocktailNamePanel);
		
		timePanel = new JPanel();
		timePanel.setBounds(1300, 0, 300, 200);
		timePanel.setBackground(Color.PINK);
		topPanel.add(timePanel);
	}

	private void initLeftPanel() {
		leftPanel = new JPanel();
		leftPanel.setBounds(0, 200, 300, 700);
		leftPanel.setLayout(null);
		leftPanel.setBackground(pinkColor);
		examPanel.add(leftPanel);
		
		itemPanel = new JPanel();
		itemPanel.setLayout(null);
		itemPanel.setSize(300, 600);
		itemPanel.setLocation(0, 100);
		itemPanel.setBackground(pinkColor);
		leftPanel.add(itemPanel);
		
		displaySelectedItemBtn();
	}

	private void showIntroPanel() {
		mainPanel.removeAll();
		
		mainPanel.add(introPanel);
		
		mainPanel.revalidate();
		mainPanel.repaint();
		
		ItemInfoDownloadThread t = new ItemInfoDownloadThread();
		t.setDaemon(true);
		t.start();
		
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				for (int i = 0; i < 3; i++) {
					try {
						Thread.sleep(1500);
						if (itemInfoDownloaded)
							break;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				
				if (itemInfoDownloaded) {
					showSelectPanel();
				} else {
					JOptionPane.showConfirmDialog(introPanel, "Fail to download item information",	"Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
			}
		};
		
		timer.schedule(task, 0);
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
	
	private void showSelectPanel() {
		mainPanel.removeAll();
		
		mainPanel.add(selectPanel);
		
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	private void initSelectPanel() {
		selectPanel = new JPanel();
		selectPanel.setBounds(0, 0, 1600, 900);
		selectPanel.setLayout(null);
		
		JButton practiceBtn = new JButton("연습모드");
		practiceBtn.setBounds(150, 200, 400, 500);
		selectPanel.add(practiceBtn);
		
		JButton wanBtn = new JButton("오답노트");
		wanBtn.setBounds(150 + 450, 200, 400, 500);
		selectPanel.add(wanBtn);
		
		JButton examBtn = new JButton("연습모드");
		examBtn.setBounds(150 + 900, 200, 400, 500);
		selectPanel.add(examBtn);
		
		JButton exitBtn = new JButton("종료하기");
		exitBtn.setBounds(1300, 775, 250, 75);
		selectPanel.add(exitBtn);
		
		// TODO Add ActionListener
		practiceBtn.addActionListener(e->{showExamPanel();});
		exitBtn.addActionListener(e->{System.exit(0);});
		
	}
	
	private void displaySelectedItemBtn() {
		itemPanel.removeAll();
		itemPanel.revalidate();
		itemBtnList.clear();
		
		for (int i = 0; i < selectedBtnList.size(); i++) {
			itemBtnList.add(selectedBtnList.get(i));
		}
		
		for (int i = selectedBtnList.size(); i < 14; i++) {
			itemBtnList.add(ItemButton.emptyBtn());
		}
		
		for (int i = 0; i < itemBtnList.size(); i++) {
			if (i % 2 == 1)
				x = leftPadding + gap + itemBtnSize;
			else
				x = leftPadding;
			y = topPadding + (i / 2) * (gap + itemBtnSize); 
			itemBtnList.get(i).setLocation(x, y);
			
			itemPanel.add(itemBtnList.get(i));
		}
		
		itemPanel.revalidate();
	}
	
	class ItemInfoDownloadThread extends Thread {
		@Override
		public void run() {
			try {
				Socket sock = new Socket(InetAddress.getByName("kbetter3.iptime.org"), 28130);
				ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
				itemInfoMap = (Map<Integer, Item>) ois.readObject();
				
				System.out.println("iteminfo Size : " + itemInfoMap.size());
				
				itemInfoDownloaded = true;
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	// TODO Create WAN Thread & Request Quiz Method
	class RequestQuizThread extends Thread {
		private RequestMessage msg;
		@Override
		public void run() {
			try {
				Socket sock = new Socket(InetAddress.getByName("kbetter3.iptime.org"), 28131);
				ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
				oos.writeObject(msg);
				oos.flush();
				
				ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
				cocktailMap = (Map<String, Cocktail>) ois.readObject();
				cocktailInfoDownloaded = true;
				
				System.out.println("cocktailMap Size : " + cocktailMap.size());
				
				showExamPanel();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		
		public RequestQuizThread(RequestMessage msg) {
			this.msg = msg;
		}
	}
}
