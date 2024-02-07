package com.aliens.backend.chatting.service;

import com.aliens.backend.chat.controller.dto.request.ChatReportRequest;
import com.aliens.backend.chat.domain.repository.ChatReportRepository;
import com.aliens.backend.chat.service.ChatReportService;
import com.aliens.backend.global.success.ChatSuccessCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class ChatReportServiceTest {
    @Autowired
    ChatReportService chatReportService;
    @MockBean
    ChatReportRepository chatReportRepository;

    @Test
    @DisplayName("채팅 상대 신고")
    void blockPartner() {
        //given
        Long memberId = 1L;
        Long partnerId = 2L;
        Long roomId = 1L;
        String category = "ETC";
        String content = "신고 사유";
        ChatReportRequest chatReportRequest = new ChatReportRequest(partnerId, roomId, category, content);
        //when
        String result = chatReportService.reportPartner(memberId, chatReportRequest);
        //then
        Assertions.assertEquals(ChatSuccessCode.REPORT_SUCCESS.getMessage(), result);
    }
}