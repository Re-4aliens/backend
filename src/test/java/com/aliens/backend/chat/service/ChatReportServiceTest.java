package com.aliens.backend.chat.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.chat.controller.dto.request.ChatReportRequest;
import com.aliens.backend.chat.service.ChatReportService;
import com.aliens.backend.global.BaseIntegrationTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.ChatError;
import com.aliens.backend.global.response.success.ChatSuccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class ChatReportServiceTest extends BaseIntegrationTest {

    @Autowired ChatReportService chatReportService;
    @Autowired DummyGenerator dummyGenerator;

    LoginMember loginMember;
    Member givenPartner;
    Long givenChatRoomId = 1L;
    String givenContent = "신고 사유";

    @BeforeEach
    void setUp() {
        List<Member> members = dummyGenerator.generateMultiMember(2);
        Member givenMember = members.get(0);
        givenPartner = members.get(1);
        loginMember = givenMember.getLoginMember();
    }

    @Test
    @DisplayName("채팅 상대 신고")
    void reportPartner() {
        //Given
        String givenCategory = "ETC";
        ChatReportRequest request = createChatReportRequestWith(givenCategory);

        //When
        String result = chatReportService.reportPartner(loginMember, request);

        //Then
        Assertions.assertEquals(ChatSuccess.REPORT_SUCCESS.getMessage(), result);
    }

    @Test
    @DisplayName("채팅 상대 신고 - 존재하지 않는 신고 카테고리 예외 발생")
    void reportPartnerWithNonExistCategory() {
        //Given
        String givenCategory = "NON_EXIST_CATEGORY";
        ChatReportRequest request = createChatReportRequestWith(givenCategory);

        //When
        RestApiException exception = Assertions.assertThrows(RestApiException.class,
                () -> chatReportService.reportPartner(loginMember, request));

        //Then
        Assertions.assertEquals(ChatError.INVALID_REPORT_CATEGORY, exception.getErrorCode());
    }

    private ChatReportRequest createChatReportRequestWith(String category) {
        return new ChatReportRequest(givenPartner.getId(),
                givenChatRoomId,
                category,
                givenContent
        );
    }
}