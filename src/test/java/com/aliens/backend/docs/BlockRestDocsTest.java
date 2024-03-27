package com.aliens.backend.docs;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.block.controller.dto.BlockRequest;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.ChatSuccess;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class BlockRestDocsTest extends BaseRestDocsTest {

    @BeforeEach
    void setUp() {
        Member member = dummyGenerator.generateSingleMember();
        GIVEN_ACCESS_TOKEN = dummyGenerator.generateAccessToken(member);
    }

    @Test
    @DisplayName("API - 채팅 상대 차단")
    void blockPartner() throws Exception {
        Long chatRoomId = 1L;
        Long memberId = 1L;
        BlockRequest request = new BlockRequest(memberId, chatRoomId);
        String message = ChatSuccess.BLOCK_SUCCESS.getMessage();
        SuccessResponse<String> response = SuccessResponse.of(ChatSuccess.REPORT_SUCCESS, message);

        doReturn(response).when(blockController).blockPartner(any(), any());

        mockMvc.perform(post("/chat/block")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andDo(document("chat-block",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("result").description("채팅 상대 차단 결과")
                        )
                ));
    }
}
