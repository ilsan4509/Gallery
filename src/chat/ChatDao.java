package chat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ChatDao {
	private Connection conn;
	public ChatDao(){
		try{
			String dbURL = "jdbc:mysql://localhost:3306/GCHAT?useSSL=false&serverTimezone=Asia/Seoul";
			String dbID = "root";
			String dbPassword = "12345";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public ArrayList<Chat> getChatList(String nowTime){
		ArrayList<Chat> chatList = null;
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		String SQL = "SELECT * FROM CHAT WHERE chatTime > ? ORDER BY chatTime";
		try{
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, nowTime);
			rs = pstmt.executeQuery();
			chatList = new ArrayList<Chat>();
			while(rs.next()){
				Chat chat = new Chat();
				chat.setChatID(rs.getInt("chatID"));
				chat.setChatName(rs.getString("chatName"));
//				출력오류를 방지하기위해 replace로 html로 바꿔주기
				chat.setChatContent(rs.getString("chatContent").replaceAll(" ", "&nbsp;").replace("<", "&lt;").replace(">", "&gt").replace("\n", "<br>")   );
				chat.setChatTime(rs.getString("chatTime"));
				chatList.add(chat);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return chatList;
		
	}
	public ArrayList<Chat> getChatListByRecent(int number){
		ArrayList<Chat> chatList = null;
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		String SQL = "SELECT * FROM CHAT WHERE chatID > (SELECT MAX(chatID) - ? FROM CHAT) ORDER BY chatTime";
		try{
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, number);
			rs = pstmt.executeQuery();
			chatList = new ArrayList<Chat>();
			while(rs.next()){
				Chat chat = new Chat();
				chat.setChatID(rs.getInt("chatID"));
				chat.setChatName(rs.getString("chatName"));
//				출력오류를 방지하기위해 replace로 html로 바꿔주기
				chat.setChatContent(rs.getString("chatContent").replaceAll(" ", "&nbsp;").replace("<", "&lt;").replace(">", "&gt").replace("\n", "<br>")   );
				chat.setChatTime(rs.getString("chatTime"));
				chatList.add(chat);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return chatList;
		
	}
	
	public ArrayList<Chat> getChatListByRecent(String chatID){
		ArrayList<Chat> chatList = null;
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		String SQL = "SELECT * FROM CHAT WHERE chatID > ? ORDER BY chatTime";
		try{
			pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, Integer.parseInt(chatID));
			rs = pstmt.executeQuery();
			chatList = new ArrayList<Chat>();
			while(rs.next()){
				Chat chat = new Chat();
				chat.setChatID(rs.getInt("chatID"));
				chat.setChatName(rs.getString("chatName"));
//				출력오류를 방지하기위해 replace로 html로 바꿔주기
				chat.setChatContent(rs.getString("chatContent").replaceAll(" ", "&nbsp;").replace("<", "&lt;").replace(">", "&gt").replace("\n", "<br>")   );
				chat.setChatTime(rs.getString("chatTime"));
				chatList.add(chat);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return chatList;
		
	}
	public int submit(String chatName, String chatContent){
		PreparedStatement pstmt = null;
		ResultSet rs =null;
		String SQL = "INSERT INTO CHAT VALUES (NULL, ?, ?, now())";
		try{
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1,  chatName);
			pstmt.setString(2,  chatContent);
			return pstmt.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(rs != null) rs.close();
				if(pstmt != null) pstmt.close();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return -1;
	}
}
