package client;

import lobbyserver.client.LobbyServerClient;
import loginserver.loginclient;
import chattingserver.ChattingClient;

public class Main {
	// delicious burrito
	public static void main(String[] args) {
		ILoginClient login_server = new loginclient();
		ILobbyClient lobby_server = new LobbyServerClient();
		IChattingClient chatting_server = new ChattingClient();
		boolean result = login_server.connectLoginServer();
		if (result) {
			String ip_address = lobby_server.connectLobbyServer();
			chatting_server.connectChattingServer(ip_address);
		} else {
			System.out.println("do not connect to lobby server");
		}
	}
}
