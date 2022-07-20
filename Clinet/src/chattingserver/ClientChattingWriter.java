package chattingserver;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientChattingWriter extends Thread{
	Socket socket = null;
	Scanner sc = new Scanner(System.in);
	
	public ClientChattingWriter(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		String buff = null;
		try {
			OutputStream out = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			while(true) {
				buff = sc.nextLine();
				writer.println(buff);
				if(buff.equals("/close")) {
					socket.close();
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}