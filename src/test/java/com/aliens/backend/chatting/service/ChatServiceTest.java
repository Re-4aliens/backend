package com.aliens.backend.chatting.service;

import com.aliens.backend.chat.controller.dto.ChatSummary;
import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.chat.service.ChatService;
import com.aliens.backend.global.success.ChatSuccessCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ChatServiceTest {
    @Autowired
    ChatService chatService;

    @Test
    @DisplayName("메시지 전송")
    void sendMessage() {
        //given
        Message message = new Message.MessageBuilder()
                .content("test message")
                .roomId(1L)
                .senderId(1L)
                .receiverId(2L)
                .build();
        //when
        String result = chatService.sendMessage(message);
        //then
        Assertions.assertEquals(ChatSuccessCode.SEND_MESSAGE_SUCCESS.getMessage(), result);
    }

    @Test
    @DisplayName("메시지 읽음 처리")
    void readMessages() {
        //given
        //when
        String result = chatService.readMessages();
        //then
        Assertions.assertEquals(ChatSuccessCode.READ_MESSAGES_SUCCESS.getMessage(), result);
    }

    @Test
    @DisplayName("채팅 요약 정보 조회")
    void getChatSummary() {
        //given
        //when
        List<ChatSummary> result = chatService.getChatSummary();
        //then
        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("메시지 조회")
    void getMessages() {
        //given
        //when
        List<Message> result = chatService.getMessages();
        //then
        Assertions.assertNotNull(result);
    }
}
