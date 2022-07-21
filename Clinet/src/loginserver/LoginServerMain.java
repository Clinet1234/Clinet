package loginserver;

import java.sql.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataOutputStream;

class server_thread extends Thread{
	static Connection conn;
	static String pw;
	static int check;
	
	private Socket socketUser;

	public server_thread(Socket socketUser){
		this.socketUser = socketUser;
		
	}

	public void run() {
		try {
	       System.out.println("Client가 접속함 : " + socketUser.getLocalAddress()); 

			OutputStream ou = socketUser.getOutputStream();
			DataOutputStream ous = new DataOutputStream(ou);
			InputStream send = socketUser.getInputStream();
			DataInputStream ins = new DataInputStream(send);
			
			String dbURL="jdbc:mysql://localhost:3306/user";
			String dbID="root"; 
			String dbPassword="2345";
			
			try{
				Class.forName("com.mysql.cj.jdbc.Driver"); 
				conn = DriverManager.getConnection(dbURL, dbID, dbPassword); 
				

			int ch;
			while (true) {
				ch = ins.readInt();
				if (ch == 1 || ch == 2) {
					break;
				}
			}
			if (ch == 1) {
				while (true) {
				String id = ins.readUTF();
				String input_pw = ins.readUTF();
				login(id);
				if (input_pw.equals(pw)) {
					int num = 1;
					ous.writeInt(num);
					break;
				}
				else {
					int num = 0;
					ous.writeInt(num);
				}
			}
			}
			
				if (ch == 2) {
					while (true) {
					String id = ins.readUTF();
					String input_pw = ins.readUTF();
					register(id,input_pw);
					if (check == 0) {
						int num = 0;
						ous.writeInt(num);
					}
					else {
						int num = 1;
						ous.writeInt(num);
						break;
					}
					
				}
				while (true) {
					String id = ins.readUTF();
					String input_pw = ins.readUTF();
					login(id);
					if (input_pw.equals(pw)) {
						int num = 1;
						ous.writeInt(num);
						break;
					}	
					else {
						int num = 0;
						ous.writeInt(num);
					}
			
			}
				}
			} catch(ClassNotFoundException e){
				e.printStackTrace();
			} catch(SQLException e){
				e.printStackTrace();
			} finally{
	            try{
	                if(conn != null && !conn.isClosed())
	                    conn.close();
		
	            } catch(SQLException e){
	                e.printStackTrace();
	            }
			}
		} catch(Exception e){
			e.printStackTrace();
	}
}
	
	public static void register(String id, String pw){ 
		String SQL = "SELECT * from user where id = ?";
        PreparedStatement pstmt = null;		
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, id); 
			pstmt.executeQuery(); 
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				check = 0;
			}
			else {
				check = 1;
				SQL = "INSERT INTO user VALUES(?, ?)"; 
		        pstmt = null;
				
				try {
					pstmt = conn.prepareStatement(SQL);
					pstmt.setString(1, id); 
					pstmt.setString(2, pw);
					pstmt.executeUpdate(); 
				} catch(Exception e2){
					e2.printStackTrace();
					
				} finally {
					try {
		                if(pstmt!=null && !pstmt.isClosed()) {
		                    pstmt.close();
					}
				} catch (Exception e2) {}
					
			}
			}
		} catch(Exception e){
			e.printStackTrace();
		
		
		}
	}
	public static void login(String id) {
		String SQL = "SELECT * from user where id = ?";
        PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1,id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				pw = rs.getString("pw");
				check = 1;
			}
			else {
				check = 0;
			}
		} catch (Exception e) {} 
		finally {		
			try { 
				 if(pstmt!=null && !pstmt.isClosed()) {
	                pstmt.close();
	               
			} 
			} catch (Exception e2) {}
		}
	
			
	}
		
}


public class LoginServerMain {
	
	public static void main(String[] args) throws IOException {
		int socketPort = 1233;
		ServerSocket serverSocket = new ServerSocket(socketPort); 
		System.out.println("socket : " + socketPort + "으로 서버가 열렸습니다");
		while(true) {
			Socket socketUser = serverSocket.accept();
			Thread th = new server_thread(socketUser);
			th.start();
		}
	}
}
