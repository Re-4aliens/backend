package com.aliens.backend.docs;

import com.aliens.backend.auth.controller.dto.AuthToken;
import com.aliens.backend.auth.controller.dto.LoginRequest;
import com.aliens.backend.auth.service.AuthService;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.AuthSuccess;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthRestDocsTest extends BaseRestDocsTest {

    @Test
    @DisplayName("API - 로그인")
    void login() throws Exception {
        final LoginRequest request = new LoginRequest("email","password");
        final AuthToken authToken = new AuthToken("accessToken", "refreshToken");
        SuccessResponse<AuthToken> response = SuccessResponse.of(AuthSuccess.GENERATE_TOKEN_SUCCESS,authToken);
        doReturn(response).when(authController).login(any());

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
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("result.accessToken").description("발급된 엑세스토큰"),
                                fieldWithPath("result.refreshToken").description("발급된 리프레쉬토큰")
                        )
                ));
    }

    @Test
    @DisplayName("API -로그아웃")
    void logout() throws Exception {
        final AuthToken request = new AuthToken("accessToken", "refreshToken");
        final String message = AuthService.LOGOUT_SUCCESS;
        SuccessResponse<String> response = SuccessResponse.of(AuthSuccess.LOGOUT_SUCCESS, message);

        doReturn(response).when(authController).logout(any());

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
    @DisplayName("API - 토큰 재발급")
    void reissue() throws Exception {
        final AuthToken request = new AuthToken("expiredAccessToken", "refreshToken");
        final AuthToken result = new AuthToken("newAccessToken", "refreshToken");
        SuccessResponse<AuthToken> response = SuccessResponse.of(AuthSuccess.REISSUE_TOKEN_SUCCESS, result);

        doReturn(response).when(authController).reissue(any());

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
                                fieldWithPath("code").description("응답 코드"),
                                fieldWithPath("result.accessToken").description("새로 발급된 엑세스토큰"),
                                fieldWithPath("result.refreshToken").description("기존의 리프레쉬토큰")
                        )
                ));
    }
}