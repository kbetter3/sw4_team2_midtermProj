package sw4.team2.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import sw4.team2.common.Cocktail;
import sw4.team2.common.HostReader;
import sw4.team2.common.Item;
import sw4.team2.common.Recipe;
import sw4.team2.common.RequestMessage;

public class QuizForm extends JFrame {
	private String userId;
	private int mode;
	private JPanel mainPanel;
	private Cocktail selectedCocktail;

	//   ========================= Progress =========================
	private int currentCocktail = 0;
	private int currentChapter;   // 재료선택창의 챕터
	private int currentType;   // 선택된 아이템버튼의 아이템 타입
	private boolean selectMethod;

	//   ========================= Intro =========================
	private JPanel introPanel;

	//   ========================= Cocktail =========================
	private JPanel cocktailPanel;

	//   ========================= Top =========================
	private JPanel topPanel;
	private JPanel cocktailNamePanel;
	private JLabel logoLabel;
	private JLabel timerLabel;
	//   private JPanel timerPanel;
	private List<Cocktail> cocktailNameList;
	private List<JButton> cocktailLabelBtnList;

	//   ========================= Left =========================
	private JPanel leftPanel;
	private JPanel itemPanel;
	private List<ItemButton> selectedItemBtnList;

	//   ========================= Right =========================
	private JPanel rightPanel;
	private JPanel recipePanel;
	private List<RecipeButton> recipeBtnList;
	private JButton exitBtn;

	//   ========================= Quiz =========================
	private JPanel quizPanel;
	private JPanel startQuizPanel;
	private JPanel selectItemPanel;
	private JPanel contentPanel;
	private JPanel bottomBtnPanel;
	private JButton mixingBtn;

	private JLabel itemLabel;
	private JPanel mixingPanel;
	private JButton selectMixBtn;
	private JButton submitBtn;

	private String selectedAnswer;
	private String selectedItemName = "METHOD";

	private ActionListener removeFromSelectedItemBtnList = e->{
		selectedItemBtnList.remove(e.getSource());
		displaySelectedItemBtn();
		if (selectedItemBtnList.size() == 0)
			mixingBtn.setEnabled(false);
	};

	private ActionListener showMixingMenu = (e)->{
		if (currentType < 9) {
			currentType = ((ItemButton)e.getSource()).getType();
			selectedItemName = e.getActionCommand();
			initMixingPanel();
			changeQuizPanel(mixingPanel);
		}
	};
	//   ========================= result =========================
	private JPanel resultPanel;
	private JPanel resultCocktailPanel;

	//   ========================= Info =========================
	private boolean itemInfoDownloaded = false;
	private boolean cocktailInfoDownloaded = false;
	private Map<Integer, List<Item>> itemInfoMap = null;
	private Map<String, Cocktail> cocktailMap = null;
	private Map<Integer, List<String>> answerMap = null;
	private Map<String, Cocktail> userAnswerMap = null;

	private List<Item> glassItemList, alcoholicItemList, nonAlcoholicItemList, addAlcoholicItemList, beverageItemList, garnishesItemList, additionalItemList, toolItemList;
	//   ========================= Ect =========================
	private int time;
	public static final int MODE_PRACTICE = 1;
	public static final int MODE_WAN = 1;
	public static final int MODE_EXAM = 3;

	private Color pinkColor = new Color(255, 202, 219);

	public QuizForm(String userID, int mode, Cocktail selectedCocktail) {
		this.userId = userID;
		System.out.println("QuizForm : " + userId);
		this.selectedCocktail = selectedCocktail;
		this.mode = mode;

		loadAnswer();
		loadItemInfo();
		loadCocktailInfo();

		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		this.setContentPane(mainPanel);
		initIntroPanel();

		this.setBounds(0, 0, 1600, 900);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setUndecorated(true);
		this.setVisible(true);
	}


	private void loadAnswer() {
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(new FileInputStream(new File("files/answer.db")));
			answerMap = (Map<Integer, List<String>>) ois.readObject();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}


	private void loadItemInfo() {
		ItemInfoDownloadThread t = new ItemInfoDownloadThread();
		t.setDaemon(true);
		t.start();
	}


	private void loadCocktailInfo() {
		Map<String, Cocktail> cMap = new HashMap<>();
		//      
		//      if (selectedCocktail != null)
		//         cocktailSet.add(selectedCocktail.getName());
		RequestMessage msg = new RequestMessage(RequestMessage.TYPE_REQUEST, (mode == MODE_EXAM ? RequestMessage.MODE_EXAM : RequestMessage.MODE_PRACTICE), userId, null);
		RequestQuizThread t = new RequestQuizThread(msg);
		t.setDaemon(true);
		t.start();
	}


	private void init() {
		cocktailPanel = new JPanel();
		cocktailPanel.setBounds(0, 0, 1600, 900);
		cocktailPanel.setLayout(null);
		initTopPanel();
		initLeftPanel();
		initRightPanel();
		initQuizPanel();
		userAnswerMap = new HashMap<>();
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
		mainPanel.add(introPanel);
	}


	private void initQuizPanel() {
		quizPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(Toolkit.getDefaultToolkit().getImage("img/quiz.png"), 0, 0, 1000, 690, this);
			}
		};
		quizPanel.setBounds(300, 190, 1000, 710);
		quizPanel.setOpaque(false);
		quizPanel.setLayout(null);

		initStartQuizPanel();
		changeQuizPanel(startQuizPanel);

		cocktailPanel.add(quizPanel);
	}

	private void enableMixingBtn() {
		mixingBtn.setEnabled(true);
	}

	private void initAnswerRadioButton(JRadioButton btn, int width, int height) {
		// TODO
		btn.setSize(width, height);
		btn.setPreferredSize(btn.getSize());
		btn.setBorder(null);
		btn.setOpaque(false);
		btn.addItemListener(e->{
			selectMixBtn.setEnabled(true);
			selectedAnswer = ((JRadioButton)e.getSource()).getText();
		});
	}

	private void initMixingPanel() {
		mixingPanel = new JPanel();
		mixingPanel.setLayout(null);
		mixingPanel.setBounds(0, 0, 1000, 690);
		mixingPanel.setOpaque(false);

		// TODO
		contentPanel = new JPanel();
		contentPanel.setBounds(0, 130, 1000, 500);
		contentPanel.setPreferredSize(contentPanel.getSize());
		contentPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 0));
		contentPanel.setBackground(new Color(0, 0, 0, 0));
		contentPanel.setOpaque(false);

		itemLabel = new JLabel();
		itemLabel.setBounds(0, 0, 1000, 130);
		itemLabel.setVerticalAlignment(JLabel.CENTER);
		itemLabel.setHorizontalAlignment(JLabel.CENTER);
		itemLabel.setFont(new Font("", Font.PLAIN, 80));
		if (currentType == -1)
			itemLabel.setText("도구를 선택하세요");
		else
			itemLabel.setText(selectedItemName);
		mixingPanel.add(itemLabel);

		JRadioButton[] answers;
		if (currentType == -1) {
			answers = new JRadioButton[1];
		} else {
			answers = new JRadioButton[answerMap.get(currentType).size()];
		}
		ButtonGroup group = new ButtonGroup();
		int width = 0, height = 0;

		switch (currentType) {
		case Item.TYPE_BEVERAGE:
			width = 180;
			height = 500 / 4;
			break;

		default:
			width = 800;
			height = 500 / answers.length;
		}

		if (currentType != -1) {
			for (int i = 0; i < answers.length; i++) {
				answers[i] = new JRadioButton(answerMap.get(currentType).get(i));
				initAnswerRadioButton(answers[i], width, height);
				group.add(answers[i]);
				contentPanel.add(answers[i]);
			}
		}
		mixingPanel.add(contentPanel);

		bottomBtnPanel = new JPanel();
		bottomBtnPanel.setBounds(0, 630, 1000, 50);
		bottomBtnPanel.setLayout(null);
		bottomBtnPanel.setOpaque(false);

		// 조주하기
		selectMixBtn = new JButton();
		selectMixBtn.setBounds(150, 0, 300, 50);
		selectMixBtn.setBorder(null);
		selectMixBtn.setContentAreaFilled(false);
		selectMixBtn.setIcon(new ImageIcon("img/selectMixNormal.png"));
		selectMixBtn.setEnabled(false);
		selectMixBtn.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				selectMixBtn.setIcon(new ImageIcon("img/selectMixPressed.png"));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				selectMixBtn.setIcon(new ImageIcon("img/selectMixNormal.png"));
			}
		});
		selectMixBtn.addActionListener(e->{
			Item item = null;

			if (currentType < 9) {
				for (Item i : itemInfoMap.get(currentType)) {
					if (i.getEngName().equals(selectedItemName)) {
						item = i;
						break;
					}
				}
				if (item == null)
					System.out.println("item is null");
			} else {
				item = new Item(e.getActionCommand(), "METHOD", Item.SECTION_NONE, null, Item.TYPE_METHOD);
			}

			Recipe recipe = new Recipe(item, false, selectedAnswer);
			RecipeButton rBtn = new RecipeButton(new ImageIcon("img/item/" + item.getEngName() + ".png"), 74, 74, currentType, recipe);
			rBtn.setToolTipText(recipe.getAnswer());
			recipeBtnList.add(rBtn);
			displayRecipeBtn();



			currentType = -1;
			initMixingPanel();
			changeQuizPanel(mixingPanel);

		});
		bottomBtnPanel.add(selectMixBtn);

		// 제출하기
		submitBtn = new JButton();
		submitBtn.setBounds(550, 0, 300, 50);
		submitBtn.setBorder(null);
		submitBtn.setContentAreaFilled(false);
		submitBtn.setIcon(new ImageIcon("img/submitNormal.png"));
		submitBtn.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				submitBtn.setIcon(new ImageIcon("img/submitPressed.png"));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				submitBtn.setIcon(new ImageIcon("img/submitNormal.png"));
			}
		});
		submitBtn.addActionListener((event)->{
			if (recipeBtnList.size() > 0) {
				List<Recipe> userRecipe = new ArrayList<>();
				for (RecipeButton rBtn : recipeBtnList) {
					userRecipe.add(rBtn.getRecipe());
				}

				userAnswerMap.put(cocktailLabelBtnList.get(currentCocktail).getText(), new Cocktail(cocktailLabelBtnList.get(currentCocktail).getText(), userRecipe));

				selectedItemBtnList.clear();
				recipeBtnList.clear();

				displaySelectedItemBtn();
				displayRecipeBtn();

				currentCocktail++;

				System.out.println(currentCocktail + " / " + cocktailMap.size());

				if (currentCocktail == cocktailMap.size()) {
					// TODO send userAnswerMap;
					// TODO show Result Panel
					System.out.println("send");
					initResultPanle();
				} else {
					for (int i = 0; i < cocktailLabelBtnList.size(); i++) {
						if (i == currentCocktail) {
							System.out.println(cocktailLabelBtnList.get(i).getText());
							cocktailLabelBtnList.get(i).setIcon(ImageResizer.fit(cocktailLabelBtnList.get(i), new ImageIcon("img/currentLabel.png"))/*new ImageIcon("img/currentLabel.png")*/);
						} else {
							cocktailLabelBtnList.get(i).setIcon(ImageResizer.fit(cocktailLabelBtnList.get(i), new ImageIcon("img/normalLabel.png")));
						}
					}

					displayCocktailNameLabelBtn();
					currentChapter = 0;
					initSelectItemPanel();
					changeQuizPanel(selectItemPanel);
				}
			}
		});
		bottomBtnPanel.add(submitBtn);

		mixingPanel.add(bottomBtnPanel);
	}

	private void initSelectItemPanel() {
		selectItemPanel = new JPanel();
		selectItemPanel.setBounds(0, 0, 1000, 690);
		selectItemPanel.setLayout(null);
		selectItemPanel.setBackground(new Color(0, 0, 0, 0));
		selectItemPanel.setOpaque(false);

		contentPanel = new JPanel();
		contentPanel.setBounds(0, 130, 1000, 500);
		contentPanel.setLayout(null);
		contentPanel.setBackground(new Color(0, 0, 0, 0));
		contentPanel.setOpaque(false);

		int leftPadding = 15, hGap = 8, vGap = 10, topPadding = 13, itemBtnSize = 155;

		List<Item> itemList = new ArrayList<>();
		String labelText = null;

		switch (currentChapter) {
		case 0:
			itemList = glassItemList;
			labelText = "GLASS";
			break;
		case 1:
			for (int i = 0; i < 18; i++)
				itemList.add(alcoholicItemList.get(i));
			labelText = "ALCOHOLIC";
			break;
		case 2:
			for (int i = 18; i < alcoholicItemList.size(); i++)
				itemList.add(alcoholicItemList.get(i));
			labelText = "ALCOHOLIC";
			break;
		case 3:
			for (int i = 0; i < 18; i++)
				itemList.add(nonAlcoholicItemList.get(i));
			labelText = "NON ALCOHOLIC";
			break;
		case 4:
			for (int i = 18; i < nonAlcoholicItemList.size(); i++)
				itemList.add(nonAlcoholicItemList.get(i));
			labelText = "NON ALCOHOLIC";
			break;
		case 5:
			for (int i = 0; i < 18; i++)
				itemList.add(addAlcoholicItemList.get(i));
			labelText = "ADD ALCOHOLIC";
			break;
		case 6:
			for (int i = 18; i < addAlcoholicItemList.size(); i++)
				itemList.add(addAlcoholicItemList.get(i));
			labelText = "ADD ALCOHOLIC";
			break;
		case 7:
			itemList = garnishesItemList;
			labelText = "GARNISHES";
			break;
		case 8:
			itemList = additionalItemList;
			labelText = "ADDITIONAL";
			break;
		case 9:
			itemList = toolItemList;
			labelText = "TOOL";
			break;
		}

		int x, y;
		ItemButton btn;
		for (int i = 0; i < itemList.size(); i++) {
			x = (i % 6) * (hGap + itemBtnSize) + leftPadding;
			y = topPadding + (i / 6) * (hGap + itemBtnSize);
			btn = new ItemButton(new ImageIcon("img/item/" + itemList.get(i).getEngName() + ".png"), itemBtnSize, itemBtnSize, itemList.get(i).getType());
			btn.setActionCommand(itemList.get(i).getEngName());
			btn.setToolTipText(itemList.get(i).getEngName());
			btn.setLocation(x, y);
			btn.addActionListener(e->{
				int btnSize = 74;
				if (selectedItemBtnList.size() < 14) {
					ItemButton button = new ItemButton(new ImageIcon("img/item/" + e.getActionCommand() + ".png"), btnSize, btnSize, ((ItemButton)e.getSource()).getType());
					button.setActionCommand(e.getActionCommand());
					button.setToolTipText(e.getActionCommand());
					button.addActionListener(removeFromSelectedItemBtnList);
					selectedItemBtnList.add(button);
					QuizForm.this.displaySelectedItemBtn();
				}

				if (selectedItemBtnList.size() > 0)
					mixingBtn.setEnabled(true);
				else
					mixingBtn.setEnabled(false);
			});
			contentPanel.add(btn);
		}

		selectItemPanel.add(contentPanel);

		JLabel categoryLabel = new JLabel(labelText);
		categoryLabel.setFont(new Font("", Font.PLAIN, 40));
		categoryLabel.setBounds(0, 0, 1000, 130);
		categoryLabel.setHorizontalAlignment(JLabel.CENTER);
		categoryLabel.setVerticalAlignment(JLabel.CENTER);
		selectItemPanel.add(categoryLabel);

		bottomBtnPanel = new JPanel();
		bottomBtnPanel.setBounds(0, 630, 1000, 50);
		bottomBtnPanel.setLayout(null);
		bottomBtnPanel.setOpaque(false);

		// <
		JButton beforeBtn = new JButton();
		beforeBtn.setBorder(null);
		beforeBtn.setIcon(new ImageIcon("img/beforeNormal.png"));
		beforeBtn.setContentAreaFilled(false);
		beforeBtn.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				beforeBtn.setIcon(new ImageIcon("img/beforePressed.png"));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				beforeBtn.setIcon(new ImageIcon("img/beforeNormal.png"));
			}
		});
		beforeBtn.addActionListener(e->{
			currentChapter--;
			if (currentChapter == 0)
				((JButton)e.getSource()).setEnabled(false);
			initSelectItemPanel();
			changeQuizPanel(selectItemPanel);
		});
		beforeBtn.setBounds(20, 0, 60, 50);
		bottomBtnPanel.add(beforeBtn);

		// 선택완료
		mixingBtn = new JButton();
		mixingBtn.setBounds(250, 0, 500, 50);
		mixingBtn.setEnabled(false);
		mixingBtn.setBorder(null);
		mixingBtn.setContentAreaFilled(false);
		mixingBtn.setIcon(new ImageIcon("img/mixingNormal.png"));
		mixingBtn.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				mixingBtn.setIcon(new ImageIcon("img/mixingPressed.png"));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				mixingBtn.setIcon(new ImageIcon("img/mixingNormal.png"));
			}
		});

		mixingBtn.addActionListener(e->{
			// TODO
			for (int i = 0; i < selectedItemBtnList.size(); i++) {
				ItemButton item = selectedItemBtnList.get(i);
				item.addActionListener(showMixingMenu);
				item.setActionCommand(selectedItemBtnList.get(i).getActionCommand());
				item.removeActionListener(removeFromSelectedItemBtnList);
			}

			currentType = 9;
			selectedItemName = "METHOD";
			initMixingPanel();
			changeQuizPanel(mixingPanel);
		});
		bottomBtnPanel.add(mixingBtn);

		// >
		JButton nextBtn = new JButton();
		nextBtn.setBorder(null);
		nextBtn.setContentAreaFilled(false);
		nextBtn.setIcon(new ImageIcon("img/nextNormal.png"));
		nextBtn.setBounds(920, 0, 60, 50);
		nextBtn.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				nextBtn.setIcon(new ImageIcon("img/nextPressed.png"));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				nextBtn.setIcon(new ImageIcon("img/nextNormal.png"));
			}
		});
		nextBtn.addActionListener(e->{
			currentChapter++;
			if (currentChapter == 9)
				((JButton)e.getSource()).setEnabled(false);
			initSelectItemPanel();
			changeQuizPanel(selectItemPanel);
		});
		bottomBtnPanel.add(nextBtn);

		if (currentChapter == 0)
			beforeBtn.setEnabled(false);
		else if (currentChapter == 9)
			nextBtn.setEnabled(false);

		if (selectedItemBtnList.size() > 0)
			mixingBtn.setEnabled(true);

		selectItemPanel.add(bottomBtnPanel);
	}

	private void initStartQuizPanel() {
		startQuizPanel = new JPanel();
		startQuizPanel.setLayout(null);
		startQuizPanel.setOpaque(false);
		startQuizPanel.setBounds(0, 0, 1000, 690);

		JLabel nameLabel = new JLabel(cocktailLabelBtnList.get(currentCocktail).getText());
		nameLabel.setHorizontalAlignment(JLabel.CENTER);
		nameLabel.setVerticalAlignment(JLabel.CENTER);
		nameLabel.setBounds(100, 200, 800, 230);
		nameLabel.setFont(new Font("", Font.PLAIN, 80));
		startQuizPanel.add(nameLabel);

		// start 시작하기
		JButton startBtn = new JButton();
		startBtn.setBorder(null);
		startBtn.setContentAreaFilled(false);
		startBtn.setBounds(300, 440, 400, 100);
		startBtn.setIcon(new ImageIcon("img/startNormal.png"));
		startBtn.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				startBtn.setIcon(new ImageIcon("img/startPressed.png"));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				startBtn.setIcon(new ImageIcon("img/startNormal.png"));
			}
		});
		startBtn.addActionListener(e->{
			currentChapter = 0;
			//         timer.schedule(timerTask, 1000);
			TimerThread tt = new TimerThread();
			tt.setDaemon(true);
			tt.start();

			initSelectItemPanel();
			changeQuizPanel(selectItemPanel);
		});
		startQuizPanel.add(startBtn);
	}

	private void changeQuizPanel(JPanel panel) {
		quizPanel.removeAll();
		quizPanel.add(panel);
		quizPanel.revalidate();
		quizPanel.repaint();
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	private void initRightPanel() {
		// TODO
		rightPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				g.drawImage(Toolkit.getDefaultToolkit().getImage("img/sequence.png"), 0, 0, 300, 650, this);
			}
		};
		rightPanel.setBounds(1300, 190, 300, 710);
		rightPanel.setLayout(null);

		// 조주
		JLabel rBox = new JLabel();
		rBox.setVerticalAlignment(JLabel.CENTER);
		rBox.setHorizontalAlignment(JLabel.CENTER);
		rBox.setFont(new Font("", 0, 20));
		rBox.setBounds(0, 0, 300, 50);
		rightPanel.add(rBox);

		recipeBtnList = new ArrayList<>();
		recipePanel = new JPanel();
		recipePanel.setBounds(0, 50, 300, 600);
		recipePanel.setBackground(new Color(0, 0, 0, 0));
		recipePanel.setOpaque(false);
		recipePanel.setLayout(null);

		rightPanel.add(recipePanel);
		displayRecipeBtn();

		// 종료하기
		exitBtn = new JButton();
		exitBtn.setBounds(0, 650, 300, 60);
		exitBtn.setBorder(null);
		exitBtn.setContentAreaFilled(false);
		exitBtn.setIcon(new ImageIcon("img/exitNormal.png"));
		exitBtn.addMouseListener(new MouseAdapter(){
			@Override
			public void mousePressed(MouseEvent e) {
				exitBtn.setIcon(new ImageIcon("img/exitPressed.png"));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				exitBtn.setIcon(new ImageIcon("img/exitNormal.png"));
			}
		});
		exitBtn.addActionListener((e)->{System.exit(0);});
		rightPanel.add(exitBtn);

		cocktailPanel.add(rightPanel);
	}


	private void initLeftPanel() {
		leftPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				//				g.drawImage(ImageIO.read(new File("img/storage.png")), 0, 0, 300, 650, this);

				super.paintComponent(g);
				g.drawImage(Toolkit.getDefaultToolkit().getImage("img/storage.png"), 0, 0, 300, 650, 0, 0, 300, 650, this);
			}
		};
		leftPanel.setBounds(0, 190, 300, 710);
		leftPanel.setLayout(null);

		// 재료창고
		JLabel lBox = new JLabel();
		lBox.setHorizontalAlignment(JLabel.CENTER);
		lBox.setVerticalAlignment(JLabel.CENTER);
		lBox.setFont(new Font("", Font.PLAIN, 20));
		lBox.setBounds(0, 0, 300, 50);
		leftPanel.add(lBox);

		selectedItemBtnList = new ArrayList<>();
		itemPanel = new JPanel();
		itemPanel.setBounds(0, 50, 300, 600);
		itemPanel.setLayout(null);
		itemPanel.setBackground(new Color(0, 0, 0, 0));
		itemPanel.setOpaque(false);
		leftPanel.add(itemPanel);
		displaySelectedItemBtn();

		cocktailPanel.add(leftPanel);
	}


	private void initTopPanel() {
		topPanel = new JPanel();
		topPanel.setBounds(0, 0, 1600, 190);
		topPanel.setLayout(null);

		logoLabel = new JLabel(new ImageIcon("img/logo.png"));
		logoLabel.setBounds(0, 0, 300, 190);
		topPanel.add(logoLabel);

		time = 400;
		timerLabel = new JLabel();
		timerLabel.setBounds(1200, 0, 400, 190);
		timerLabel.setIcon(new ImageIcon("img/timer.png"));
		timerLabel.setHorizontalTextPosition(JLabel.CENTER);
		timerLabel.setVerticalTextPosition(JLabel.CENTER);
		timerLabel.setBackground(pinkColor);
		timerLabel.setForeground(Color.WHITE);
		timerLabel.setText((time / 60) + " : " + (time % 60));
		timerLabel.setFont(new Font("", Font.BOLD, 60));
		topPanel.add(timerLabel);

		cocktailNamePanel = new JPanel();
		cocktailNamePanel.setBounds(300, 20, 1000, 150);
		cocktailNamePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 0));

		cocktailNameList = new ArrayList<>();
		cocktailLabelBtnList = new ArrayList<>();

		for (String name : cocktailMap.keySet()) {
			cocktailNameList.add(cocktailMap.get(name));
			JButton btn = new JButton();/*new JButton(name, new ImageIcon("img/normalLabel.png"));*/
			btn.setFont(new Font("", Font.PLAIN, 30));
			btn.setVerticalTextPosition(JButton.CENTER);
			btn.setHorizontalTextPosition(JButton.CENTER);
			btn.setSize(150, 150);
			btn.setText(name);
			btn.setIcon(ImageResizer.fit(btn, new ImageIcon("img/normalLabel.png")));
			btn.setOpaque(false);
			btn.setContentAreaFilled(false);
			btn.setBorder(null);

			cocktailLabelBtnList.add(btn);
			cocktailNamePanel.add(btn);
		}
		topPanel.add(cocktailNamePanel);

		cocktailLabelBtnList.get(currentCocktail).setIcon(ImageResizer.fit(cocktailLabelBtnList.get(currentCocktail), new ImageIcon("img/currentLabel.png"))/*new ImageIcon("img/currentLabel.png")*/);

		cocktailPanel.add(topPanel);
	}

	private void prepareQuiz() {
		if (itemInfoDownloaded && cocktailInfoDownloaded) {
			init();

			mainPanel.removeAll();
			mainPanel.add(cocktailPanel);
			mainPanel.revalidate();
			mainPanel.repaint();
		}
	}

	private void displayCocktailNameLabelBtn() {
		cocktailNamePanel.removeAll();

		for (JButton btn : cocktailLabelBtnList)
			cocktailNamePanel.add(btn);

		cocktailNamePanel.revalidate();
		cocktailNamePanel.repaint();
	}

	private void displayRecipeBtn() {
		// TODO
		recipePanel.removeAll();

		int leftPadding = 71;
		int gap = 10;
		int itemBtnSize = 74;
		int topPadding = 11;

		int x, y;
		for (int i = 0; i < 14; i++) {
			if (i % 2 == 1)
				x = leftPadding + gap + itemBtnSize;
			else
				x = leftPadding;
			y = topPadding + (i / 2) * (gap + itemBtnSize);

			RecipeButton btn;
			if (i < recipeBtnList.size()) {
				btn = recipeBtnList.get(i);
			} else {
				btn = RecipeButton.emptyBtn();
				btn.setVisible(false);
			}

			btn.setLocation(x, y);
			recipePanel.add(btn);
		}

		recipePanel.revalidate();
		recipePanel.repaint();
	}

	private void displaySelectedItemBtn() {
		itemPanel.removeAll();

		int leftPadding = 71;
		int gap = 10;
		int itemBtnSize = 74;
		int topPadding = 11;

		int x, y;
		for (int i = 0; i < 14; i++) {
			if (i % 2 == 1)
				x = leftPadding + gap + itemBtnSize;
			else
				x = leftPadding;
			y = topPadding + (i / 2) * (gap + itemBtnSize);

			ItemButton btn;
			if (i < selectedItemBtnList.size()) {
				btn = selectedItemBtnList.get(i);
			} else {
				btn = ItemButton.emptyBtn();
			}

			btn.setLocation(x, y);
			itemPanel.add(btn);
		}

		itemPanel.revalidate();
		itemPanel.repaint();
	}

	private void initResultPanle() {
		resultPanel = new JPanel();
		resultPanel.setLayout(null);
		resultPanel.setBounds(0, 0, 1600, 900);

		resultCocktailPanel = new JPanel();
		resultCocktailPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));
		resultCocktailPanel.setBounds(0, 200, 1600, 300);

		Map<String, Cocktail> resultMap = new HashMap<>();
		Cocktail userCocktail, serverCocktail;
		List<Recipe> userRecipeList, serverRecipeList;
		Recipe userRecipe, serverRecipe;
		Item userItem, serverItem;

		for (String key : cocktailMap.keySet()) {
			if (userAnswerMap.containsKey(key)) {
				userRecipeList = userAnswerMap.get(key).getRecipe();
				serverRecipeList = cocktailMap.get(key).getRecipe();

				if (userRecipeList.size() != serverRecipeList.size()) {
					resultMap.put(key, cocktailMap.get(key));
				} else {
					// TODO 반복문을 돌며 로직을 확인
					boolean isCorrect = true;
					System.out.println("group\tname\tanswer");
					for (int i = 0; i < userRecipeList.size(); i++) {
						userRecipe = userRecipeList.get(i);
						serverRecipe = serverRecipeList.get(i);
						userItem = userRecipe.getItem();
						serverItem = serverRecipe.getItem();

						System.out.println(serverRecipe.isBeverageGroup() + "\t" + serverItem.getEngName() + "\t" + serverRecipe.getAnswer());
						System.out.println(userRecipe.isBeverageGroup() + "\t" + userItem.getEngName() + "\t" + userRecipe.getAnswer());

						if (serverRecipe.isBeverageGroup()) {
							if (!serverItem.getBeverageType().equals(userItem.getBeverageType())) {
								isCorrect = false;
								break;
							}
						} else {
							if (!userItem.getEngName().equals(serverItem.getEngName())) {
								isCorrect = false;
								break;
							}
						}

						if (!userRecipe.getAnswer().equals(serverRecipe.getAnswer())) {
							isCorrect = false;
							break;
						}
					}

					if (!isCorrect) {
						resultMap.put(key, cocktailMap.get(key));
					}
				}
			} else {
				resultMap.put(key, cocktailMap.get(key));
			}
		}

		for (String k : cocktailMap.keySet()) {
			JLabel lbl = new JLabel(k);
			lbl.setSize(300, 300);
			lbl.setFont(new Font("", Font.PLAIN, 60));
			lbl.setVerticalTextPosition(JLabel.CENTER);
			lbl.setHorizontalTextPosition(JLabel.CENTER);
			String imgName;
			if (resultMap.containsKey(k)) {
				// 틀린문제
				imgName = "wrongLabel.png";
			} else {
				// 맞은문제
				imgName = "correctLabel.png";
			}
			lbl.setIcon(ImageResizer.fit(lbl, new ImageIcon("img/" + imgName)));
			resultCocktailPanel.add(lbl);
		}

		// TODO resultMap 전송
		System.out.println("rsltMap size : " + resultMap.size());
		RequestMessage msg = new RequestMessage(RequestMessage.TYPE_WAN, RequestMessage.MODE_EXAM, userId, resultMap);
		WANThread t = new WANThread(msg);
		t.setDaemon(true);
		t.start();

		resultPanel.add(resultCocktailPanel);

		// TODO 뒤로가기 버튼 생성 -> SelectForm으로 이동
		JButton backBtn = new JButton();
		backBtn.setBounds(550, 550, 500, 150);
		backBtn.setContentAreaFilled(false);
		backBtn.setBorder(null);
		backBtn.setIcon(new ImageIcon("img/backBtnNormal.png"));
		backBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				backBtn.setIcon(new ImageIcon("img/backBtnPressed.png"));
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				backBtn.setIcon(new ImageIcon("img/backBtnNormal.png"));
			}
		});
		backBtn.addActionListener(e->{
			SelectForm sf = new SelectForm(userId);
			QuizForm.this.dispose();
		});
		resultPanel.add(backBtn);


		displayResultPanel();
	}

	private void displayResultPanel() {
		mainPanel.removeAll();

		mainPanel.add(resultPanel);

		mainPanel.revalidate();
		mainPanel.repaint();
	}

	class ItemInfoDownloadThread extends Thread {
		@Override
		public void run() {
			try {
//				Socket sock = new Socket(InetAddress.getByName("kbetter3.iptime.org"), 28130);
				Socket sock = HostReader.getHost(28130);
				ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
				itemInfoMap = (Map<Integer, List<Item>>) ois.readObject();

				glassItemList = itemInfoMap.get(Item.TYPE_GLASS);

				beverageItemList = itemInfoMap.get(Item.TYPE_BEVERAGE);
				alcoholicItemList = new ArrayList<>();
				nonAlcoholicItemList = new ArrayList<>();
				addAlcoholicItemList = new ArrayList<>();
				for (Item item : beverageItemList) {
					switch (item.getBeverageSection()) {
					case Item.SECTION_ALCOHOLIC :
						alcoholicItemList.add(item);
						break;
					case Item.SECTION_NON_ALCOHOLIC :
						nonAlcoholicItemList.add(item);
						break;

					case Item.SECTION_ADD_ALCOHOILC :
						addAlcoholicItemList.add(item);
					}
				}

				garnishesItemList = itemInfoMap.get(Item.TYPE_GARNISHES);

				additionalItemList = itemInfoMap.get(Item.TYPE_ADDITIONAL);

				toolItemList = new ArrayList<>();
				toolItemList.add(itemInfoMap.get(Item.TYPE_BARSPOON).get(0));
				toolItemList.add(itemInfoMap.get(Item.TYPE_COCKTAILPICK).get(0));
				toolItemList.add(itemInfoMap.get(Item.TYPE_SHAKER).get(0));
				toolItemList.add(itemInfoMap.get(Item.TYPE_MIXINGGLASS_AND_STRAINER).get(0));
				toolItemList.add(itemInfoMap.get(Item.TYPE_BLENDER).get(0));

				itemInfoDownloaded = true;
				prepareQuiz();
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

	class WANThread extends Thread {
		private RequestMessage msg;

		public WANThread(RequestMessage msg) {
			this.msg = msg;
		}

		@Override
		public void run() {
			try {
//				Socket sock = new Socket(InetAddress.getByName("kbetter3.iptime.org"), 28131);
				Socket sock = HostReader.getHost(28131);
				ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
				oos.writeObject(msg);
				oos.flush();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class RequestQuizThread extends Thread {
		private RequestMessage msg;

		@Override
		public void run() {
			try {
//				Socket sock = new Socket(InetAddress.getByName("kbetter3.iptime.org"), 28131);
				Socket sock = HostReader.getHost(28131);
				ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
				oos.writeObject(msg);
				oos.flush();

				ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
				cocktailMap = (Map<String, Cocktail>) ois.readObject();
				cocktailInfoDownloaded = true;

				prepareQuiz();
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


	class TimerThread extends Thread {
		@Override
		public void run() {

			while (time > 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				time--;
				if (time == 90) {
					timerLabel.setForeground(Color.RED);
				}
				DecimalFormat df = new DecimalFormat("00");
				timerLabel.setText((time / 60) + " : " + df.format(time % 60));
			}

			initResultPanle();
		}
	}
}