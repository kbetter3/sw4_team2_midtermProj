package sw4.team2.client;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sw4.team2.common.Member;

public class LoginForm extends JFrame {
	JPanel mainPanel;
	JLabel loginImageLbl;
	JLabel idLbl, pwLbl;
	JTextField idTf, pwTf;
	JButton loginBtn, regBtn, exitBtn;
	Member member;
	
	public LoginForm() {
		display();
		menu();
		event();
		
		this.setTitle("Login");
		this.setBounds(100, 100, 535, 362);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}

	private void event() {
		ActionListener loginBtnListener = (e)->{
			try {
				boolean login = false;
				member = new Member(idTf.getText(), pwTf.getText(), Member.REGISTER);
				Socket sock = new Socket(InetAddress.getByName("kbetter3.iptime.org"), 2240);
				ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
				oos.writeObject(member);
				oos.flush();
				
				ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
				login = ois.readBoolean();
				
				if (login) {
					// 로그인 성공
				} else {
					JOptionPane.showMessageDialog(LoginForm.this, "아이디/비밀번호가 옳바르지 않습니다.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
				sock.close();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		};
		loginBtn.addActionListener(loginBtnListener);
		
		ActionListener regBtnListener = (e)->{
			
		};
		regBtn.addActionListener(regBtnListener);
	}

	private void menu() {
	}

	private void display() {
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		this.setContentPane(mainPanel);
		
		loginImageLbl = new JLabel();
		loginImageLbl.setBounds(71, 10, 392, 116);
		loginImageLbl.setIcon(fit(loginImageLbl, new ImageIcon("img/loginImg.png")));
		mainPanel.add(loginImageLbl);
		
		idLbl = new JLabel("아이디");
		idLbl.setBounds(59, 170, 57, 15);
		idLbl.setHorizontalAlignment(JLabel.RIGHT);
		mainPanel.add(idLbl);
		
		pwLbl = new JLabel("비밀번호");
		pwLbl.setBounds(59, 236, 57, 15);
		pwLbl.setHorizontalAlignment(JLabel.RIGHT);
		mainPanel.add(pwLbl);
		
		idTf = new JTextField();
		idTf.setBounds(128, 153, 116, 50);
		idTf.setColumns(12);
		idTf.setToolTipText("아이디를 입력하세요");
		mainPanel.add(idTf);
		
		pwTf = new JTextField();
		pwTf.setBounds(128, 219, 116, 50);
		pwTf.setColumns(12);
		pwTf.setToolTipText("비밀번호를 입력하세요");
		mainPanel.add(pwTf);
		
		loginBtn = new JButton();
		loginBtn.setBounds(338, 153, 128, 116);
		loginBtn.setIcon(fit(loginBtn, new ImageIcon("img/loginBtnImg.png")));
		mainPanel.add(loginBtn);
		
		regBtn = new JButton();
		regBtn.setBounds(193, 290, 173, 23);
		regBtn.setIcon(fit(regBtn, new ImageIcon("img/regBtnImg.png")));
		mainPanel.add(regBtn);
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
