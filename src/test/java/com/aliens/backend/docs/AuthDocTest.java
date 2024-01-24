package com.aliens.backend.docs;

import com.aliens.backend.auth.controller.dto.AuthToken;
import com.aliens.backend.auth.controller.dto.LoginRequest;
import com.aliens.backend.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class AuthDocTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("로그인 API 테스트")
    void login() throws Exception {
        final LoginRequest request = new LoginRequest("email","password");
        final AuthToken response = new AuthToken("accessToken", "refreshToken");
        when(authService.login(any())).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(post("/authentication")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("auth-login",
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호")
                        ),

                        responseFields(
                                fieldWithPath("accessToken").description("발급된 엑세스토큰"),
                                fieldWithPath("refreshToken").description("발급된 리프레쉬토큰")
                        )
                ));
    }

    @Test
    @DisplayName("로그아웃 API 테스트")
    void logout() throws Exception {
        final AuthToken request = new AuthToken("accessToken", "refreshToken");
        final String response = AuthService.LOGOUT_SUCCESS;
        when(authService.logout(any())).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(post("/authentication/logout")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("auth-logout",
                        requestFields(
                                fieldWithPath("accessToken").description("엑세스토큰"),
                                fieldWithPath("refreshToken").description("리프레쉬토큰")
                        )
                ));
    }

    @Test
    @DisplayName("토큰 재발급 API 테스트")
    void reissue() throws Exception {
        final AuthToken request = new AuthToken("expiredAccessToken", "refreshToken");
        final AuthToken response = new AuthToken("newAccessToken", "refreshToken");
        when(authService.reissue(any())).thenReturn(response);

        ObjectMapper objectMapper = new ObjectMapper();
        this.mockMvc.perform(post("/authentication/reissue")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("auth-reissue",
                        requestFields(
                                fieldWithPath("accessToken").description("만료된 엑세스토큰"),
                                fieldWithPath("refreshToken").description("재발급을 위한 리프레쉬토큰")
                        ),

                        responseFields(
                                fieldWithPath("accessToken").description("새로 발급된 엑세스토큰"),
                                fieldWithPath("refreshToken").description("기존의 리프레쉬토큰")
                        )
                ));
    }
}
