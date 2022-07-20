package lobbyserver.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

import client.ILobbyClient;
import lobbyserver.server.LobbyServer;

public class LobbyServerClient implements ILobbyClient {
	
	public static final int LOBBY_PORT = LobbyServer.CLIENT_PORT;
	
	@Override
	public String connectLobbyServer() {
		String lobby_ip = "";
		try {
			System.out.println("please put lobby server ip address");
			Scanner sc = new Scanner(System.in);
			lobby_ip = sc.nextLine();
			
			System.out.println("try to connect lobby server...");
			Socket socket = new Socket(lobby_ip, LOBBY_PORT);
			System.out.println("connected to lobby server " + lobby_ip);
			InputStream in = socket.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(in));
			StringTokenizer st = new StringTokenizer(rd.readLine());
			
			System.out.println("<current chatting servers info>");
			
			String str;
			do {
				str = rd.readLine();
				System.out.println(str);
			} while (!str.equals("end_server_info"));
			
			System.out.println("please put chatting server number to connect.");
			int input_num;
			input_num = sc.nextInt();
			
			OutputStream out = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			writer.println(Integer.toString(input_num));
			socket.close();
		} catch (Exception e) {
			System.out.println("failed to connect lobby server + " + lobby_ip);
			e.printStackTrace();
		}
		return null;
	}
}
