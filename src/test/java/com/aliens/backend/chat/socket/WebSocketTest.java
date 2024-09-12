package com.aliens.backend.chat.socket;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.chat.domain.MessageType;
import com.aliens.backend.chat.domain.model.MemberPair;
import com.aliens.backend.chat.domain.repository.ChatRoomRepository;
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

import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

class WebSocketTest extends BaseIntegrationTest {

    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired WebSocketProperties properties;
    @Autowired DummyGenerator dummyGenerator;
    ObjectMapper objectMapper = new ObjectMapper();

    Member sender;
    Member receiver;

    String accessToken;
    ChatRoom givenChatRoom;
    ChatClient chatClient;

    @BeforeEach
    void setup() {
        sender = dummyGenerator.generateSingleMember();
        accessToken = dummyGenerator.generateAccessToken(sender);

        receiver = dummyGenerator.generateSingleMember();
        setChatRoom(receiver, sender);

        chatClient = new ChatClient(properties);
        objectMapper = new ObjectMapper();
    }

    private void setChatRoom(Member receiver, Member sender) {
        MemberPair receiverMemberPair = new MemberPair(receiver, sender);
        givenChatRoom = ChatRoom.createOpenChatroom(receiverMemberPair);
        givenChatRoom = chatRoomRepository.save(givenChatRoom);
    }

    @Test
    @DisplayName("Websocket - 연결 테스트")
    void WebSocketConnection() throws Exception {
        // When
        StompSession session = chatClient.connect(accessToken);

        // Then
        Assertions.assertEquals(true, session.isConnected());
    }

    @Test
    @DisplayName("Websocket - 웹소켓 인터셉터 테스트")
    void WebSocketInterceptorTest() throws Exception {
        // Given
        chatClient.connect(accessToken);
        List<Object> receiveMessage = chatClient.subscribe(givenChatRoom.getId());
        MessageSendRequest request = createMessageSendRequest(givenChatRoom.getId());

        //When
        chatClient.send(properties.getAppDestinationPrefix() + "/send", request);
        await().atMost(5, SECONDS).until(() -> !receiveMessage.isEmpty());

        //Then
        verify(chatChannelInterceptor, times(3)).preSend(any(), any());
        verify(chatAuthValidator, times(1)).validateRoomFromTopic(any(), any());
        verify(chatAuthValidator, times(1)).validateRoom(any(), any());
        verify(chatService,times(1)).getChatRooms(any());
    }

    @Test
    @DisplayName("Websocket - 메시지 전송 및 수신 테스트")
    void WebSocketMessageSendingAndReceiving() throws Exception {
        // Given
        chatClient.connect(accessToken);
        List<Object> receiveMessage = chatClient.subscribe(givenChatRoom.getId());
        MessageSendRequest request = createMessageSendRequest(givenChatRoom.getId());

        //When
        chatClient.send(properties.getAppDestinationPrefix() + "/send", request);
        await().atMost(5, SECONDS).until(() -> !receiveMessage.isEmpty());

        //Then
        verify(chatService, times(1)).sendMessage(request);

        Message receivedMessage = objectMapper.readValue((byte[]) receiveMessage.get(0), Message.class);
        Assertions.assertEquals(1, receiveMessage.size());
        Assertions.assertEquals(request.type(), receivedMessage.getType());
        Assertions.assertEquals(request.content(), receivedMessage.getContent());
        Assertions.assertEquals(request.roomId(), receivedMessage.getRoomId());
        Assertions.assertEquals(request.senderId(), receivedMessage.getSenderId());
        Assertions.assertEquals(request.receiverId(), receivedMessage.getReceiverId());
    }

    @Test
    @DisplayName("Websocket - 허가 되지 않은 채팅방에 메시지 전송 시도")
    void sendMessageToUnauthorizedRoom() throws Exception {
        //Given
        chatClient.connect(accessToken);
        chatClient.subscribe(givenChatRoom.getId());

        Long unAuthorizedRoomId = givenChatRoom.getId() + 1;
        MessageSendRequest messageSendRequest = createMessageSendRequest(unAuthorizedRoomId);

        //When
        chatClient.send(properties.getAppDestinationPrefix() + "/send", messageSendRequest);

        //Then
        verify(chatController, times(0)).sendMessage(messageSendRequest);
    }

    @Test
    @DisplayName("Websocket -  허가 되지 않은 채팅방에 구독 시도")
    void subscribeToUnauthorizedRoom() throws Exception {
        //Given
        Long unAuthorizedRoomId = givenChatRoom.getId() + 1;

        //When
        chatClient.connect(accessToken);
        chatClient.subscribe(unAuthorizedRoomId);

        //Then
        verify(chatChannelInterceptor, times(2)).preSend(any(), any()); // 1. CONNECT, 2. SUBSCRIBE
        verify(chatService, times(1)).getChatRooms(sender.getId());
    }

    private MessageSendRequest createMessageSendRequest(Long roomId) {
        MessageType type = MessageType.NORMAL;
        String content = "Hello";
        return new MessageSendRequest(type, content, roomId, sender.getId(), receiver.getId());
    }
}