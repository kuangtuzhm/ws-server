package com.jimi.controller;

import java.io.IOException;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

import com.jimi.ws.handler.MessageWebSocketHandler;

@Controller
@RequestMapping("/ws")
public class WSTestController {
	
	@Resource
	MessageWebSocketHandler messageWebSocketHandler;

	@RequestMapping("/index")
    String home() {
        return "index";
    }

	@RequestMapping(value = "/testWebSocket", method = {
			RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String testWebSocket(@RequestParam("jspCode") String jspCode) throws IOException {
		System.out.println("jspCode="+jspCode);
		messageWebSocketHandler.sendMessageToJsp(new TextMessage("WSTestController/testWebSocket:time="
								+System.currentTimeMillis()), jspCode);
		return "1";
	}
	
	@RequestMapping(value = "/sendMsg", method = {
			RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String sendMsg(@RequestParam("jspCode") String jspCode) throws IOException {
		System.out.println("jspCode="+jspCode);
		messageWebSocketHandler.sendMessageToJsp(new TextMessage("WSTestController/sendMsg="+System.currentTimeMillis()), jspCode);
		return "1";
	}
}
