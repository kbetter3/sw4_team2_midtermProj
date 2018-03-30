package sw4.team2.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
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
import java.util.List;

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

public class QuizForm extends JFrame {
	private JPanel mainPanel;
	private String userId;
	
	// ========================= Intro Panel =========================
	private JPanel introPanel;
	private boolean itemInfoDownloaded = false;
	
	// ========================= Select Panel =========================
	private JPanel selectPanel;
	
	// ========================= Quiz Panel =========================
	private JPanel quizPanel, rightPanel;
	
	private JPanel topPanel, cocktailNamePanel, timePanel;
	private List<Cocktail> cocktailList = new ArrayList<>();
	private List<JLabel> cocktailLabelList = new ArrayList<>();
	private int presentCocktailIndex = 0;
	
	private JPanel leftPanel, itemPanel;
	private ItemButton currentSelectedBtn = null;
	private List<ItemButton> selectedBtnList = new ArrayList<>();
	private List<ItemButton> itemBtnList = new ArrayList<>();
	private int selectedItemIndex = 0;
	
	private Color pinkColor = new Color(255, 202, 219);
	
	public QuizForm(String uId) {
		this.userId = uId;
		
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		this.setContentPane(mainPanel);
		
		initIntroPanel();
		initSelectPanel();
		
		
		// TODO init QuizPanel
//		selectDisplay();
//		quizDisplay();
//		event();
		
		showIntroPanel();
		
		this.setBounds(0, 0, 1600, 900);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setVisible(true);
	}

	private void showIntroPanel() {
		mainPanel.removeAll();
		
		mainPanel.add(introPanel);
		
		mainPanel.revalidate();
		mainPanel.repaint();
		
		/*
		ItemInfoDownloadThread t = new ItemInfoDownloadThread();
		t.setDaemon(true);
		t.start();
		
		for (int i = 0; i < 3; i++) {
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (itemInfoDownloaded)
				break;
		}
		
		if (itemInfoDownloaded) {
			showSelectPanel();
		} else {
			JOptionPane.showConfirmDialog(introPanel, "Fail to download item information",	"Error", JOptionPane.CLOSED_OPTION, JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		*/
	}
	
	private void initIntroPanel() {
		introPanel = new JPanel();
		introPanel.setBackground(Color.LIGHT_GRAY);
		introPanel.setBounds(0, 0, 1600, 900);
		introPanel.setLayout(null);
		
		// TODO intro image
		/*
		JLabel introIconLabel = new JLabel();
		introIconLabel.setIcon(new ImageIcon("filename"));
		introPanel.add(introIconLabel);
		*/
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
	}

	private void quizDisplay() {
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBounds(0, 0, 1600, 900);
		this.setContentPane(mainPanel);
		
		displayTopPanel();
		displayLeftPanel();

		mainPanel.add(leftPanel);
		
		quizPanel = new JPanel();
		quizPanel.setLayout(null);
		quizPanel.setBounds(300, 200, 1000, 700);
		quizPanel.setBackground(Color.GREEN);
		mainPanel.add(quizPanel);
		
		rightPanel = new JPanel();
		rightPanel.setLayout(null);
		rightPanel.setBounds(1300, 200, 300, 700);
		rightPanel.setBackground(Color.BLUE);
		mainPanel.add(rightPanel);
	}

	private void displayTopPanel() {
		topPanel = new JPanel();
		topPanel.setLayout(null);
		topPanel.setBounds(0, 0, 1600, 200);
		topPanel.setBackground(Color.WHITE);
		mainPanel.add(topPanel);
		
		JLabel logoLabel = new JLabel();
		logoLabel.setOpaque(true);
		logoLabel.setBounds(0, 0, 300, 200);
		logoLabel.setBackground(Color.GRAY);
//		logoLabel.setIcon(ImageResizer.fit(logoLabel, new ImageIcon("filename")));
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
		
		displayCocktailNamePanel();
		
		displayTimePanel();
	}

	private void displayTimePanel() {
		timePanel = new JPanel();
		timePanel.setBounds(1300, 0, 300, 200);
		timePanel.setBackground(Color.PINK);
		topPanel.add(timePanel);
	}

	private void displayCocktailNamePanel() {
		cocktailNamePanel.removeAll();
		
		for (int i = 0; i < cocktailList.size(); i++) {
			JLabel cocktail = new JLabel("cocktail " + (i + 1));
			cocktail.setPreferredSize(new Dimension(160, 160));
			cocktail.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3, true));
			cocktailNamePanel.add(cocktail);
			cocktailLabelList.add(cocktail);
		}
		
		cocktailNamePanel.revalidate();
		cocktailNamePanel.repaint();
	}

	private void displayLeftPanel() {
		leftPanel = new JPanel();
		leftPanel.setBounds(0, 200, 300, 700);
		leftPanel.setLayout(null);
		leftPanel.setBackground(pinkColor);
		
		itemPanel = new JPanel();
		itemPanel.setLayout(null);
		itemPanel.setSize(300, 600);
		itemPanel.setLocation(0, 100);
		itemPanel.setBackground(pinkColor);
		leftPanel.add(itemPanel);
		
		displaySelectedItemBtn();
		
		ItemButton testBtn = new ItemButton(new ImageIcon("img/GREEN CREAMED MINT.png"), 74, 74);
		selectedBtnList.add(testBtn);
		displaySelectedItemBtn();
	}
	
	private void displaySelectedItemBtn() {
		int topPadding = 11, bottomMarginPdding = 11, leftPadding = 71, rightPadding = 71;
		int gap = 10;
		int btnSize = 74;
		int x = leftPadding, y;
		
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
				x = leftPadding + gap + btnSize;
			else
				x = leftPadding;
			y = topPadding + (i / 2) * (gap + btnSize); 
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
				ois.readObject();
				
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
}
