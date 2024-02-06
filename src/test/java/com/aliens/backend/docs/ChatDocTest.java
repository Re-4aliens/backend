package com.aliens.backend.docs;

import com.aliens.backend.auth.service.TokenProvider;
import com.aliens.backend.chat.controller.dto.request.ChatBlockRequest;
import com.aliens.backend.chat.controller.dto.request.ChatReportRequest;
import com.aliens.backend.chat.controller.dto.response.ChatSummaryResponse;
import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.chat.service.ChatBlockService;
import com.aliens.backend.chat.service.ChatReportService;
import com.aliens.backend.chat.service.ChatService;
import com.aliens.backend.global.success.ChatSuccessCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class ChatDocTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ChatService chatService;
    @MockBean
    private ChatReportService chatReportService;
    @MockBean
    private ChatBlockService chatBlockService;
    @MockBean
    private TokenProvider tokenProvider;

    ObjectMapper objectMapper;
    MockMultipartFile multipartFile;
    String accessToken;

    @BeforeEach
    void setUp() {
        accessToken= "accessToken";
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("채팅 메시지 조회")
    void getMessages() throws Exception {
        Long chatRoomId = 1L;
        List<Message> response = new ArrayList<>();
        when(chatService.getMessages(chatRoomId, null)).thenReturn(response);

        mockMvc.perform(get("/chat/room/{roomId}/messages", chatRoomId)
                        .header("Authorization", accessToken)
                )
                .andExpect(status().isOk())
                .andDo(document("chat-get-messages",
                        responseFields(
                                fieldWithPath("response").description("메시지 목록 조회 결과")
                        )
                ));
    }

    @Test
    @DisplayName("채팅 요약 정보 조회")
    void getChatSummary() throws Exception {
        Long memberId = 1L;
        ChatSummaryResponse response = new ChatSummaryResponse(new ArrayList<>(), new ArrayList<>());
        when(chatService.getChatSummaries(memberId)).thenReturn(response);

        mockMvc.perform(get("/chat/summaries")
                        .header("Authorization", accessToken)
                )
                .andExpect(status().isOk())
                .andDo(document("chat-get-summary",
                        responseFields(
                                fieldWithPath("response").description("채팅방 요약 정보 조회 결과")
                        )
                ));
    }

    @Test
    @DisplayName("채팅 상대 신고")
    void reportPartner() throws Exception {
        Long memberId = 1L;
        Long chatRoomId = 1L;
        String reportCategory = "ETC";
        String reportContent = "신고 사유";
        ChatReportRequest request = new ChatReportRequest(memberId, chatRoomId, reportCategory, reportContent);
        String response = ChatSuccessCode.REPORT_SUCCESS.getMessage();
        when(chatReportService.reportPartner(any(), any())).thenReturn(response);
        mockMvc.perform(post("/chat/report")
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andDo(document("chat-report",
                        responseFields(
                                fieldWithPath("response").description("채팅 상대 신고 결과")
                        )
                ));
    }

    @Test
    @DisplayName("채팅 상대 차단")
    void blockPartner() throws Exception {
        Long chatRoomId = 1L;
        Long memberId = 1L;
        ChatBlockRequest request = new ChatBlockRequest(memberId, chatRoomId);
        String response = ChatSuccessCode.BLOCK_SUCCESS.getMessage();
        when(chatBlockService.blockPartner(any(), any())).thenReturn(response);
        mockMvc.perform(post("/chat/block")
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andDo(document("chat-block",
                        responseFields(
                                fieldWithPath("response").description("채팅 상대 차단 결과")
                        )
                ));
    }
}