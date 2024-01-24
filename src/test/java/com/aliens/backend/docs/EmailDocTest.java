package com.aliens.backend.docs;

import com.aliens.backend.email.controller.response.EmailResponse;
import com.aliens.backend.email.service.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class EmailDocTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("이메일 중복 API 테스트 - 사용불가")
    void duplicateCheckSuccess() throws Exception {
        final String request = "tmp@example.com";
        final String response = EmailResponse.DUPLICATED_EMAIL.getMessage();

        when(emailService.duplicateCheck(any())).thenReturn(response);

        this.mockMvc.perform(get("/members/exist").param("email", request))
                .andExpect(status().isOk())
                .andDo(document("email-duplicateCheck-fail",
                        queryParameters(
                                parameterWithName("email").description("검증 요청 이메일")
                        ),
                        responseFields(
                                fieldWithPath("response").description("검증 결과")
                        )
                ));
    }

    @Test
    @DisplayName("이메일 중복 API 테스트 - 사용가능")
    void duplicateCheckFail() throws Exception {
        final String request = "tmp@example.com";
        final String response = EmailResponse.AVAILABLE_EMAIL.getMessage();

        when(emailService.duplicateCheck(any())).thenReturn(response);

        this.mockMvc.perform(get("/members/exist").param("email", request))
                .andExpect(status().isOk())
                .andDo(document("email-duplicateCheck-success",
                        queryParameters(
                                parameterWithName("email").description("검증 요청 이메일")
                        ),
                        responseFields(
                                fieldWithPath("response").description("검증 결과")
                        )
                ));
    }

    @Test
    @DisplayName("인증 이메일 전송 API 테스트")
    void sendAuthenticationEmail() throws Exception {
        final String email = "tmp@example.com";
        final String response = EmailResponse.EMAIL_SEND_SUCCESS.getMessage();

        when(emailService.sendAuthenticationEmail(any())).thenReturn(response);

        this.mockMvc.perform(post("/emails/verification/send")
                        .content(objectMapper.writeValueAsString(Collections.singletonMap("email", email)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("email-verification-send",
                        requestFields(
                                fieldWithPath("email").description("검증 요청 이메일")
                        ),
                        responseFields(
                                fieldWithPath("response").description("전송 결과")
                        )
                ));
    }

    @Test
    @DisplayName("이메일 검증 요청 API 테스트")
    void authenticateEmail() throws Exception {
        final String request = "testToken";
        final String response = EmailResponse.EMAIL_AUTHENTICATION_SUCCESS.getMessage();

        when(emailService.authenticateEmail(any())).thenReturn(response);

        this.mockMvc.perform(get("/emails/confirm").param("token", request))
                .andExpect(status().isOk())
                .andDo(document("email-authentication-success",
                        queryParameters(
                                parameterWithName("token").description("이메일 검증을 위한 토큰")
                        ),
                        responseFields(
                                fieldWithPath("response").description("검증 결과")
                        )
                ));
    }
}
