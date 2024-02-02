package com.aliens.backend.chatting.util;

import com.aliens.backend.global.property.WebSocketProperties;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

public class ChatClient {
    private final String endPoint;
    private final String port;
    private final WebSocketStompClient stompClient;
    private final ChatClientHandler chatClientHandler;

    public ChatClient(WebSocketProperties properties) {
        this.endPoint = properties.getEndpoint();
        this.port = properties.getPort();
        this.stompClient = createStompClient();
        this.chatClientHandler = new ChatClientHandler();
    }

    public StompSession connect() throws Exception {
        String url = "ws://localhost:" + port + endPoint;
        StompSession session = stompClient.connect(url, chatClientHandler).get();
        return session;
    }

    private WebSocketStompClient createStompClient() {
        WebSocketClient webSocketClient = new StandardWebSocketClient();
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        return stompClient;
    }
}
