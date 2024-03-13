package com.aliens.backend.auth.service;

import com.aliens.backend.auth.controller.dto.AuthToken;
import com.aliens.backend.auth.controller.dto.LoginRequest;
import com.aliens.backend.global.BaseIntegrationTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.JWTProperties;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

class AuthServiceTest extends BaseIntegrationTest {

    @Autowired AuthService authService;
    @Autowired JWTProperties jwtProperties;
    @Autowired DummyGenerator dummyGenerator;

    LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        dummyGenerator.generateSingleMember();
        loginRequest = new LoginRequest(
                DummyGenerator.GIVEN_EMAIL,
                DummyGenerator.GIVEN_PASSWORD
        );
    }

    @Test
    @DisplayName("로그인 성공")
    void loginTest() {
        //When
        AuthToken response = authService.login(loginRequest);

        //Then
        Assertions.assertNotNull(response);
    }

    @DisplayName("로그인 실패 - 없는 회원, 비밀번호 불일치")
    @ParameterizedTest
    @CsvSource(value = {"noMember@example.com : password",
                        "tmp@example.com : incorrectPassword"},
                        delimiter = ':')
    void loginFailTest(String givenEmail, String givenPassword) {
        // Given
        LoginRequest inValidLoginRequest = new LoginRequest(givenEmail, givenPassword);

        // When & Then
        Assertions.assertThrows(RestApiException.class, () -> authService.login(inValidLoginRequest));
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logoutTest() {
        //Given
        AuthToken authToken = authService.login(loginRequest);

        //When
        String response = authService.logout(authToken);

        //Then
        Assertions.assertEquals(AuthService.LOGOUT_SUCCESS, response);
    }

    @Test
    @DisplayName("토큰 재발급 성공")
    void reissueTest() {
        //Given
        jwtProperties.setAccessTokenValidTime(1L); //AccessToken 유효기한 짧게변경
        AuthToken requestAuthToken = authService.login(loginRequest);
        jwtProperties.setAccessTokenValidTime(86400000L); //AccessToken 유효기한 원상복구

        //When
        AuthToken responseAuthToken = authService.reissue(requestAuthToken);

        //Then
        Assertions.assertNotEquals(requestAuthToken, responseAuthToken);
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 유효기간이 남은 AccessToken")
    void reissueFailTestByAccessToken() {
        //Given
        AuthToken requestAuthToken = authService.login(loginRequest);

        //When & Then
        Assertions.assertThrows(RestApiException.class, () -> authService.reissue(requestAuthToken));
    }

    @Test
    @DisplayName("토큰 재발급 실패 - 만료된 RefreshToken")
    void reissueFailTestByRefreshToken() {
        //Given
        jwtProperties.setRefreshTokenValidTime(1L); //RefreshToken 유효기한 짧게변경
        AuthToken requestAuthToken = authService.login(loginRequest);
        jwtProperties.setRefreshTokenValidTime(2592000000L); //RefreshToken 유효기한 원상복구

        //When & Then
        Assertions.assertThrows(RestApiException.class, () -> authService.reissue(requestAuthToken));
    }
}
