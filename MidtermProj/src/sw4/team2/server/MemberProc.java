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
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import sw4.team2.common.Member;

public class MemberProc {
	private File memberFile;
	
	public MemberProc() {
		try {
			ServerSocket server = new ServerSocket(2240);
			memberFile = new File("files/member.db");
			
			
			Thread thread = new Thread() {
				@Override
				public void run() {
					while (!isInterrupted()) {
						try {
							Socket srcSock = server.accept();
							Thread t = new Thread() {
								Socket sock = srcSock;
								@Override
								public void run() {
									ObjectInputStream ois;
									ObjectOutputStream oos;
									boolean rslt = false;
									try {
										ois = new ObjectInputStream(sock.getInputStream());
										Member member = (Member) ois.readObject();
										
										if (member.getType() == 0) {
											rslt = regProc(member);
										} else if (member.getType() == 1) {
											rslt = loginProc(member);
										}
										
										oos = new ObjectOutputStream(sock.getOutputStream());
										oos.writeBoolean(rslt);
										oos.flush();
										
										ois.close();
//										oos.close();
//										sock.close();
									} catch (IOException e) {
										e.printStackTrace();
									} catch (ClassNotFoundException e) {
										e.printStackTrace();
									}
									
								}
							};
							t.setDaemon(true);
							t.start();
							
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						
					}
				}
			};
			
			thread.setDaemon(true);
			thread.start();
			System.out.println("MemberProc is running");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
	public boolean loginProc(Member m) {
		Map<String, String> member = new HashMap<>();
		boolean login = false;
		
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(memberFile));
			member = (Map<String, String>) ois.readObject();
			
			System.out.println(m.getId() + " / " + m.getPw());
			
			for (String key : member.keySet()) {
				System.out.println(key + " : " + member.get(key));
			}
			
			if (member.containsKey(m.getId()) && member.get(m.getId()).equals(m.getPw())) {
				login = true;
			}
			
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return login;
	}
	
	public boolean regProc(Member m) {
		boolean alreadyExist = false;
		boolean reg = false;
		Map<String, String> member = new HashMap<>();
		
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(memberFile));
			member = (Map<String, String>) ois.readObject();
			
			if (member.containsKey(m.getId())) {
				alreadyExist = true;
			}
			ois.close();
			
			if (!alreadyExist) {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(memberFile));
				member.put(m.getId(), m.getPw());
				oos.writeObject(member);
				oos.flush();
				oos.close();
				reg = true;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		return reg;
	}

}
