package chat;

import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/ChatListServlet")
public class ChatListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		String listType = request.getParameter("listType");
		if(listType == null || listType.equals("")) response.getWriter().write("");
		else if(listType.equals("today")) response.getWriter().write(getToday());
		else if(listType.equals("ten")) response.getWriter().write(getTen());
		else {
			try{
				Integer.parseInt(listType);
				response.getWriter().write(getID(listType));
			}catch(Exception e){
				response.getWriter().write("");
			}
		}
	}
	public String getToday(){
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");//제이슨 형태 큰따옴표로 변수이름 정해서 클라이언트로 보내지고 파싱해서 보여지게..
		ChatDao chatDao = new ChatDao();
		ArrayList<Chat> chatList = chatDao.getChatList(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		for(int i=0; i<chatList.size(); i++){
			result.append("[{\"value\": \"" + chatList.get(i).getChatName() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime()+ "\"}]");
			if(i != chatList.size()-1) result.append(",");
		}
//		가장 최근 채팅한 ID의 값 불러오기 
		result.append("], \"last\":\""+ chatList.get(chatList.size()-1).getChatID() +"\"}");
		return result.toString();
	}
	
	public String getTen(){
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");//제이슨 형태 큰따옴표로 변수이름 정해서 클라이언트로 보내지고 파싱해서 보여지게..
		ChatDao chatDao = new ChatDao();
		ArrayList<Chat> chatList = chatDao.getChatListByRecent(10);
		for(int i=0; i<chatList.size(); i++){
			result.append("[{\"value\": \"" + chatList.get(i).getChatName() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime()+ "\"}]");
			if(i != chatList.size()-1) result.append(",");
		}
//		가장 최근 채팅한 ID의 값 불러오기 
		result.append("], \"last\":\""+ chatList.get(chatList.size()-1).getChatID() +"\"}");
		return result.toString();
	}
	public String getID(String chatID){
		StringBuffer result = new StringBuffer("");
		result.append("{\"result\":[");//제이슨 형태 큰따옴표로 변수이름 정해서 클라이언트로 보내지고 파싱해서 보여지게..
		ChatDao chatDao = new ChatDao();
		ArrayList<Chat> chatList = chatDao.getChatListByRecent(chatID);
		for(int i=0; i<chatList.size(); i++){
			result.append("[{\"value\": \"" + chatList.get(i).getChatName() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatContent() + "\"},");
			result.append("{\"value\": \"" + chatList.get(i).getChatTime()+ "\"}]");
			if(i != chatList.size()-1) result.append(",");
		}
//		가장 최근 채팅한 ID의 값 불러오기 
		result.append("], \"last\":\""+ chatList.get(chatList.size()-1).getChatID() +"\"}");
		return result.toString();
	}
}
