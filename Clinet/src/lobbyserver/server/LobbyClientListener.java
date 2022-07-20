package lobbyserver.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.io.InputStreamReader;


public class LobbyClientListener extends Thread {

	private ServerSocket ssocket;
    private Socket socket;

    public LobbyClientListener(ServerSocket sv, Socket s) {
    	this.ssocket = sv;
        this.socket = s;
    }

    @Override
    public void run() {
        super.run();

        try {
            OutputStream out = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(out, true);
            writer.println(LobbyServer.getInstance().getChattingServersInfo());
            
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringTokenizer st = new StringTokenizer(reader.readLine());
			int cnt = Integer.parseInt(st.nextToken());

            LobbyServer.getInstance().allow_user_in_chatting_server(cnt, Integer.toString(ssocket.getLocalPort()));

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
			ssocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
