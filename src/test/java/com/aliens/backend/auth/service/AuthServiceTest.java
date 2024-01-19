package com.aliens.backend.auth.service;

import com.aliens.backend.auth.controller.dto.AuthToken;
import com.aliens.backend.auth.controller.dto.LoginRequest;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.auth.domain.MemberRole;
import com.aliens.backend.global.property.JWTProperties;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    AuthService authService;
    @Autowired
    JWTProperties jwtProperties;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    Member member;

    @BeforeEach
    void setUp() {
        member = new Member("tmp@example.com",
                passwordEncoder.encrypt("tmpPassword"),
                MemberRole.MEMBER);
        memberRepository.save(member);
    }

    @AfterEach
    void afterDown() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void loginTest() {
        //Given
        LoginRequest loginRequest = new LoginRequest("tmp@example.com", "tmpPassword");

        //When
        AuthToken response = authService.login(loginRequest);

        //Then
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("로그아웃 성공")
    void logoutTest() {
        //Given
        LoginRequest loginRequest = new LoginRequest("tmp@example.com", "tmpPassword");
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
        LoginRequest loginRequest = new LoginRequest("tmp@example.com", "tmpPassword");
        jwtProperties.setAccessTokenValidTime(1L); //AccessToken 유효기한 짧게변경
        AuthToken requestAuthToken = authService.login(loginRequest);
        jwtProperties.setAccessTokenValidTime(86400000L); //AccessToken 유효기한 원상복구

        //When
        AuthToken responseAuthToken = authService.reissue(requestAuthToken);

        //Then
        Assertions.assertNotEquals(requestAuthToken, responseAuthToken);
    }
}
