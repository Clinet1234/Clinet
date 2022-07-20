package lobbyserver.server;

public class LobbyServerMain {
	public static void main(String[] args) {
		LobbyServer lobby = LobbyServer.getInstance();
		lobby.init();
		lobby.run();
	}
}
