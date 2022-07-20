package client;

public class Main {
	public static void main(String[] args) {
		ILoginserver login_server; // = new ~
		ILobbyServer lobby_server; // = new ~
		IChattingServer chatting_server; //  = new ~
		boolean result = login_server.connectLoginServer();
		if (result) {
			String ip_address = lobby_server.connectLobbyServer();
			chatting_server.connectChattingServer(ip_address);
		} else {
			System.out.println("do not connect to lobby server");
		}
	}
}
