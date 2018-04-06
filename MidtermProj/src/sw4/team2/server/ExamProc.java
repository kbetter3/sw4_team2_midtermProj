package sw4.team2.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
	private ServerSocket wanServer;

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

			wanServer = new ServerSocket(28129);
			WANThread want = new WANThread();
			want.setDaemon(true);
			want.start();
			System.out.println("WANThread is running");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class WANSendThread extends Thread {
		private Socket sock;
		public WANSendThread(Socket sock) {
			this.sock = sock;
		}

		@Override
		public void run() {
			ObjectOutputStream oos;
			ObjectInputStream ois;
			File f = new File("files/wan.db");
			Map<String, Map<String, Cocktail>> wanMap;
			Map<String, Cocktail> userMap = new HashMap<>();

			try {
				if (!f.exists()) {
					f.createNewFile();
					wanMap = new HashMap<>();
					oos = new ObjectOutputStream(new FileOutputStream(new File("files/wan.db")));
					oos.writeObject(wanMap);
					oos.flush();
					oos.close();
				} else {
					ois = new ObjectInputStream(new FileInputStream(f));
					wanMap = (Map<String, Map<String, Cocktail>>) ois.readObject();
					ois.close();
				}

				ois = new ObjectInputStream(sock.getInputStream());
				String userID = (String) ois.readObject();

				if (wanMap.containsKey(userID)) {
					userMap = wanMap.get(userID);
				}

				System.out.println(sock.getInetAddress() + "\t" + userID + " " + userMap.size());

				oos = new ObjectOutputStream(sock.getOutputStream());
				oos.writeObject(userMap);
				oos.flush();
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}


		}
	}

	class WANThread extends Thread {
		@Override
		public void run() {
			try {
				while (true) {
					System.out.println("WAN ready");
					Socket sock = wanServer.accept();
					System.out.println(sock.getInetAddress() + "\treq WAN");

					WANSendThread t = new WANSendThread(sock);
					t.setDaemon(true);
					t.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
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
				System.out.println("207 " + message.getUserId());
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
					/*
					 * Map<String id, Map<String, Cocktail>>
					 */
					Map<String, Map<String, Cocktail>> wanMap;
					File wanFile = new File("files/wan.db");
					ObjectOutputStream os;
					ObjectInputStream is;

					if (!wanFile.exists()) {
						wanFile.createNewFile();
						wanMap = new HashMap<>();
						os = new ObjectOutputStream(new FileOutputStream(wanFile));
						os.writeObject(wanMap);
						os.flush();
						os.close();
					}

					is = new ObjectInputStream(new FileInputStream(wanFile));
					wanMap = (Map<String, Map<String, Cocktail>>) is.readObject();

					Map<String, Cocktail> userMap;

					if (wanMap.containsKey(message.getUserId())) {
						userMap = wanMap.get(message.getUserId());
					} else {
						userMap = new HashMap<>();
					}

					for (String key : message.getCocktailMap().keySet()) {
						userMap.put(key, message.getCocktailMap().get(key));
					}
					System.out.println(message.getUserId() + " " + userMap.size());
					wanMap.put(message.getUserId(), userMap);
					os = new ObjectOutputStream(new FileOutputStream(wanFile));
					os.writeObject(wanMap);
					os.flush();
					os.close();
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