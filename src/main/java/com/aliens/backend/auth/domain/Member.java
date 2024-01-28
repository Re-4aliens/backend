package com.aliens.backend.auth.domain;

import com.aliens.backend.auth.controller.dto.LoginMember;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Member {

    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private MemberRole role;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Token> tokens = new ArrayList<>();

    protected Member() {
    }

    public Member(final String email, final String password, final MemberRole role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public boolean isCorrectPassword(String password) {
        return this.password.equals(password);
    }

    public LoginMember getLoginMember() {
        return new LoginMember(id,role);
    }

    @Override
    public String toString() {
        return String.format("email:  %s, password : %s, role : %s", this.email, this.password, this.role);
    }
}