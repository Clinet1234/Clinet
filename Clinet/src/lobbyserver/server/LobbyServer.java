package lobbyserver.server;

import java.net.ServerSocket;
import java.util.ArrayList;

import java.net.Socket;

public class LobbyServer {

	private static LobbyServer INSTANCE = new LobbyServer();
	public static final int CLIENT_PORT = 3000;

	private ArrayList<LobbyChattingServerListener> chatting_servers;
	private boolean isRunning;
	
	private LobbyServer() {
		chatting_servers = new ArrayList<LobbyChattingServerListener>();
	}

	public static LobbyServer getInstance() {
		return INSTANCE;
	}

	public void init() {
		System.out.println("lobby server started init");
		addChatting();
		this.isRunning = true;
		System.out.println("lobby server finished init");
	}

	public void run() {
		System.out.println("lobby server started running");
		try {
			ServerSocket server_socket_client = new ServerSocket(CLIENT_PORT);
			while (isRunning) {
				Socket socket_client = server_socket_client.accept();
				System.out.println("client accepted");
				Thread th = new LobbyClientListener(server_socket_client, socket_client);
				th.start();
			}
			server_socket_client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("lobby server finished running");
		this.quit();
	}

	private void quit() {
		System.out.println("lobby server started quit");

		int cnt = chatting_servers.size() - 1;
		for (int i = 0; i < cnt; ++i) {
			chatting_servers.get(i).closeServer();
		}
		
		System.out.println("lobby server finished quit");
	}
	
	public void addChatting() {
		chatting_servers.add(new LobbyChattingServerListener(chatting_servers.size()));
	}
	
	public void allow_user_in_chatting_server(int chatting_server_num, String userIP) {
		if (chatting_server_num < chatting_servers.size() - 1) {
			chatting_servers.get(chatting_server_num).allow_user_in_chatting_server(userIP);
		} else {
			System.out.println("requested chatting server id " + chatting_server_num + " from client " + userIP + " is not valid.");
		}
	}

	public String getChattingServersInfo() {
		int cnt = chatting_servers.size() - 1;
		String res = "server count : " + Integer.toString(cnt) + "\n";
		for (int i = 0; i < cnt; ++i) {
			res += "server " + i + " : \n";
			res += chatting_servers.get(i).get_user() + "\n";
		}
		res += "\nend_server_info";
		return res;
	}
}
