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
	
	public ExamProc() {
		ItemInfoThread iit = new ItemInfoThread();
		iit.setDaemon(true);
		iit.start();
		System.out.println("ItemInfoProc is running");
		
		CocktailRequestThread crt = new CocktailRequestThread();
		crt.setDaemon(true);
		crt.start();
		System.out.println("CocktailRequestThread is running");
	}
	
	class ItemInfoThread extends Thread {
		@Override
		public void run() {
			try {
				ServerSocket server = new ServerSocket(28130);
				Socket sock = server.accept();
				
				ItemInfoSendThread t = new ItemInfoSendThread(sock);
				t.setDaemon(true);
				t.start();
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
				Map<Integer, Item> item = (Map<Integer, Item>) ois.readObject();
				ois.close();
				
				ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
				oos.writeObject(item);
				oos.flush();
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
				ServerSocket server = new ServerSocket(28131);
				// TODO make tcp session
				Socket sock = server.accept();
				CocktailSendThread cst = new CocktailSendThread(sock);
				cst.setDaemon(true);
				cst.start();
				
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
					List<Cocktail> cocktailList = (ArrayList<Cocktail>) cocktail.values();
					Map<String, Cocktail> quiz = new HashMap<>();
					
					while (quiz.size() < message.getRequestMode()) {
						Cocktail c = cocktailList.get((int)Math.random() * cocktailList.size());
						quiz.put(c.getName(), c);
					}
					
					ObjectOutputStream oos = new ObjectOutputStream(sock.getOutputStream());
					oos.writeObject(quiz);
					oos.flush();
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
