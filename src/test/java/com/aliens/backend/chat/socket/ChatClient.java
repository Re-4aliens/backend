package com.aliens.backend.chat.socket;

import com.aliens.backend.global.property.WebSocketProperties;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChatClient {
    private final String endPoint;
    private final String port;
    private final WebSocketStompClient stompClient;
    private final ChatClientHandler chatClientHandler;
    private StompSession session;

    public ChatClient(WebSocketProperties properties) {
        this.endPoint = properties.getEndpoint();
        this.port = properties.getPort();
        this.stompClient = createStompClient();
        this.chatClientHandler = new ChatClientHandler();
    }

    public StompSession connect(String accessToken) throws Exception {
        String url = "ws://localhost:" + port + endPoint;
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add("Authorization", accessToken);
        session = stompClient.connectAsync(url, headers, chatClientHandler).get();
        return session;
    }

    public List<Object> subscribe(Long roomId){
        List<Object> receiveMessage = new ArrayList<>();
        session.subscribe("/room/" + roomId, createFrameHandler(receiveMessage));
        return receiveMessage;
    }

    public void send(String destination, Object payload) {
        session.send(destination, payload);
    }

    private WebSocketStompClient createStompClient() {
        WebSocketClient webSocketClient = new SockJsClient(createTransport());
        WebSocketStompClient stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        return stompClient;
    }

    private List<Transport> createTransport() {
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private StompFrameHandler createFrameHandler(List<Object> receiveMessage) {
        return new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return byte[].class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                receiveMessage.add(payload);
            }
        };
    }
}
