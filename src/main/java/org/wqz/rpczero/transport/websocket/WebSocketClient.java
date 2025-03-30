package org.wqz.rpczero.transport.websocket;


import jakarta.websocket.*;

import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class WebSocketClient {

    @OnOpen
    public void onOpen(Session session) {
        try {
            // 发送请求
            String request = "hello";
            session.getBasicRemote().sendText(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received from server: " + message);
    }

    @OnClose
    public void onClose() {
        System.out.println("Connection closed");
    }

    public static void main(String[] args) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            String uri = "ws://localhost:8025/ws/websocket";
            container.connectToServer(WebSocketClient.class, URI.create(uri));
        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
        }
    }
}