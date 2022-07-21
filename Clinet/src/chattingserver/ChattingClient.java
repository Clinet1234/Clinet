package chattingserver;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import client.IChattingClient;

public class ChattingClient implements IChattingClient {

	public void connectChattingServer(String ipaddress) {
		String name = "user";
		int port = 4000;
		try {
			Socket socket = new Socket(ipaddress, port);  
			System.out.println("포트번호: "+port+"로 서버 접속 성공");
			
			OutputStream out = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			writer.println(name);
			writer.println("chatclient");
			
			ClientChattingListener li = new ClientChattingListener(socket);
			ClientChattingWriter wr = new ClientChattingWriter(socket);
			
			li.start();
			wr.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
