package com.aliens.backend.notification.domain;

import com.aliens.backend.auth.domain.Member;
import jakarta.persistence.*;

@Entity
public class FcmToken {

    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private String token;

    protected FcmToken(Member member, String token) {
        this.member = member;
        this.token = token;
    }

    protected FcmToken() {
    }

    public static FcmToken of(Member member, String token) {
        return new FcmToken(member, token);
    }

    public String getToken() {
        return token;
    }

    public void changeToken(final String token) {
        this.token = token;
    }
}