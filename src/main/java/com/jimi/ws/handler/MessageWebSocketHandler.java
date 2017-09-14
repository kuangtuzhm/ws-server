package com.jimi.ws.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class MessageWebSocketHandler implements WebSocketHandler {

	public static final Map<String, HashMap<Long, WebSocketSession>> userSocketSessionMap;  
	
	public static int sessionCount;
	  
    static {  
        userSocketSessionMap = new HashMap<String, HashMap<Long, WebSocketSession>>(50000);  
        sessionCount = 0;
    }  
      
      
    @Override  
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {  
        String jspCode = (String) session.getAttributes().get("jspCode");
        long sessionTime = (long) session.getAttributes().get("sessionTime"); 
        HashMap<Long, WebSocketSession> list = userSocketSessionMap.get(jspCode);
        if (list == null) {  
        	list = new HashMap<Long, WebSocketSession>(1);
        	list.put(sessionTime, session);
        	System.out.println("新建连接jspCode="+jspCode+";sessionTime="+sessionTime);
            userSocketSessionMap.put(jspCode, list);  
        } 
        else
        {
        	list.put(sessionTime, session);
        	System.out.println("增加连接jspCode="+jspCode+";sessionTime="+sessionTime);
        }
        sessionCount++;
        System.out.println("服务器建立websocket数量="+sessionCount);    
    }  
  
    @Override  
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {  

        //Message msg=new Gson().fromJson(message.getPayload().toString(),Message.class);  
        //msg.setDate(new Date());  
//      sendMessageToUser(msg.getTo(), new TextMessage(new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(msg)));  
    	System.out.println("服务器收到的消息:"+message.getPayload().toString());
        session.sendMessage(new TextMessage("服务器收到的消息:"+message.getPayload().toString()));  
    }  
  
    @Override  
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {  

        if (session.isOpen()) {  
            session.close();  
        }  
        String jspCode = (String)session.getAttributes().get("jspCode");
        System.out.println("Socket会话已经移除:" + jspCode);  
        HashMap<Long, WebSocketSession> list = userSocketSessionMap.get(jspCode);
        if(null != list && list.size() > 0)
        {
        	WebSocketSession sessionInList = list.remove((Long)session.getAttributes().get("sessionTime"));
	        if(null != sessionInList)
	        {
	        	sessionCount--;
	        }
        }
    }  
  
    @Override  
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {  
        String jspCode = (String)session.getAttributes().get("jspCode");
        System.out.println("Websocket:" + jspCode + "已经关闭");  
        HashMap<Long, WebSocketSession> list = userSocketSessionMap.get(jspCode);
        if(null != list && list.size() > 0)
        {
        	WebSocketSession sessionInList = list.remove((Long)session.getAttributes().get("sessionTime"));
	        if(null != sessionInList)
	        {
	        	sessionCount--;
	        }
        }
    }  
  
    @Override  
    public boolean supportsPartialMessages() {  
        // TODO Auto-generated method stub  
        return false;  
    }  

      
    /** 
     * 给在线用户的实时工程检测页面发送消息 
     *  
     * @param message 
     * @throws IOException 
     */  
    public void sendMessageToJsp(final TextMessage message,String jspCode) throws IOException {  
    	
    	HashMap<Long, WebSocketSession> sessionList = userSocketSessionMap.get(jspCode);
    	if(null != sessionList && sessionList.size() > 0)
    	{
    		Set<Entry<Long, WebSocketSession>> set = sessionList.entrySet();
    		for(Entry<Long, WebSocketSession> entry : set)
    		{
    			WebSocketSession session = entry.getValue();
		    	if(null != session && session.isOpen())
		    	{
		    		System.out.println("服务端向jspCode="+session.getAttributes().get("jspCode")+",sessionTime="+session.getAttributes().get("sessionTime")+"主动发起响应,message="+message.getPayload());
		    		try {
						session.sendMessage(message);
					} catch (Exception e) {
						e.printStackTrace();
					}
		    	}
		    	else
		    	{
		    		if(session != null)
		    		{
		    			System.out.println("session.isOpen()="+session.isOpen());
		    		}
		    		System.out.println("服务端主动发起响应,连接已关闭");
		    		userSocketSessionMap.remove(jspCode);
		    	}
    		}
    	}
    }  
}
