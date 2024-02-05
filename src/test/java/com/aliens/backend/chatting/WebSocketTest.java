package com.aliens.backend.chatting;

import com.aliens.backend.chat.controller.ChatController;
import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.chatting.util.ChatClient;
import com.aliens.backend.global.property.WebSocketProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.stomp.StompSession;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class WebSocketTest {

    @Autowired
    private WebSocketProperties properties;
    @MockBean
    private ChatController chatController;
    private ChatClient chatClient;

    @BeforeEach
    public void setup() {
        chatClient = new ChatClient(properties);
    }

    @Test
    @DisplayName("웹소켓 - 연결")
    public void WebSocketConnection() throws Exception {
        //when
        StompSession session = chatClient.connect();
        //then
        assert session.isConnected();
    }

    @Test
    @DisplayName("웹소켓 - 메시지 전송 요청시 sendMessage() 호출")
    public void WebSocketSendMessage() throws Exception {
        //given
        StompSession session = chatClient.connect();
        String type = "NORMAL";
        String content = "Hello";
        Long roomId = 1L;
        Long senderId = 1L;
        Long receiverId = 2L;
        MessageSendRequest messageSendRequest = new MessageSendRequest(type, content, roomId, senderId, receiverId);
        //when
        session.send(properties.getRequest()+"/send", messageSendRequest);
        //then
        verify(chatController, timeout(100).times(1)).sendMessage(messageSendRequest);
    }

    @Test
    @DisplayName("웹소켓 - 읽음 처리 요청 시 readMessage() 호출")
    public void WebSocketReadMessage() throws Exception {
        //given
        StompSession session = chatClient.connect();
        //when
        session.send(properties.getRequest()+"/read","");
        //then
        verify(chatController, timeout(100).times(1)).readMessage();
    }

    @Test
    @DisplayName("웹소켓 - 채팅 요약 정보 요청 시 getChatSummary() 호출")
    public void WebSocketGetChatSummary() throws Exception {
        //given
        StompSession session = chatClient.connect();
        //when
        session.send(properties.getRequest()+"/summary","");
        //then
        verify(chatController, timeout(100).times(1)).getChatSummary();
    }
}