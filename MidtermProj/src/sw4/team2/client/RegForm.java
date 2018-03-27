package sw4.team2.client;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import sw4.team2.common.Member;

public class RegForm extends JDialog {
	JPanel mainPanel;
	JLabel idLbl, pwLbl, pwCheckLbl;
	JTextField idTf, pwTf, pwCheckTf;
	JButton regBtn, cancelBtn;
	
	public RegForm() {
		display();
		menu();
		event();
		
		this.setTitle("회원가입");
		this.setResizable(false);
		this.setBounds(100, 100, 300, 349);
	}

	private void event() {
		ActionListener regBtnListener = (e)->{
			try {
				boolean register = false;
				Socket sock = new Socket(InetAddress.getByName("kbetter3.iptime.org"), 2240);
				Member member = new Member(idTf.getText(), pwTf.getText(), Member.REGISTER);
				ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
				oos.writeObject(member);
				oos.flush();
				ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
				register = ois.readBoolean();
				
				if (register) {
					// 등록성공
				} else {
					// 등록실패
				}
				
				sock.close();
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		};
		regBtn.addActionListener(regBtnListener);
		
		ActionListener cancelBtnListener = (e)->{
			RegForm.this.dispose();
		};
		cancelBtn.addActionListener(cancelBtnListener);
	}

	private void menu() {
	}

	private void display() {
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		this.setContentPane(mainPanel);
		
		idLbl = new JLabel("아이디");
		idLbl.setBounds(32, 116, 86, 26);
		idLbl.setHorizontalAlignment(JLabel.RIGHT);;
		mainPanel.add(idLbl);
		
		pwLbl = new JLabel("비밀번호");
		pwLbl.setBounds(32, 158, 86, 26);
		pwLbl.setHorizontalAlignment(JLabel.RIGHT);
		mainPanel.add(pwLbl);
		
		pwCheckLbl = new JLabel("비밀번호 확인");
		pwCheckLbl.setBounds(32, 200, 86, 26);
		pwCheckLbl.setHorizontalAlignment(JLabel.RIGHT);
		mainPanel.add(pwCheckLbl);
		
		idTf = new JTextField();
		idTf.setColumns(12);
		idTf.setBounds(130, 116, 116, 26);
		mainPanel.add(idTf);
		
		pwTf = new JTextField();
		pwTf.setColumns(12);
		pwTf.setBounds(130, 158, 116, 26);
		mainPanel.add(pwTf);
		
		pwCheckTf = new JTextField();
		pwCheckTf.setColumns(12);
		pwCheckTf.setBounds(130, 200, 116, 26);
		mainPanel.add(pwCheckTf);
		
		regBtn = new JButton("가입하기");
		regBtn.setBounds(50, 253, 97, 35);
		mainPanel.add(regBtn);
		
		cancelBtn = new JButton("취소");
		cancelBtn.setBounds(153, 253, 97, 35);
		mainPanel.add(cancelBtn);
	}
}
