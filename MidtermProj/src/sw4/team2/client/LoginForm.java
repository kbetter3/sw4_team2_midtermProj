package sw4.team2.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import com.sun.glass.events.KeyEvent;

import sw4.team2.common.Member;

public class LoginForm extends JFrame {
	JPanel mainPanel;
	JLabel TitleImagelb;
	JLabel loginImageLbl;
	JLabel idLbl, pwLbl;
	JTextField idTf;
	JPasswordField pwTf;
	JButton loginBtn, regBtn, exitBtn;
	Member member;
	
	public LoginForm() {
		display();
		menu();
		event();
		
		this.setTitle("Login");
		this.setBounds(100, 100, 453, 365);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private void loginProc() {
		try {
			boolean login = false;
			member = new Member(idTf.getText(), new String(pwTf.getPassword()), Member.LOGIN);
			Socket sock = new Socket(InetAddress.getByName("kbetter3.iptime.org"), 2240);
			ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
			oos.writeObject(member);
			oos.flush();
			
			ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
			login = ois.readBoolean();
			
			if (login) {
				// 로그인 성공
				SelectForm sf = new SelectForm(member.getId());
				LoginForm.this.dispose();
			} else {
				JOptionPane.showMessageDialog(LoginForm.this, "아이디/비밀번호가 올바르지 않습니다.", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
			sock.close();
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void event() {
		KeyStroke loginKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
		Action loginAction = new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				loginProc();
			}
		};
		ActionListener loginBtnListener = (e)->{
			loginProc();
		};
		loginBtn.addActionListener(loginBtnListener);
		pwTf.getInputMap().put(loginKey, "login");
		pwTf.getActionMap().put("login", loginAction);
		
		ActionListener regBtnListener = (e)->{
			RegForm rf = new RegForm();
			rf.setModal(true);
			rf.setVisible(true);
		};
		regBtn.addActionListener(regBtnListener);
		
		ActionListener exitBtnListener = (e)->{
			System.exit(0);
		};
		exitBtn.addActionListener(exitBtnListener);
		
		//============마우스 리스너 ==========================
		MouseListener listener=new MouseAdapter() {
			public void  mouseReleased(MouseEvent event) {
				if(event.getSource()==loginBtn) {
					loginBtn.setIcon(fit(loginBtn, new ImageIcon("img/loginBtnImg.png")));
				}
				else if(event.getSource()==regBtn) {
					regBtn.setIcon(fit(regBtn, new ImageIcon("img/regBtnImg.png")));
				}
				else if(event.getSource()==exitBtn) {
					exitBtn.setIcon(fit(exitBtn,new ImageIcon("img/exitBtnImg.png")));
				}
			}
			
			public void mousePressed(MouseEvent event) {
				if(event.getSource()==loginBtn) {
					loginBtn.setIcon(fit(loginBtn, new ImageIcon("img/loginBtnImg1.png")));
				}
				else if(event.getSource()==regBtn) {
					regBtn.setIcon(fit(regBtn, new ImageIcon("img/regBtnImg1.png")));
				}
				else if(event.getSource()==exitBtn) {
					exitBtn.setIcon(fit(exitBtn, new ImageIcon("img/exitBtnImg1.png")));
				}
			}
		};
		loginBtn.addMouseListener(listener);
		regBtn.addMouseListener(listener);
		exitBtn.addMouseListener(listener);
	}

	private void menu() {
	}

	private void display() {
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		this.setContentPane(mainPanel);
		TitleImagelb = new JLabel();
		TitleImagelb.setBounds(54,37,324,100);
		TitleImagelb.setIcon(fit(TitleImagelb,new ImageIcon("img/title.png")));
		mainPanel.add(TitleImagelb);
		
		idLbl = new JLabel("");
		idLbl.setBounds(42, 153, 95, 35);
		idLbl.setIcon(fit(idLbl,new ImageIcon("img/IdImg.png")));
		idLbl.setHorizontalAlignment(JLabel.RIGHT);
		mainPanel.add(idLbl);
		
		pwLbl = new JLabel("");
		pwLbl.setBounds(42, 219, 95, 35);
		pwLbl.setIcon(fit(pwLbl,new ImageIcon("img/PwImg.png")));
		pwLbl.setHorizontalAlignment(JLabel.RIGHT);
		mainPanel.add(pwLbl);
		
		idTf = new JTextField();
		idTf.setBounds(185, 153, 181, 35);
		idTf.setColumns(12);
		idTf.setToolTipText("아이디를 입력하세요");
		mainPanel.add(idTf);
		
		pwTf = new JPasswordField();
		pwTf.setBounds(185, 219, 181, 35);
		pwTf.setColumns(12);
		pwTf.setToolTipText("비밀번호를 입력하세요");
		mainPanel.add(pwTf);
		
		loginBtn = new JButton();
		loginBtn.setBounds(42, 281, 111, 35);
		loginBtn.setIcon(fit(loginBtn, new ImageIcon("img/loginBtnImg.png")));
		loginBtn.setContentAreaFilled(false);
		loginBtn.setBorder(null);
		mainPanel.add(loginBtn);
		
		regBtn = new JButton();
		regBtn.setBounds(165, 281, 111, 35);
		regBtn.setIcon(fit(regBtn, new ImageIcon("img/regBtnImg.png")));
		regBtn.setContentAreaFilled(false);
		regBtn.setBorder(null);
		mainPanel.add(regBtn);
		
		exitBtn = new JButton();
		exitBtn.setBounds(288, 281, 111, 35);
		exitBtn.setIcon(fit(exitBtn,new ImageIcon("img/exitBtnImg.png")));
		exitBtn.setContentAreaFilled(false);
		exitBtn.setBorder(null);
		mainPanel.add(exitBtn);
	}
	
	private ImageIcon fit(Component c, ImageIcon icon) {
		int width = c.getWidth();
		int height = c.getHeight();
		
		BufferedImage resize = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = resize.getGraphics();
		g.drawImage(icon.getImage(), 0, 0, width, height, null);
		
		return new ImageIcon(resize);
	}
}
