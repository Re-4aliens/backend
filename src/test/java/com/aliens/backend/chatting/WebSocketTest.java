package com.aliens.backend.chatting;

import com.aliens.backend.chatting.util.ChatClient;
import com.aliens.backend.global.property.WebSocketProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WebSocketTest {

    @Autowired
    private WebSocketProperties properties;
    private ChatClient chatClient;

    @BeforeEach
    public void setup() {
        chatClient = new ChatClient(properties);
    }

    @Test
    @DisplayName("웹소켓 연결 테스트")
    public void WebSocketConnection() throws Exception {
        //given
        //when
        StompSession session = chatClient.connect();
        //then
        assert session.isConnected();
    }
}