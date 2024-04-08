package com.aliens.backend.chatting.socket;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.chat.controller.dto.request.ReadRequest;
import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.domain.MessageType;
import com.aliens.backend.global.BaseIntegrationTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.global.property.WebSocketProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class WebSocketTest extends BaseIntegrationTest {

    @Autowired
    private WebSocketProperties properties;

    @Autowired
    DummyGenerator dummyGenerator;

    private ChatClient chatClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private String accessToken ;
    private Member member;

    @BeforeEach
    void setup() {
        member = dummyGenerator.generateSingleMember();
        accessToken = dummyGenerator.generateAccessToken(member);
        chatClient = new ChatClient(properties);
    }

    @Test
    @DisplayName("Websocket - 연결")
    void WebSocketConnection() throws Exception {
        // When
        StompSession session = chatClient.connect(accessToken);

        // Then
        Assertions.assertEquals(true, session.isConnected());
    }

    @Test
    @DisplayName("Websocket - 메시지 전송 요청시 sendMessage() 호출")
    void WebSocketSendMessage() throws Exception {
        //Given
        ChatRoom chatroom = mock(ChatRoom.class);
        when(chatroom.getId()).thenReturn(1L);
        List<ChatRoom> chatRooms = new ArrayList<>();
        chatRooms.add(chatroom);
        when(chatService.getChatRooms(member.getId())).thenReturn(chatRooms);


        StompSession session = chatClient.connect(accessToken);
        MessageSendRequest messageSendRequest = createMessageSendRequest();
        String jsonMessage = objectMapper.writeValueAsString(messageSendRequest);

        //When
        session.send(properties.getAppDestinationPrefix()+"/send", jsonMessage.getBytes());

        //Then
        verify(chatChannelInterceptor, timeout(100).times(2)).preSend(any(), any()); // 1. SUBSCRIBE, 2. SEND
        verify(chatService, timeout(100).times(1)).getChatRooms(member.getId());
        verify(chatAuthValidator, timeout(100).times(1)).validateRoom(any(), any());
        verify(chatController, timeout(100).times(1)).sendMessage(messageSendRequest);
    }

    @Test
    @DisplayName("Websocket - 메시지 전송 요청시 허가 되지 않은 채팅방 요청")
    void WebSocketSendMessageWithUnauthorizedRoom() throws Exception {
        //Given - 허가되지 않은 채팅방 요청
        ChatRoom chatroom = mock(ChatRoom.class);
        when(chatroom.getId()).thenReturn(2L);
        List<ChatRoom> chatRooms = new ArrayList<>();
        chatRooms.add(chatroom);
        when(chatService.getChatRooms(member.getId())).thenReturn(chatRooms);

        StompSession session = chatClient.connect(accessToken);
        MessageSendRequest messageSendRequest = createMessageSendRequest();
        String jsonMessage = objectMapper.writeValueAsString(messageSendRequest);

        //When
        session.send(properties.getAppDestinationPrefix()+"/send", jsonMessage.getBytes());

        //Then
        verify(chatChannelInterceptor, timeout(100).times(3)).preSend(any(), any()); // 1. SUBSCRIBE, 2. SEND, 3. DISCONNECT
        verify(chatService, timeout(100).times(1)).getChatRooms(member.getId());
        verify(chatAuthValidator, timeout(100).times(1)).validateRoom(any(), any());
        verify(chatController, timeout(100).times(0)).sendMessage(messageSendRequest); // 호출되지 않음
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

    private MessageSendRequest createMessageSendRequest() {
        MessageType type = MessageType.NORMAL;
        String content = "Hello";
        Long roomId = 1L;
        Long senderId = 1L;
        Long receiverId = 2L;
        MessageSendRequest messageSendRequest = new MessageSendRequest(type, content, roomId, senderId, receiverId);
        return messageSendRequest;
    }
}