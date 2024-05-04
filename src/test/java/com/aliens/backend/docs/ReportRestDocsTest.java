package com.aliens.backend.docs;

import com.aliens.backend.chat.controller.dto.request.ChatReportRequest;
import com.aliens.backend.chat.domain.ChatReportCategory;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.ChatSuccess;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class ReportRestDocsTest extends BaseRestDocsTest {

    @Test
    @DisplayName("API - 채팅 상대 신고")
    void reportPartner() throws Exception {
        Long memberId = 1L;
        Long chatRoomId = 1L;
        String category = ChatReportCategory.ETC.toString();
        String reportContent = "신고 사유";
        ChatReportRequest request = new ChatReportRequest(memberId, chatRoomId, category, reportContent);

        String message = ChatSuccess.REPORT_SUCCESS.getMessage();
        SuccessResponse<String> response = SuccessResponse.of(ChatSuccess.REPORT_SUCCESS, message);
        doReturn(response).when(chatReportController).reportPartner(any(), any());

        mockMvc.perform(post("/chat/report")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andDo(document("chat-report",
                        requestFields(
                                fieldWithPath("partnerId").description("신고 대상자의 ID"),
                                fieldWithPath("chatRoomId").description("채팅방의 ID, 신고가 발생한 채팅방"),
                                fieldWithPath("category").description("신고 카테고리: SEXUAL_HARASSMENT, VIOLENCE, SPAM, SCAM, ETC"),
                                fieldWithPath("content").description("신고 내용, 신고 대상자의 행동에 대한 설명")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("result").description("채팅 상대 신고 결과 메시지")
                        )
                ));
    }
}
