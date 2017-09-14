package com.jimi.ws.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.jimi.ws.handler.MessageWebSocketHandler;
import com.jimi.ws.interceptor.MessageWebSocketInterceptor;

@Configuration
@EnableWebSocket
public class MessageWebSocketConfig implements WebSocketConfigurer {

	@Override  
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {  
        registry.addHandler(myhandler(), "/websocket").addInterceptors(myInterceptors()).setAllowedOrigins("*");  
        registry.addHandler(myhandler(), "/sockjs/websocket").addInterceptors(myInterceptors()).withSockJS();  
    }  
  
    @Bean  
    public WebSocketHandler myhandler() {  
        return new MessageWebSocketHandler();  
    }  
  
    @Bean  
    public HandshakeInterceptor myInterceptors() {  
        return new MessageWebSocketInterceptor();  
    }  

}
