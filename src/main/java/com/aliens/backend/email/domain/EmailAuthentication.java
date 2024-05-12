package com.aliens.backend.email.domain;

import jakarta.persistence.*;

@Entity
public class EmailAuthentication {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "emailAuthenticatedId")
    private Long id;

    @Column
    private String email;

    @Column
    private boolean isAuthenticated = false;

    protected EmailAuthentication() {
    }

    public EmailAuthentication(String email) {
        this.email = email;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public Long getId() {
        return id;
    }

    public void authenticate() {
        isAuthenticated = true;
    }
}
