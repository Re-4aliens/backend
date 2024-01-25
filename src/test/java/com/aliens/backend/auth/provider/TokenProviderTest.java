package com.aliens.backend.auth.provider;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.MemberRole;
import com.aliens.backend.auth.service.TokenProvider;
import com.aliens.backend.global.exception.RestApiException;
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

    private LoginMember givenLoginMember;
    private Long givenTokenId;

    @BeforeEach
    void setUp() {
        Long memberId = 1L;
        givenTokenId = 1L;
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
        String refreshToken = tokenProvider.generateRefreshToken(givenLoginMember,givenTokenId);

        //Then
        Assertions.assertNotNull(refreshToken);
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
    @DisplayName("토큰으로부터 로그인멤버 정보 추출 실패 - 올바르지 않은 토큰")
    void getLoginMemberFromInvalidTokenFailTest() {
        //Given
        String InValidAccessToken = "올바르지 않은 토큰";

        //When & Then
        Assertions.assertThrows(RestApiException.class, () -> tokenProvider.getLoginMemberFromToken(InValidAccessToken));
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

    @Test
    @DisplayName("리프레시토큰으로부터 토큰 아이디 추출")
    void getTokenIdFromTokenTest() {
        //Given
        String refreshToken = tokenProvider.generateRefreshToken(givenLoginMember,givenTokenId);

        //When
        Long result = tokenProvider.getTokenIdFromToken(refreshToken);

        //Then
        Assertions.assertEquals(result, givenTokenId);
    }

    @Test
    @DisplayName("리프레시토큰으로부터 토큰 아이디 추출 실패 - 올바르지 않은 토큰")
    void getTokenIdFromInvalidTokenFailTest() {
        //Given
        String InvalidRefreshToken = "올바르지 않은 토큰";

        //When & Then
        Assertions.assertThrows(RestApiException.class, () -> tokenProvider.getTokenIdFromToken(InvalidRefreshToken));
    }

    @Test
    @DisplayName("리프레시토큰으로부터 토큰 아이디 추출 실패 - 만료된 토큰")
    void getTokenIdFromExpiredTokenFailTest() {
        //Given
        jwtProperties.setRefreshTokenValidTime(1L); //RefreshToken 유효기한 짧게변경
        String expiredRefreshToken = tokenProvider.generateRefreshToken(givenLoginMember, givenTokenId);
        jwtProperties.setRefreshTokenValidTime(2592000000L); //RefreshToken 유효기한 원상복구

        //When & Then
        Assertions.assertThrows(RestApiException.class, () -> tokenProvider.getTokenIdFromToken(expiredRefreshToken));
    }
}
