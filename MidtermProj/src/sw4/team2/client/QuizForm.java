package sw4.team2.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import sw4.team2.common.Cocktail;

public class QuizForm extends JFrame {
	private JPanel testPanel;
	private JPanel mainPanel, quizPanel, rightPanel;
	
	private JPanel topPanel, cocktailNamePanel, timePanel;
	private List<Cocktail> cocktailList = new ArrayList<>();
	private List<JLabel> cocktailLabelList = new ArrayList<>();
	private int presentCocktailIndex = 0;
	
	private JPanel leftPanel, itemPanel;
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
		
		
		
		GridLayout gl = new GridLayout(7, 2);
		gl.setHgap(30);
		
		displayTopPanel();
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
		cocktailNamePanel.revalidate();
		
		for (int i = 0; i < cocktailList.size(); i++) {
			JLabel cocktail = new JLabel("cocktail " + (i + 1));
			cocktail.setPreferredSize(new Dimension(160, 160));
			cocktail.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3, true));
			cocktailNamePanel.add(cocktail);
			cocktailLabelList.add(cocktail);
		}
		
		cocktailNamePanel.revalidate();
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
			/*
			selectedBtnList.get(i).addActionListener(e->{
				testPanel = new JPanel();
				testPanel.setBounds(0, 0, 1600, 900);
				testPanel.setLayout(null);
				testPanel.setBackground(Color.GREEN);
				KeyStroke enterKs = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
				Action enterAction = new AbstractAction() {
					@Override
					public void actionPerformed(ActionEvent e) {
						QuizForm.this.getContentPane().removeAll();
						QuizForm.this.display();
						QuizForm.this.getContentPane().revalidate();
						QuizForm.this.getContentPane().repaint();
						System.out.println("enter");
					}
				};
				
				testPanel.getInputMap().put(enterKs, "enter");
				testPanel.getActionMap().put("enter", enterAction);
				
				this.getContentPane().removeAll();
				this.getContentPane().add(testPanel);
				this.getContentPane().revalidate();
				this.getContentPane().repaint();
			});
			*/
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
