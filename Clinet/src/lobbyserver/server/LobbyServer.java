package lobbyserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

import java.net.Socket;

public class LobbyServer {

	private static LobbyServer INSTANCE = new LobbyServer();
	public static final int CLIENT_PORT = 3000;
	public static final int CHATTING_PORT = 3400;
	private static ServerSocket chatting_socket;
	private static ServerSocket client_socket;

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
        try {
        	System.out.println("try to make socket for chatting server");
        	chatting_socket = new ServerSocket(CHATTING_PORT);
        	System.out.println("made socket for chatting server");
        	
        	System.out.println("try to make socket for client");
			client_socket = new ServerSocket(CLIENT_PORT);
        	System.out.println("made socket for client");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.isRunning = true;
		System.out.println("lobby server finished init");
	}

	public void run() {
		System.out.println("lobby server started running");
		
		class LobbyChattingServerHandler extends Thread {
			boolean isRunning = true;
			@Override
			public void run() {
				System.out.println("thread for accepting chatting servers started");
				try {
					while (isRunning) {
						Socket socket_chatting = chatting_socket.accept();
						System.out.println("new chatting server accepted from ip " + socket_chatting.getInetAddress().toString());
						addChatting(socket_chatting);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("thread for accepting chatting servers ends");
			}
		}
		System.out.println("starting to make thread for accepting chatting servers..");
		Thread lobbyChattingServerHandler = new LobbyChattingServerHandler();
		lobbyChattingServerHandler.run();
		System.out.println("finished to make thread for accepting chatting servers");
		
		try {
			System.out.println("started to accept new connection from clients");
			while (isRunning) {
				Socket socket_client = client_socket.accept();
				System.out.println("new client accepted from ip " + socket_client.getInetAddress().toString());
				Thread th = new LobbyClientListener(socket_client);
				th.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("lobby server finished running");
		this.quit();
	}

	private void quit() {
		System.out.println("lobby server started quit");
		
		System.out.println("closing all socket for chatting server...");
		int cnt = chatting_servers.size() - 1;
		for (int i = 0; i < cnt; ++i) {
			chatting_servers.get(i).closeServer();
		}
		System.out.println("closed all socket for chatting server");
		
		try {
			System.out.println("try to close serversocket for client...");
			client_socket.close();
			System.out.println("closed serversocket for client");
			
			System.out.println("try to close serversocket for chatting server...");
			chatting_socket.close();
			System.out.println("closed serversocket for chatting server");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("lobby server finished quit");
	}
	
	public void addChatting(Socket socket) {
		chatting_servers.add(new LobbyChattingServerListener(socket, chatting_servers.size()));
	}
	
	public void allow_user_in_chatting_server(int chatting_server_num, String userIP) {
		if (chatting_server_num < chatting_servers.size()) {
			chatting_servers.get(chatting_server_num).allow_user_in_chatting_server(userIP);
		} else {
			System.out.println("requested chatting server id " + chatting_server_num + " from client " + userIP + " is not valid.");
		}
	}
	
	public String getChattingServersInfo() {
		int cnt = chatting_servers.size();
		String res = "server count : " + Integer.toString(cnt) + "\n";
		for (int i = 0; i < cnt; ++i) {
			res += "server " + i + " : \n";
			res += chatting_servers.get(i).get_user() + "\n";
		}
		res += "\nend_server_info";
		return res;
	}
}
