package com.aliens.backend.auth.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Token {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tokenId")
    private Long id;

    @Column
    private String refreshToken;

    @Column
    private boolean isExpired = false;

    @Column
    private LocalDateTime recentLogin = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    protected Token() {
    }

    public Token(Member member) {
        this.member = member;
    }

    public void putRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void expire() {
        isExpired = true;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public Long getId() {
        return id;
    }

    public void changeRecentLogin() {
        recentLogin = LocalDateTime.now();
    }
}