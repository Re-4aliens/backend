package com.aliens.backend.docs;

import com.aliens.backend.chat.controller.dto.response.ChatSummaryResponse;
import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.ChatSuccess;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ChatRestDocsTest extends BaseRestDocsTest {

    @Test
    @DisplayName("API - 채팅 메시지 조회")
    void getMessages() throws Exception {
        Long chatRoomId = 1L;
        List<Message> result = new ArrayList<>();
        SuccessResponse<List<Message>> response = SuccessResponse.of(ChatSuccess.GET_MESSAGES_SUCCESS, result);
        doReturn(response).when(chatController).getMessages(any(),any());

        mockMvc.perform(get("/chat/room/{roomId}/messages", chatRoomId)
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                )
                .andExpect(status().isOk())
                .andDo(document("chat-get-messages",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("result").description("메시지 목록 조회 결과")
                        )
                ));
    }

    @Test
    @DisplayName("API - 채팅 요약 정보 조회")
    void getChatSummary() throws Exception {
        ChatSummaryResponse result = new ChatSummaryResponse(new ArrayList<>(), new ArrayList<>());
        SuccessResponse<ChatSummaryResponse> response = SuccessResponse.of(ChatSuccess.GET_SUMMARIES_SUCCESS, result);
        doReturn(response).when(chatController).getChatSummaries(any());


        mockMvc.perform(get("/chat/summaries")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                )
                .andExpect(status().isOk())
                .andDo(document("chat-get-summary",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("result").description("조회 결과"),
                                fieldWithPath("result.chatRooms").description("채팅방 정보"),
                                fieldWithPath("result.chatMessageSummaries").description("채팅방 요약 정보 조회 결과")
                        )
                ));
    }
}