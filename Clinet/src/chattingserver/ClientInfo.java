package chattingserver;

import java.net.Socket;

public class ClientInfo {
	public String name;
	public Socket socket;
	
	public ClientInfo(String name, Socket socket) {
		this.name = name;
		this.socket = socket;
	}
	
}
