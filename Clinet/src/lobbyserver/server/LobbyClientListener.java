package lobbyserver.server;

import java.net.Socket;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.BufferedReader;
import java.util.StringTokenizer;
import java.io.InputStreamReader;


public class LobbyClientListener extends Thread {

    private Socket socket;

    public LobbyClientListener(Socket s) {
        this.socket = s;
    }

    @Override
    public void run() {

        try {
            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);
            writer.println(LobbyServer.getInstance().getChattingServersInfo());
            
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringTokenizer st = new StringTokenizer(reader.readLine());
			int cnt = Integer.parseInt(st.nextToken());
			
			System.out.println("requested chatting server number from client : " + cnt);
			String str = LobbyServer.getInstance().getChattingServerIp(cnt);
			System.out.println("send chatting server ip for request : ip -" + str);
			writer.println(str);
            LobbyServer.getInstance().allow_user_in_chatting_server(cnt, socket.getInetAddress().toString());
            
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
