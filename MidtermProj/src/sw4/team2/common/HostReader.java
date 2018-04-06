package sw4.team2.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;

public class HostReader {
	public static Socket getHost(int port) {
		Socket sock = null;
		try {
			File f = new File("files/server.txt");
			BufferedReader br = new BufferedReader(new FileReader(f));
			sock = new Socket(InetAddress.getByName(br.readLine()), port);
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sock;
	}
}
