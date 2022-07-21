package lobbyserver.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class LobbyChattingServerListener {

    private Socket socket;
    private int server_id;

    public LobbyChattingServerListener(Socket socket, int serverID) {
    	this.socket = socket;
        server_id = serverID;
    }
    
    public String get_user() {
        try {
            String res = "";

            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);
            writer.println("1");
            
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringTokenizer st = new StringTokenizer(reader.readLine());
			int cnt = Integer.parseInt(st.nextToken());
            res += "joined people : " + cnt + "\n";
            for (int i = 0; i != cnt; ++i) {
                res += reader.readLine();
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public String getIP() {
    	return socket.getInetAddress().toString();
    }
    
    public void allow_user_in_chatting_server(String userip) {
        OutputStream out;
		try {
			out = socket.getOutputStream();
	        PrintWriter writer = new PrintWriter(out, true);
	        writer.println("2 " + userip);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void closeServer() {
    	try {
            System.out.println("chatting server " + server_id + " closed");
            socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
