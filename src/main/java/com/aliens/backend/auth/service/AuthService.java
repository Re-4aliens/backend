package com.aliens.backend.auth.service;

import com.aliens.backend.auth.controller.dto.AuthToken;
import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.controller.dto.LoginRequest;
import com.aliens.backend.auth.domain.*;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.auth.domain.repository.TokenRepository;
import com.aliens.backend.global.error.MemberError;
import com.aliens.backend.global.error.TokenError;
import com.aliens.backend.global.exception.RestApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public static final String LOGOUT_SUCCESS = "로그아웃 되었습니다.";

    public AuthService(final MemberRepository memberRepository,
                       final TokenRepository tokenRepository,
                       final PasswordEncoder passwordEncoder,
                       final TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Transactional
    public AuthToken login(LoginRequest loginRequest) {
        Member member = getMemberEntityFromEmail(loginRequest.email());
        passwordCheck(loginRequest.password(), member);
        return generateAuthToken(member);
    }

    private Member getMemberEntityFromEmail(final String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
        if (member.isWithdraw()) {
            throw new RestApiException(MemberError.WITHDRAW_MEMBER);
        }
        return member;
    }

    private void passwordCheck(final String password, final Member member) {
        if(!member.isCorrectPassword(passwordEncoder.encrypt(password))) {
            throw new RestApiException(MemberError.INCORRECT_PASSWORD);
        }
    }

    private AuthToken generateAuthToken(final Member member) {
        LoginMember loginMember = member.getLoginMember();

        String accessToken = tokenProvider.generateAccessToken(loginMember);
        String refreshToken = generateRefreshToken(member, loginMember);

        return new AuthToken(accessToken,refreshToken);
    }

    private String generateRefreshToken(final Member member, final LoginMember loginMember) {
        Token token = new Token(member);
        tokenRepository.save(token);
        String refreshToken = tokenProvider.generateRefreshToken(loginMember,token.getId());

        token.putRefreshToken(refreshToken);
        return refreshToken;
    }

    @Transactional
    public String logout(final AuthToken authToken) {
        Long tokenId = tokenProvider.getTokenIdFromToken(authToken.refreshToken());
        Token token = getTokenEntity(tokenId);
        token.expire();
        return LOGOUT_SUCCESS;
    }

    private Token getTokenEntity(final Long tokenId) {
        return tokenRepository.findById(tokenId).orElseThrow(() -> new RestApiException(TokenError.NULL_REFRESH_TOKEN));
    }

    @Transactional
    public AuthToken reissue(final AuthToken authToken) {
        tokenExpiredCheck(authToken);
        LoginMember loginMember = tokenProvider.getLoginMemberFromToken(authToken.refreshToken());
        String newAccessToken = tokenProvider.generateAccessToken(loginMember);
        return new AuthToken(newAccessToken, authToken.refreshToken());
    }

    private void tokenExpiredCheck(final AuthToken authToken) {
        if (tokenProvider.isNotExpiredToken(authToken.accessToken())) {
            throw new RestApiException(TokenError.NOT_ACCESS_TOKEN_FOR_REISSUE);
        }
        if (!tokenProvider.isNotExpiredToken(authToken.refreshToken())) {
            throw new RestApiException(TokenError.EXPIRED_REFRESH_TOKEN);
        }

        dbTokenExpiredCheck(authToken.refreshToken());
    }

    private void dbTokenExpiredCheck(final String refreshToken) {
        Long tokenId = tokenProvider.getTokenIdFromToken(refreshToken);
        Token token = getTokenEntity(tokenId);

        if (token.isExpired()) {
            throw new RestApiException(TokenError.EXPIRED_REFRESH_TOKEN);
        }
        token.changeRecentLogin();
    }
}
