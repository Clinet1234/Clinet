package chattingserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

class ChattingLobbbyConnecter extends Thread {
	Socket socket = null;
	String ip = null;
	static int LobbyPort = 3400;
	
	public ChattingLobbbyConnecter(String ip) {
		try {
			this.socket = new Socket(ip, LobbyPort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			OutputStream out = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			
			String readValue;
			System.out.println("[채팅서버] <> [로비서버] 연결성공");
			while((readValue = reader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(readValue);
				if(readValue.equals("1")) {
					writer.println(ChattingServerMain.info.size());
					for(String key : ChattingServerMain.info.keySet()) {
						writer.println(ChattingServerMain.info.get(key).name);
					}
				}
				else if(st.nextToken().equals("2")) {
					ChattingServerMain.check.put(st.nextToken(), true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

public class ChattingServerMain extends Thread {
	static HashMap<String, ClientInfo> info = new HashMap<String, ClientInfo>();
	static HashMap<String, Boolean> check = new HashMap<String, Boolean>();
	
	Socket socket = null;
	InetAddress ip;
	
	public ChattingServerMain(String name, Socket socket) {
		this.socket = socket;
		this.ip = socket.getInetAddress();
		info.put(socket.getInetAddress().toString(), new ClientInfo(name, socket));
	}
	
	public ChattingServerMain(Socket socket) {
		this.socket = socket;
	}
	
	public void run() {
		try {
			InputStream input = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			
			OutputStream out = socket.getOutputStream();
			PrintWriter writer = new PrintWriter(out, true);
			
			String readValue;
			String IP = socket.getInetAddress().toString();
			System.out.println("[채팅서버]: IP주소: "+ IP +", 닉네임: "+(info.get(IP).name)+"의 사용자가 접속하였습니다."
			+" 현재 접속 인원: "+info.size());
			
			String showName = info.get(IP).name;
			
			for(String key : info.keySet()) {
				writer.println("["+showName+"] 님이 접속함");
			}
			
			while((readValue = reader.readLine()) != null) {
				for(String key : info.keySet()) {
					out = info.get(key).socket.getOutputStream();
					writer = new PrintWriter(out, true);
					writer.println("["+showName+"]: "+readValue);
				}
			}
			info.remove(IP);
			for(String key : info.keySet()) {
				writer.println("["+showName+"] 님이 접속 종료");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {		
			System.out.println("IP주소: "+this.ip+" 접속종료, 현재 접속 인원: "+info.size());	
		}
	}
	
	public static void main(String[] args) {
		String LobbyIp = null;
		Scanner sc = new Scanner(System.in);
		
		LobbyIp = sc.nextLine();
		ChattingLobbbyConnecter chattinglobbbyconnecter = new ChattingLobbbyConnecter(LobbyIp);
		
		chattinglobbbyconnecter.run();
		String name = null;
		try {
			int ChattingSocketPort = 4000;
			ServerSocket ChattingServerSocket = new ServerSocket(ChattingSocketPort);
			System.out.println("포트번호: "+ ChattingSocketPort +"로 채팅서버가 열렸습니다");
			
			while(true) {
				Socket socketUser = ChattingServerSocket.accept();
				InputStream input = socketUser.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				name = reader.readLine();
				
				OutputStream out = socketUser.getOutputStream();
				PrintWriter writer = new PrintWriter(out, true);
				
				if(check.get(socketUser.getInetAddress().toString()) == null) {
					System.out.println("[채팅서버]: IP주소 :"+ socketUser.getInetAddress() + "로 허가되지 않은 사용자 접근시도(실패)");
					writer.println("허가되지 않았습니다");
				}
				else {
					Thread th = new ChattingServerMain(name, socketUser);
					th.start();
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}