package lobbyserver.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.StringTokenizer;

import client.ILobbyClient;

public class LobbyServerClient implements ILobbyClient {
	
	public static String LOBBY_IP = "";
	public static int LOBBY_PORT = 0;
	
	@Override
	public String connectLobbyServer() {
		System.out.println("try to connect lobby server...");
		try {
			Socket socket = new Socket(LOBBY_IP, LOBBY_PORT);
			System.out.println("connected to lobby server " + LOBBY_IP);
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
			Scanner sc = new Scanner(System.in);
			input_num = sc.nextInt();
			
			OutputStream out = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			writer.println(Integer.toString(input_num));
			socket.close();
		} catch (Exception e) {
			System.out.println("failed to connect lobby server + " + LOBBY_IP);
			e.printStackTrace();
		}
		return null;
	}
}
