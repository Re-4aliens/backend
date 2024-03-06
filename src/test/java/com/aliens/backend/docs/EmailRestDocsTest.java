package com.aliens.backend.docs;

import com.aliens.backend.email.controller.response.EmailResponse;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.EmailSuccess;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
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

        this.mockMvc.perform(post("/emails/verification/send")
                        .content(objectMapper.writeValueAsString(Collections.singletonMap("email", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("email-verification-send",
                        requestFields(
                                fieldWithPath("email").description("검증 요청 이메일")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("result").description("응답 결과")
                        )
                ));
    }

    @Test
    @DisplayName("API - 이메일 검증 요청")
    void authenticateEmail() throws Exception {
        final String request = "testToken";
        final String message = EmailResponse.EMAIL_AUTHENTICATION_SUCCESS.getMessage();

        SuccessResponse<String> response = SuccessResponse.of(EmailSuccess.EMAIL_AUTHENTICATE_SUCCESS, message);
        doReturn(response).when(emailController).authenticateEmail(any());

        this.mockMvc.perform(get("/emails/confirm").param("token", request))
                .andExpect(status().isOk())
                .andDo(document("email-authentication-success",
                        queryParameters(
                                parameterWithName("token").description("이메일 검증을 위한 토큰")
                        ),
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("result").description("응답 결과")
                        )
                ));
    }
}
