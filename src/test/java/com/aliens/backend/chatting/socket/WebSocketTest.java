package com.aliens.backend.chatting.socket;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.chat.controller.dto.request.ReadRequest;
import com.aliens.backend.chat.controller.dto.response.ReadResponse;
import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.domain.Message;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class WebSocketTest extends BaseIntegrationTest {

    @Autowired
    private WebSocketProperties properties;
    @Autowired
    private DummyGenerator dummyGenerator;
    private ChatClient chatClient;
    private ObjectMapper objectMapper;
    private String accessToken;
    private Member member;
    private Long authorizedRoomId;

    @BeforeEach
    void setup() {
        member = dummyGenerator.generateSingleMember();
        accessToken = dummyGenerator.generateAccessToken(member);
        chatClient = new ChatClient(properties);
        objectMapper = new ObjectMapper();
        authorizedRoomId = 101L;
        setValidRooms();
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
    @DisplayName("Websocket - 메시지 전송 및 수신 테스트")
    void WebSocketMessageSendingAndReceiving() throws Exception {
        // Given
        MessageSendRequest messageSendRequest = createMessageSendRequest(authorizedRoomId);
        Message expectedMessage = Message.of(messageSendRequest);
        setPrivateField(expectedMessage, "id", "507f1f77bcf86cd799439011");
        doReturn(expectedMessage).when(messageRepository).save(any());

        //When
        chatClient.connect(accessToken);
        List<Object> receiveMessage = chatClient.subscribe(authorizedRoomId);
        chatClient.send(properties.getAppDestinationPrefix() + "/send", messageSendRequest);

        //Then
        verify(chatService, timeout(20000).times(1)).sendMessage(messageSendRequest);
        verify(chatChannelInterceptor, timeout(20000).times(3)).preSend(any(), any()); // 1. CONNECT, 2. SUBSCRIBE, 3. SEND
        verify(chatService, timeout(20000).times(1)).getChatRooms(member.getId()); // CONNECT
        verify(chatAuthValidator, timeout(20000).times(1)).validateRoomFromTopic(any(), any()); // SUBSCRIBE
        verify(chatAuthValidator, timeout(20000).times(1)).validateRoom(any(), any()); // SEND
        Message receivedMessage = objectMapper.readValue((byte[]) receiveMessage.get(0), Message.class);
        Assertions.assertEquals(1, receiveMessage.size());
        Assertions.assertEquals(expectedMessage.getId(), receivedMessage.getId());
        Assertions.assertEquals(expectedMessage.getType(), receivedMessage.getType());
        Assertions.assertEquals(expectedMessage.getContent(), receivedMessage.getContent());
        Assertions.assertEquals(expectedMessage.getRoomId(), receivedMessage.getRoomId());
        Assertions.assertEquals(expectedMessage.getSenderId(), receivedMessage.getSenderId());
        Assertions.assertEquals(expectedMessage.getReceiverId(), receivedMessage.getReceiverId());
    }

    @Test
    @DisplayName("Websocket - 메시지 전송 요청시 허가 되지 않은 채팅방 요청")
    void WebSocketSendMessageWithUnauthorizedRoom() throws Exception {
        //Given
        Long unAuthorizedRoomId = 102L;
        MessageSendRequest messageSendRequest = createMessageSendRequest(unAuthorizedRoomId);

        //When
        chatClient.connect(accessToken);
        chatClient.subscribe(authorizedRoomId);
        chatClient.send(properties.getAppDestinationPrefix() + "/send", messageSendRequest);

        //Then
        verify(chatChannelInterceptor, timeout(20000).times(4)).preSend(any(), any()); // 1. CONNECT, 2. SUBSCRIBE, 3. SEND, 4. DISCONNECT
        verify(chatService, timeout(20000).times(1)).getChatRooms(member.getId());
        verify(chatAuthValidator, timeout(20000).times(1)).validateRoom(any(), any());
        verify(chatController, timeout(20000).times(0)).sendMessage(messageSendRequest); // 호출되지 않음
    }

    @Test
    @DisplayName("Websocket - 채팅방 구독 요청시 허가 되지 않은 채팅방 요청")
    void WebSocketSubscribeWithUnauthorizedRoom() throws Exception {
        //Given
        Long unAuthorizedRoomId = 102L;

        //When
        chatClient.connect(accessToken);
        chatClient.subscribe(unAuthorizedRoomId);

        //Then
        verify(chatChannelInterceptor, timeout(20000).times(3)).preSend(any(), any()); // 1. CONNECT, 2. SUBSCRIBE, 3. DISCONNECT
        verify(chatService, timeout(20000).times(1)).getChatRooms(member.getId());
        verify(chatAuthValidator, timeout(20000).times(1)).validateRoomFromTopic(any(), any());
    }

    @Test
    @DisplayName("Websocket - 읽음 처리 요청")
    void WebSocketReadMessage() throws Exception {
        //Given
        ReadRequest readRequest = new ReadRequest(authorizedRoomId, member.getId());
        ReadResponse readResponse = new ReadResponse(member.getId());
        doNothing().when(messageRepository).markMessagesAsRead(any(), any());

        //When
        chatClient.connect(accessToken);
        List<Object> receiveMessage = chatClient.subscribe(authorizedRoomId);
        chatClient.send(properties.getAppDestinationPrefix() + "/read", readRequest);

        //Then
        verify(chatChannelInterceptor, timeout(20000).times(3)).preSend(any(), any()); // 1. CONNECT, 2. SUBSCRIBE, 3. SEND
        verify(chatController, timeout(20000).times(1)).readMessage(readRequest);
        verify(chatService, timeout(20000).times(1)).getChatRooms(member.getId()); // CONNECT
        verify(chatAuthValidator, timeout(20000).times(1)).validateRoomFromTopic(any(), any()); // SUBSCRIBE
        verify(chatAuthValidator, timeout(20000).times(1)).validateRoom(any(), any()); // SEND
        ReadResponse receivedReadResponse = objectMapper.readValue((byte[]) receiveMessage.get(0), ReadResponse.class);
        Assertions.assertEquals(1, receiveMessage.size());
        Assertions.assertEquals(readResponse.readBy(), receivedReadResponse.readBy());
    }

    @Test
    @DisplayName("Websocket - 읽음 처리 요청 시 허가되지 않은 채팅방 요청")
    void WebSocketReadMessageWithUnauthorizedRoom() throws Exception {
        //Given
        Long unAuthorizedRoomId = 102L;
        ReadRequest unAuthorizedReadRequest = new ReadRequest(unAuthorizedRoomId, member.getId());
        doNothing().when(messageRepository).markMessagesAsRead(any(), any());

        //When
        chatClient.connect(accessToken);
        chatClient.subscribe(authorizedRoomId);
        chatClient.send(properties.getAppDestinationPrefix() + "/read", unAuthorizedReadRequest);

        //Then
        verify(chatChannelInterceptor, timeout(20000).times(4)).preSend(any(), any()); // 1. CONNECT, 2. SUBSCRIBE, 3. SEND, 4. DISCONNECT
        verify(chatService, timeout(20000).times(1)).getChatRooms(member.getId());
        verify(chatAuthValidator, timeout(20000).times(1)).validateRoom(any(), any());
        verify(chatController, timeout(20000).times(0)).readMessage(unAuthorizedReadRequest); // 호출되지 않음
    }

    private MessageSendRequest createMessageSendRequest(Long roomId) {
        MessageType type = MessageType.NORMAL;
        String content = "Hello";
        Long senderId = 1L;
        Long receiverId = 2L;
        MessageSendRequest messageSendRequest = new MessageSendRequest(type, content, roomId, senderId, receiverId);
        return messageSendRequest;
    }

    private void setValidRooms() {
        ChatRoom chatroom = mock(ChatRoom.class);
        when(chatroom.getId()).thenReturn(authorizedRoomId);
        List<ChatRoom> chatRooms = new ArrayList<>();
        chatRooms.add(chatroom);
        when(chatService.getChatRooms(member.getId())).thenReturn(chatRooms);
    }

    public void setPrivateField(Object targetObject, String fieldName, Object value) {
        try {
            Field field = targetObject.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(targetObject, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}