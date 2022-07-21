package loginserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import client.ILoginClient;

public class loginclient implements ILoginClient {

	@Override
	public boolean connectLoginServer()  {
		try {
		Scanner sc = new Scanner(System.in);
		String ip;
		ip = sc.nextLine();
		
		Socket socket = new Socket(ip,1233);
		InputStream send = socket.getInputStream();
		DataInputStream ins = new DataInputStream(send);
		OutputStream ou = socket.getOutputStream();
		DataOutputStream ous = new DataOutputStream(ou);

	System.out.println("로그인 : 1 회원가입 : 2");
	int ch;
	while(true) {
		ch = sc.nextInt();
		String space = sc.nextLine();
		ous.writeInt(ch);
		if (ch == 1 || ch == 2) {
			break;
		}
		System.out.println("다시 입력해주세요");
	}

		

		
	if (ch == 2) {
		while(true) {
		
			System.out.print("id 입력: ");
			String UserId = sc.nextLine();
			System.out.print("Password 입력: ");
			String UserPw = sc.nextLine();
			System.out.print("Password 재입력: ");
			String UserPw2 = sc.nextLine();
			if (!UserPw.equals(UserPw2)) {
				System.out.println("비밀번호가 서로 다릅니다");
				continue;
			}
			ous.writeUTF(UserId);
			ous.writeUTF(UserPw);
			int num = ins.readInt();
			if (num == 0) {
				System.out.println("이미 있는 아이디입니다");
				continue;
			}
			else{
				System.out.println("회원가입이 완료되었습니다");
				break;
			}
			}
		
		while(true) {
			System.out.print("id: ");
			String id = sc.nextLine();
			System.out.print("Password: ");
			String pw = sc.nextLine();
			ous.writeUTF(id);
			ous.writeUTF(pw);
			int num = ins.readInt();
			if (num == 1) {
				break;
			}
			System.out.println("아이디 또는 비밀번호가 틀렸거나 없는 아이디입니다");
	}
			
		
	}
	
	if (ch == 1) {		
		while(true) {
			System.out.print("id: ");
			String id = sc.nextLine();
			System.out.print("Password: ");
			String pw = sc.nextLine();
			ous.writeUTF(id);
			ous.writeUTF(pw);
			int num = ins.readInt();
			if (num == 1) {
				break;
			}
			System.out.println("아이디 또는 비밀번호가 틀렸거나 없는 아이디입니다");
	}
	System.out.println("로그인 되었습니다");

	sc.close();
	socket.close();
	
	}
	

		}catch(Exception e) {e.printStackTrace();}	
		return false;
	}

}
