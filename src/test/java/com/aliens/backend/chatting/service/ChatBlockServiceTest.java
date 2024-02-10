package com.aliens.backend.chatting.service;

import com.aliens.backend.chat.controller.dto.request.ChatBlockRequest;
import com.aliens.backend.chat.domain.repository.ChatBlockRepository;
import com.aliens.backend.chat.domain.repository.ChatRoomRepository;
import com.aliens.backend.chat.service.ChatBlockService;
import com.aliens.backend.global.success.ChatSuccessCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ChatBlockServiceTest {
    @Autowired
    ChatBlockService chatBlockService;
    @MockBean
    ChatRoomRepository chatRoomRepository;
    @MockBean
    ChatBlockRepository chatBlockRepository;

    @Test
    @DisplayName("채팅 상대 차단")
    void blockPartner() {
        //given
        Long memberId = 1L;
        Long partnerId = 2L;
        Long roomId = 1L;
        ChatBlockRequest chatBlockRequest = new ChatBlockRequest(partnerId, roomId);
        //when
        String result = chatBlockService.blockPartner(memberId, chatBlockRequest);
        //then
        Assertions.assertEquals(ChatSuccessCode.BLOCK_SUCCESS.getMessage(), result);
    }
}