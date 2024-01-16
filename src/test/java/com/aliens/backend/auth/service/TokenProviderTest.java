package com.aliens.backend.auth.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.MemberRole;
import com.aliens.backend.global.property.JWTProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenProviderTest {

    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private JWTProperties jwtProperties;

    LoginMember givenLoginMember;

    @BeforeEach
    void setUp() {
        Long memberId = 1L;
        MemberRole role = MemberRole.MEMBER;
        givenLoginMember = new LoginMember(memberId,role);
    }

    @Test
    @DisplayName("엑세스토큰 생성")
    void generateAccessTokenTest() {
        //When
        String accessToken = tokenProvider.generateAccessToken(givenLoginMember);

        //Then
        Assertions.assertNotNull(accessToken);
    }

    @Test
    @DisplayName("리프레쉬토큰 생성")
    void generateRefreshTokenTest() {
        //When
        String accessToken = tokenProvider.generateRefreshToken(givenLoginMember);

        //Then
        Assertions.assertNotNull(accessToken);
    }

    @Test
    @DisplayName("토큰으로부터 로그인멤버 정보 추출")
    void getLoginMemberFromAccessTokenTest() {
        //Given
        String accessToken = tokenProvider.generateAccessToken(givenLoginMember);

        //When
        LoginMember result = tokenProvider.getLoginMemberFromToken(accessToken);

        //Then
        Assertions.assertEquals(givenLoginMember,result);
    }

    @Test
    @DisplayName("만료된 토큰 검증")
    void expiredTokenTest() {
        //Given
        jwtProperties.setAccessTokenValidTime(1L); //AccessToken 유효기한 짧게변경
        String accessToken = tokenProvider.generateAccessToken(givenLoginMember);
        jwtProperties.setAccessTokenValidTime(86400000L); //AccessToken 유효기한 원상복구

        //When
        boolean result = tokenProvider.isNotExpiredToken(accessToken);

        //Then
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("기간이 유효한 토큰 검증")
    void notExpiredTokenTest() {
        //Given
        String accessToken = tokenProvider.generateAccessToken(givenLoginMember);

        //When
        boolean result = tokenProvider.isNotExpiredToken(accessToken);

        //Then
        Assertions.assertTrue(result);
    }
}
