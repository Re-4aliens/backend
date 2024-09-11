package com.aliens.backend.chatting.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.chat.controller.dto.request.ReadRequest;
import com.aliens.backend.chat.controller.dto.response.ChatSummaryResponse;
import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.chat.domain.MessageType;
import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.chat.service.ChatService;
import com.aliens.backend.chat.domain.model.ChatMessageSummary;
import com.aliens.backend.global.BaseIntegrationTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.global.response.success.ChatSuccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class ChatServiceTest extends BaseIntegrationTest {

    @Autowired ChatService chatService;
    @Autowired DummyGenerator dummyGenerator;

    Member receiver;

    @BeforeEach
    void setUp() {
        receiver = dummyGenerator.generateSingleMember();;

        Message message = Message.from(makeMessageSendRequest());
        Date now = new Date();
        doReturn(message).when(messageRepository).save(any());
        doNothing().when(messageRepository).markMessagesAsRead(any(),any());

        ChatMessageSummary summary = new ChatMessageSummary(1L,"this is the last message", now,1L);
        List<ChatMessageSummary> givenSummary = List.of(summary,summary) ;
        doReturn(givenSummary).when(messageRepository).aggregateMessageSummaries(any(),any());

        List<Message> givenMessages = List.of(message,message);
        doReturn(givenMessages).when(messageRepository).findMessages(any(),any());
    }
    @Test
    @DisplayName("메시지 전송")
    void sendMessage() {
        //Given
        MessageSendRequest request = makeMessageSendRequest();

        //When
        String result = chatService.sendMessage(request);

        //Then
        Assertions.assertEquals(ChatSuccess.SEND_MESSAGE_SUCCESS.getMessage(), result);
    }

    @Test
    @DisplayName("메시지 읽음 처리")
    void readMessages() {
        //Given
        Long chatRoomId = 1L;
        Long memberId = 1L;
        ReadRequest readRequest = new ReadRequest(chatRoomId, memberId);
        String expectedResponse = ChatSuccess.READ_MESSAGES_SUCCESS.getMessage();

        //When
        String result = chatService.readMessages(readRequest);

        //Then
        Assertions.assertEquals(expectedResponse, result);
    }

    @Test
    @DisplayName("채팅 요약 정보 조회")
    void getChatSummary() {
        //Given
        Member member = dummyGenerator.generateSingleMember();
        LoginMember loginMember = member.getLoginMember();

        //When
        ChatSummaryResponse result = chatService.getChatSummaries(loginMember);

        //Then
        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("메시지 조회")
    void getMessages() {
        //Given
        Long chatRoomId = 1L;

        //When
        List<Message> result = chatService.getMessages(chatRoomId, null);

        //Then
        Assertions.assertNotNull(result);
    }

    private MessageSendRequest makeMessageSendRequest() {
        MessageType type = MessageType.NORMAL;
        String content = "Hello";
        Long roomId = 1L;
        Long senderId = 1L;
        Long receiverId = receiver.getId();
        return new MessageSendRequest(type, content, roomId, senderId, receiverId);
    }
}