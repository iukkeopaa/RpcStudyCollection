package org.wqz.rpczero.transport.websocket;


import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;

@ServerEndpoint("/websocket")
public class WebSocketServer {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("New client connected");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received from client: " + message);
        try {
            // 处理请求
            String response = handleRequest(message);
            session.getBasicRemote().sendText(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Client disconnected");
    }

    private String handleRequest(String request) {
        // 简单返回请求的大写形式作为响应
        return request.toUpperCase();
    }
}