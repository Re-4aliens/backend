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

    public String generateRefreshToken(final LoginMember loginMember) {
        Claims claims = getClaimsFrom(loginMember);
        return getTokenFrom(claims,jwtProperties.getRefreshTokenValidTime());
    }

    public LoginMember getLoginMemberFromToken(final String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getBytesSecretKey()))
                .build()
                .parseClaimsJws(removeBearer(token))
                .getBody();

        return new LoginMember(Long.parseLong(String.valueOf(claims.get("memberId"))), MemberRole.fromCode((Integer)claims.get("role")));
    }

    private String removeBearer(String token) {
        if (token != null && token.startsWith("Authentication ")) {
            return token.substring(7);
        }
        return token;
    }

    public boolean isNotExpiredToken(final String token) {
        try{ return !Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtProperties.getBytesSecretKey()))
                    .build()
                    .parseClaimsJws(removeBearer(token))
                    .getBody()
                    .getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return false;
        }
    }
}
