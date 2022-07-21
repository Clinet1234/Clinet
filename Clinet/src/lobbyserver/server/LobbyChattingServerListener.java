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

public class LobbyChattingServerListener extends Thread {

    private ServerSocket chatting_server;
    private Socket chatting_socket;
    private int server_id;

    public LobbyChattingServerListener(ServerSocket chatting_server, int serverID) {
    	this.chatting_server = chatting_server;
        server_id = serverID;
    }
    
    @Override
    public void run() {
        try {
            chatting_socket = chatting_server.accept();
            System.out.println("new chatting server " + server_id + " accepted : " + 
                Integer.toString(chatting_server.getLocalPort()));
            LobbyServer.getInstance().addChatting();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public String get_user() {
        try {
            String res = "";

            OutputStream out = chatting_socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);
            writer.println("1");
            
            InputStream in = chatting_socket.getInputStream();
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
    
    public void allow_user_in_chatting_server(String userip) {
        OutputStream out;
		try {
			out = chatting_socket.getOutputStream();
	        PrintWriter writer = new PrintWriter(out, true);
	        writer.println("2 " + userip);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void closeServer() {
    	try {
            System.out.println("chatting server " + server_id + " closed");
			chatting_server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
