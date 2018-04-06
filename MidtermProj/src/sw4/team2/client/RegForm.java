package sw4.team2.client;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import sw4.team2.common.HostReader;
import sw4.team2.common.Member;

public class RegForm extends JDialog {
	JPanel mainPanel;
	JLabel TitleImagelb;
	JLabel idLbl, pwLbl, pwCheckLbl;
	JTextField idTf;
	JPasswordField pwTf, pwCheckTf;
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
				
				String idregex="^[a-zA-Z0-9_-]{4,20}$";
				//String pwregex="^[a-zA-Z0-9_-]{8,20}$";

				if(Pattern.matches(idregex,idTf.getText())/* || Pattern.matches(pwregex, pwregex)*/){
					String pwStr = new String(pwTf.getPassword());
					String pwChStr = new String(pwCheckTf.getPassword());
					if (pwTf.getPassword().length > 0 && pwStr.equals(pwChStr)) {
//						Socket sock = new Socket(InetAddress.getByName("kbetter3.iptime.org"), 2240);
						Socket sock = HostReader.getHost(2240);
						Member member = new Member(idTf.getText(), pwTf.getPassword().toString(), Member.REGISTER);
						ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
						oos.writeObject(member);
						oos.flush();
						ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
						register = ois.readBoolean();
						
						if (register) {
							JOptionPane.showMessageDialog(RegForm.this, "회원가입 성공", "회원가입", JOptionPane.PLAIN_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(RegForm.this, "회원가입 실패", "회원가입", JOptionPane.ERROR_MESSAGE);
						}

						sock.close();
					} else {
						JOptionPane.showMessageDialog(RegForm.this, "비밀번호가 옳바르지 않습니다.", "회원가입", JOptionPane.ERROR_MESSAGE);
					}
				}else {
					JOptionPane.showMessageDialog(RegForm.this, "아이디는 4자이상 영문,숫자만 사용가능합니다.", "회원가입", JOptionPane.ERROR_MESSAGE);
				}
				
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
		//============마우스 리스너 ==========================
		MouseListener listener=new MouseAdapter() {
			public void  mouseReleased(MouseEvent event) {
				if(event.getSource()==cancelBtn) {
					cancelBtn.setIcon(fit(cancelBtn,new ImageIcon("img/cancelBtnImg.png")));
				}
				else if(event.getSource()==regBtn) {
					regBtn.setIcon(fit(regBtn, new ImageIcon("img/regBtnImg3.png")));
				}
			}

			public void mousePressed(MouseEvent event) {
				if(event.getSource()==cancelBtn) {
					cancelBtn.setIcon(fit(cancelBtn,new ImageIcon("img/cancelBtnImg1.png")));
				}
				else if(event.getSource()==regBtn) {
					regBtn.setIcon(fit(regBtn, new ImageIcon("img/regBtnImg4.png")));
				}
			}
		};
		cancelBtn.addMouseListener(listener);
		regBtn.addMouseListener(listener);
	}

	private void menu() {
	}

	private void display() {
		mainPanel = new JPanel();
		mainPanel.setLayout(null);
		this.setContentPane(mainPanel);

		TitleImagelb = new JLabel();
		TitleImagelb.setBounds(32,37,200,60);
		TitleImagelb.setIcon(fit(TitleImagelb,new ImageIcon("img/title2.png")));
		mainPanel.add(TitleImagelb);

		idLbl = new JLabel("");
		idLbl.setBounds(32, 116, 86, 26);
		idLbl.setIcon(fit(idLbl,new ImageIcon("img/regIdImg.png")));
		idLbl.setHorizontalAlignment(JLabel.RIGHT);;
		mainPanel.add(idLbl);

		pwLbl = new JLabel("");
		pwLbl.setBounds(32, 158, 86, 26);
		pwLbl.setIcon(fit(pwLbl,new ImageIcon("img/regPwImg.png")));
		pwLbl.setHorizontalAlignment(JLabel.RIGHT);
		mainPanel.add(pwLbl);

		pwCheckLbl = new JLabel("");
		pwCheckLbl.setBounds(32, 200, 86, 26);
		pwCheckLbl.setIcon(fit(pwLbl,new ImageIcon("img/regPwCImg.png")));
		pwCheckLbl.setHorizontalAlignment(JLabel.RIGHT);
		mainPanel.add(pwCheckLbl);

		idTf = new JTextField();
		idTf.setColumns(12);
		idTf.setBounds(130, 116, 116, 26);
		mainPanel.add(idTf);

		pwTf = new JPasswordField();
		pwTf.setColumns(12);
		pwTf.setBounds(130, 158, 116, 26);
		mainPanel.add(pwTf);

		pwCheckTf = new JPasswordField();
		pwCheckTf.setColumns(12);
		pwCheckTf.setBounds(130, 200, 116, 26);
		mainPanel.add(pwCheckTf);

		regBtn = new JButton("");
		regBtn.setBounds(50, 253, 97, 35);
		regBtn.setIcon(fit(regBtn, new ImageIcon("img/regBtnImg3.png")));
		regBtn.setContentAreaFilled(false);
		regBtn.setBorder(null);
		mainPanel.add(regBtn);

		cancelBtn = new JButton("");
		cancelBtn.setBounds(153, 253, 97, 35);
		cancelBtn.setIcon(fit(cancelBtn,new ImageIcon("img/cancelBtnImg.png")));
		cancelBtn.setContentAreaFilled(false);
		cancelBtn.setBorder(null);
		mainPanel.add(cancelBtn);
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
