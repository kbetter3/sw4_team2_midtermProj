package sw4.team2.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sw4.team2.common.Cocktail;
import sw4.team2.common.Item;
import sw4.team2.common.RequestMessage;

public class ExamProc {
	private ServerSocket itemServer;
	private ServerSocket cocktailServer;
	public ExamProc() {
		try {
			itemServer = new ServerSocket(28130);
			ItemInfoThread iit = new ItemInfoThread();
			iit.setDaemon(true);
			iit.start();
			System.out.println("ItemInfoProc is running");
			
			cocktailServer = new ServerSocket(28131);
			CocktailRequestThread crt = new CocktailRequestThread();
			crt.setDaemon(true);
			crt.start();
			System.out.println("CocktailRequestThread is running");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	class ItemInfoThread extends Thread {
		@Override
		public void run() {
			try {
				while (true) {
					System.out.println("item ready");
					Socket sock = itemServer.accept();
					System.out.println(sock.getInetAddress() + "\treq ItemInfo");
					
					ItemInfoSendThread t = new ItemInfoSendThread(sock);
					t.setDaemon(true);
					t.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class ItemInfoSendThread extends Thread {
		private Socket sock;
		
		public ItemInfoSendThread(Socket sock) {
			this.sock = sock;
		}
		
		@Override
		public void run() {
			try {
				File itemFile = new File("files/item.db");
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(itemFile));
				Map<Integer, List<Item>> item = (Map<Integer, List<Item>>) ois.readObject();
				ois.close();
				
				ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
				oos.writeObject(item);
				oos.flush();
				System.out.println(sock.getInetAddress() + "\tsent ItemInfo");
				sock.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	class CocktailRequestThread extends Thread {
		@Override
		public void run() {
			try {
				// TODO make tcp session
				while (true) {
					System.out.println("cocktail Readey");
					Socket sock = cocktailServer.accept();
					System.out.println(sock.getInetAddress() + "\treq Cocktail");
					CocktailSendThread cst = new CocktailSendThread(sock);
					cst.setDaemon(true);
					cst.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class CocktailSendThread extends Thread {
		private Socket sock;
		
		public CocktailSendThread(Socket sock) {
			this.sock = sock;
		}
		
		@Override
		public void run() {
			// TODO send quiz cocktail
			try {
				File cocktailFile = new File("files/cocktail.db");
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(cocktailFile));
				Map<String, Cocktail> cocktail = (Map<String, Cocktail>) ois.readObject();
				ois.close();
				
				ois = new ObjectInputStream(sock.getInputStream());
				RequestMessage message = (RequestMessage) ois.readObject();
				switch (message.getRequestType()) {
				case RequestMessage.TYPE_REQUEST :
					List<String> cocktailNameList = new ArrayList<String>(cocktail.keySet());
					Map<String, Cocktail> quiz = new HashMap<>();
					
					System.out.println(cocktailNameList.size());
					
					while (quiz.size() < message.getRequestMode()) {
						Cocktail c = cocktail.get(cocktailNameList.get((int) (Math.random() * cocktailNameList.size())));
						quiz.put(c.getName(), c);
					}
					
					ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
					oos.writeObject(quiz);
					oos.flush();
					System.out.println(sock.getInetAddress() + "\tsent Cocktail");
					sock.close();
					break;
					
				case RequestMessage.TYPE_WAN :
					// TODO make WAN file and read and after send
					break;
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

}
