package sw4.team2.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class QuizForm extends JFrame {
	private JPanel mainPanel, topPanel, leftPanel, quizPanel, rightPanel;
	
	private JPanel itemPanel;
	private ItemButton currentSelectedBtn = null;
	private List<ItemButton> selectedBtnList = new ArrayList<>();
	private List<ItemButton> itemBtnList = new ArrayList<>();
	private int selectedItemIndex = 0;
	
	private Color pink = new Color(255, 202, 219);
	
	public QuizForm() {
		display();
		event();
		
		this.setBounds(0, 0, 1600, 900);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setVisible(true);
	}

	private void event() {
		ActionListener changeSelectedItemListener = (e)->{
			
		};
	}

	private void display() {
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		mainPanel.setBounds(0, 0, 1600, 900);
		this.setContentPane(mainPanel);
		
		topPanel = new JPanel();
		topPanel.setLayout(null);
		topPanel.setBounds(0, 0, 1600, 200);
		topPanel.setBackground(Color.BLACK);
		mainPanel.add(topPanel);
		
		GridLayout gl = new GridLayout(7, 2);
		gl.setHgap(30);
		
		
		displayLeftPanel();

		mainPanel.add(leftPanel);
		
		quizPanel = new JPanel();
		quizPanel.setLayout(null);
		quizPanel.setBounds(300, 200, 1000, 700);
		quizPanel.setBackground(Color.GREEN);
		mainPanel.add(quizPanel);
		
		rightPanel = new JPanel();
		rightPanel.setLayout(gl);
		rightPanel.setBounds(1300, 200, 300, 700);
		rightPanel.setBackground(Color.BLUE);
		mainPanel.add(rightPanel);
	}

	private void displayLeftPanel() {
		leftPanel = new JPanel();
		leftPanel.setBounds(0, 200, 300, 700);
		leftPanel.setLayout(null);
		leftPanel.setBackground(pink);
		
		itemPanel = new JPanel();
		itemPanel.setLayout(null);
		itemPanel.setSize(300, 600);
		itemPanel.setLocation(0, 100);
		itemPanel.setBackground(pink);
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
	
}
