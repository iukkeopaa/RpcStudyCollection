package org.wqz.rpczero.transport.websocket;


//import jakarta.websocket.DeploymentException;
//
//import java.io.IOException;
//
//public class WebSocketServerStarter {
//    public static void main(String[] args) {
//        Server server = new Server("localhost", 8025, "/ws", WebSocketServer.class);
//        try {
//            server.start();
//            System.out.println("WebSocket server started");
//            System.in.read();
//        } catch (DeploymentException | IOException e) {
//            e.printStackTrace();
//        } finally {
//            server.stop();
//        }
//    }
//}