package com.aliens.backend.chatting.socket;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.chat.controller.ChatController;
import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.chat.controller.dto.request.ReadRequest;
import com.aliens.backend.chat.domain.MessageType;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.global.property.WebSocketProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.messaging.simp.stomp.StompSession;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class WebSocketTest {

    @Autowired
    private WebSocketProperties properties;

    @SpyBean
    private ChatController chatController;

    @Autowired
    DummyGenerator dummyGenerator;

    private ChatClient chatClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String accessToken ;

    @BeforeEach
    void setup() {
        Member member = dummyGenerator.generateSingleMember();
        accessToken = dummyGenerator.generateAccessToken(member);
        chatClient = new ChatClient(properties);
    }

    @Test
    @DisplayName("Websocket - 연결")
    void WebSocketConnection() throws Exception {
        // When
        StompSession session = chatClient.connect(accessToken);

        // Then
        assert session.isConnected();
    }

    @Test
    @DisplayName("Websocket - 메시지 전송 요청시 sendMessage() 호출")
    void WebSocketSendMessage() throws Exception {
        //Given
        StompSession session = chatClient.connect(accessToken);
        MessageSendRequest messageSendRequest = makeMessageSendRequest();
        String jsonMessage = objectMapper.writeValueAsString(messageSendRequest);

        //When
        session.send(properties.getAppDestinationPrefix()+"/send", jsonMessage.getBytes());

        //Then
        verify(chatController, timeout(100).times(1)).sendMessage(messageSendRequest);
    }

    @Test
    @DisplayName("Websocket - 읽음 처리 요청 시 readMessage() 호출")
    void WebSocketReadMessage() throws Exception {
        //Given
        Long chatRoomId = 1L;
        Long memberId = 1L;
        ReadRequest readRequest = new ReadRequest(chatRoomId, memberId);
        String jsonMessage = objectMapper.writeValueAsString(readRequest);
        StompSession session = chatClient.connect(accessToken);

        //When
        session.send(properties.getAppDestinationPrefix()+"/read", jsonMessage.getBytes());

        //Then
        verify(chatController, timeout(100).times(1)).readMessage(readRequest);
    }

    private MessageSendRequest makeMessageSendRequest() {
        MessageType type = MessageType.NORMAL;
        String content = "Hello";
        Long roomId = 1L;
        Long senderId = 1L;
        Long receiverId = 2L;
        MessageSendRequest messageSendRequest = new MessageSendRequest(type, content, roomId, senderId, receiverId);
        return messageSendRequest;
    }
}