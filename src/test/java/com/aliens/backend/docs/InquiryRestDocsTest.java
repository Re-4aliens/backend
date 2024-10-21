package com.aliens.backend.docs;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.InquirySuccess;
import com.aliens.backend.inquire.controller.request.InquiryCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class InquiryRestDocsTest extends BaseRestDocsTest {
    @BeforeEach
    void setUp() {
        Member member = dummyGenerator.generateSingleMember();
        GIVEN_ACCESS_TOKEN = dummyGenerator.generateAccessToken(member);
    }

    @Test
    @DisplayName("API - 문의 생성")
    void createInquiry() throws Exception {
        InquiryCreateRequest request = new InquiryCreateRequest("문의 내용");
        final SuccessResponse<?> response = SuccessResponse.of(InquirySuccess.CREATE_INQUIRY_SUCCESS);
        doReturn(response).when(inquiryController).createInquiry(any(), any());

        mockMvc.perform(post("/inquiries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                )
                .andExpect(status().isOk())
                .andDo(document("inquiry-create",
                        requestFields(
                                fieldWithPath("content").description("문의 내용")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("result").description("문의 생성 결과 메시지")
                        )
                ));
    }
}