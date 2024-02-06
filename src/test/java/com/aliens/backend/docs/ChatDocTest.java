package com.aliens.backend.docs;

import com.aliens.backend.auth.service.TokenProvider;
import com.aliens.backend.chat.controller.dto.response.ChatSummaryResponse;
import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.chat.service.ChatService;
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

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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
}