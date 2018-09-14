package serverchat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {

	public static void main(String[] args) {
		
		try {
			ServerSocket server = new ServerSocket(30000);
			List<OutputStream> clients = new ArrayList<>(); 
			while(true) {
				Socket socket = server.accept();
				//lambda expression
				Runnable run = () -> {
					try {
						InputStream is = socket.getInputStream();
						OutputStream os = socket.getOutputStream();
						String from = socket.getInetAddress().toString();
						for (OutputStream osclient : clients) {
							String msg = String.format("%s entrou.\r\n", from);
							osclient.write(msg.getBytes());
						}
						clients.add(os);
						Scanner scan = new Scanner(is);
						while(scan.hasNext()) {
							String line = scan.nextLine();
							String msg = String.format("%s: %s\r\n", from, line);
							for (OutputStream osclient : clients) {
								osclient.write(msg.getBytes());
							}
							System.out.println(line);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				};
				new Thread(run).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
