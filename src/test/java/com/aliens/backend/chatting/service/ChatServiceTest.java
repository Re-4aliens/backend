package com.aliens.backend.chatting.service;

import com.aliens.backend.chat.controller.dto.request.ReadRequest;
import com.aliens.backend.chat.controller.dto.response.ChatSummaryResponse;
import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.chat.domain.ChatRepository.MessageRepository;
import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.chat.service.ChatService;
import com.aliens.backend.global.success.ChatSuccessCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

@SpringBootTest
public class ChatServiceTest {
    @Autowired
    ChatService chatService;
    @MockBean
    MessageRepository messageRepository;

    @Test
    @DisplayName("메시지 전송")
    void sendMessage() {
        //given
        String type = "NORMAL";
        String content = "Hello";
        Long roomId = 1L;
        Long senderId = 1L;
        Long receiverId = 2L;
        MessageSendRequest messageSendRequest = new MessageSendRequest(type, content, roomId, senderId, receiverId);
        //when
        String result = chatService.sendMessage(messageSendRequest);
        //then
        Assertions.assertEquals(ChatSuccessCode.SEND_MESSAGE_SUCCESS.getMessage(), result);
    }

    @Test
    @DisplayName("메시지 읽음 처리")
    void readMessages() {
        //given
        Long chatRoomId = 1L;
        Long memberId = 1L;
        ReadRequest readRequest = new ReadRequest(chatRoomId, memberId);
        //when
        String result = chatService.readMessages(readRequest);
        //then
        Assertions.assertEquals(ChatSuccessCode.READ_MESSAGES_SUCCESS.getMessage(), result);
    }

    @Test
    @DisplayName("채팅 요약 정보 조회")
    void getChatSummary() {
        //given
        Long memberId = 1L;
        //when
        ChatSummaryResponse result = chatService.getChatSummaries(memberId);
        //then
        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("메시지 조회")
    void getMessages() {
        //given
        Long chatRoomId = 1L;
        //when
        List<Message> result = chatService.getMessages(chatRoomId, null);
        //then
        Assertions.assertNotNull(result);
    }
}
