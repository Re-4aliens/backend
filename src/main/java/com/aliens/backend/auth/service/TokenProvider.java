package com.aliens.backend.auth.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.MemberRole;
import com.aliens.backend.global.property.JWTProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenProvider {

    private final JWTProperties jwtProperties;

    public TokenProvider(final JWTProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateAccessToken(final LoginMember loginMember) {
        Claims claims = getClaimsFrom(loginMember);
        return getTokenFrom(claims,jwtProperties.getAccessTokenValidTime());
    }

    private Claims getClaimsFrom(final LoginMember loginMember) {
        Claims claims = Jwts.claims();
        claims.put("memberId",loginMember.memberId());
        claims.put("role", loginMember.role().getCode());
        return claims;
    }

    private String getTokenFrom(final Claims claims, final long validTime) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(
                        Keys.hmacShaKeyFor(jwtProperties.getBytesSecretKey()),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    public String generateRefreshToken(final LoginMember loginMember, final Long tokenId) {
        Claims claims = getClaimsFrom(loginMember,tokenId);
        return getTokenFrom(claims,jwtProperties.getRefreshTokenValidTime());
    }

    private Claims getClaimsFrom(final LoginMember loginMember, final Long tokenId) {
        Claims claims = Jwts.claims();
        claims.put("memberId",loginMember.memberId());
        claims.put("role", loginMember.role().getCode());
        claims.put("tokenId", tokenId);
        return claims;
    }

    public LoginMember getLoginMemberFromToken(final String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getBytesSecretKey()))
                .build()
                .parseClaimsJws(removePrefix(token))
                .getBody();

        return new LoginMember(Long.parseLong(String.valueOf(claims.get("memberId"))), MemberRole.of((Integer)claims.get("role")));
    }

    private String removePrefix(String token) {
        if (token != null && token.startsWith("Authentication ")) {
            return token.substring(7);
        }
        return token;
    }

    public boolean isNotExpiredToken(final String token) {
        try{ return !Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getBytesSecretKey()))
                    .build()
                    .parseClaimsJws(removePrefix(token))
                    .getBody()
                    .getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return false;
        }
    }

    public Long getTokenIdFromToken(final String refreshToken) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getBytesSecretKey()))
                .build()
                .parseClaimsJws(removePrefix(refreshToken))
                .getBody();

        return Long.parseLong(String.valueOf(claims.get("tokenId")));
    }
}
