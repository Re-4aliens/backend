package com.aliens.backend.docs;

import com.aliens.backend.email.controller.response.EmailResponse;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.EmailSuccess;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EmailRestDocsTest extends BaseRestDocsTest {

    @Test
    @DisplayName("API - 이메일 중복(사용불가)")
    void duplicateCheckSuccess() throws Exception {
        final String request = "tmp@example.com";
        final String message = EmailResponse.DUPLICATED_EMAIL.getMessage();

        SuccessResponse<String> response = SuccessResponse.of(EmailSuccess.DUPLICATE_CHECK_SUCCESS, message);
        doReturn(response).when(emailController).duplicateCheck(any());

        this.mockMvc.perform(get("/members/exist").param("email", request))
                .andExpect(status().isOk())
                .andDo(document("email-duplicateCheck-duplicate",
                        queryParameters(
                                parameterWithName("email").description("검증 요청 이메일")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("result").description("응답 결과")
                        )
                ));
    }

    @Test
    @DisplayName("API - 이메일 중복 API 테스트(사용가능)")
    void duplicateCheckFail() throws Exception {
        final String request = "tmp@example.com";
        final String message = EmailResponse.AVAILABLE_EMAIL.getMessage();

        SuccessResponse<String> response = SuccessResponse.of(EmailSuccess.DUPLICATE_CHECK_SUCCESS, message);
        doReturn(response).when(emailController).duplicateCheck(any());

        this.mockMvc.perform(get("/members/exist").param("email", request))
                .andExpect(status().isOk())
                .andDo(document("email-duplicateCheck-available",
                        queryParameters(
                                parameterWithName("email").description("검증 요청 이메일")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("result").description("응답 결과")
                        )
                ));
    }

    @Test
    @DisplayName("API - 인증 이메일 전송")
    void sendAuthenticationEmail() throws Exception {
        final String email = "tmp@example.com";
        final String message = EmailResponse.EMAIL_SEND_SUCCESS.getMessage();

        SuccessResponse<String> response = SuccessResponse.of(EmailSuccess.SEND_EMAIL_SUCCESS, message);
        doReturn(response).when(emailController).sendAuthenticationEmail(any());

        this.mockMvc.perform(post("/emails/verification/send?email=" + email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("email-verification-send",
                        queryParameters(
                                parameterWithName("email").description("검증을 위한 이메일")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("result").description("응답 결과")
                        )
                ));
    }


    @Test
    @DisplayName("API - 이메일 인증완료 확인 (인증완료)")
    void checkEmailAuthenticated() throws Exception {
        final String request = "tmp@example.com";
        final String message = EmailResponse.CAN_NEXT_STEP.getMessage();

        SuccessResponse<String> response = SuccessResponse.of(EmailSuccess.EMAIL_AUTHENTICATE_SUCCESS, message);
        doReturn(response).when(emailController).checkEmailAuthenticated(any());

        this.mockMvc.perform(get("/emails").param("email", request))
                .andExpect(status().isOk())
                .andDo(document("email-authenticated-check",
                        queryParameters(
                                parameterWithName("email").description("요청 이메일")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("result").description("응답 결과")
                        )
                ));
    }

    @Test
    @DisplayName("API - 이메일 인증완료 확인 (미인증)")
    void checkEmailNotAuthenticated() throws Exception {
        final String request = "tmp@example.com";
        final String message = EmailResponse.CANT_NEXT_STEP.getMessage();

        SuccessResponse<String> response = SuccessResponse.of(EmailSuccess.EMAIL_AUTHENTICATE_SUCCESS, message);
        doReturn(response).when(emailController).checkEmailAuthenticated(any());

        this.mockMvc.perform(get("/emails").param("email", request))
                .andExpect(status().isOk())
                .andDo(document("email-authenticated-check-not",
                        queryParameters(
                                parameterWithName("email").description("요청 이메일")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("result").description("응답 결과")
                        )
                ));
    }
}
