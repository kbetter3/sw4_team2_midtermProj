package sw4.team2.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import sw4.team2.common.Cocktail;
import sw4.team2.common.HostReader;

public class WANote extends JDialog {
	private JPanel MainPanel = new JPanel();

	private ImageIcon icon1 = new ImageIcon("img/Wanss.png");

	private JToolBar toolBar = new JToolBar();
	private JLabel Wanlb = new JLabel("");

	private JTextArea Jta = new JTextArea();

	JScrollPane sp = new JScrollPane(Jta, 
			ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
			ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);


	public WANote(Frame f, boolean modal) throws ClassNotFoundException {
		super(f, modal);
		this.setTitle("오답노트");
		this.setBounds(0,0,500,500);
		this.setLocationByPlatform(true);
		this.setResizable(false);
		this.init();
		Thread thread = new Thread() {
			@Override
			public void run() {
				String userId = "kkk";
				Map<String, Cocktail> wanMap;
				try {
//					Socket sock = new Socket(InetAddress.getByName("kbetter3.iptime.org"), 28129);
					Socket sock = HostReader.getHost(28129);
					ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
					oos.writeObject(userId);
					oos.flush();

					ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
					wanMap = (Map<String, Cocktail>) ois.readObject();
					sock.close();

					System.out.println(wanMap.size());

					for (String key : wanMap.keySet()) {
						// TODO wanMap에서 꺼내서 사용하면됨
						String name=key + "\n";
						Jta.append(name);
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		};

		thread.setDaemon(true);
		thread.start();

		this.display();
		this.setVisible(true);

	}
	public void init() {
		MainPanel.setLayout(new BorderLayout());
		this.setContentPane(MainPanel);
		ToolBar();
	}
	private void display() throws ClassNotFoundException{
		WrongText();
	}
	public void ToolBar() {
		MainPanel.add(toolBar,BorderLayout.NORTH);
		toolBar.setBackground(new Color(255, 228, 225));
		Wanlb.setIcon(icon1);
		toolBar.add(Wanlb);
	}
	private void WrongText() {
		Jta.setEditable(false);
		MainPanel.add(sp);
	}
}
