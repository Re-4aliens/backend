package com.aliens.backend.email.domain;

import jakarta.persistence.*;

@Entity
@Table(schema = "EMAIL_AUTHENTICATION")
public class EmailAuthentication {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String email;

    @Column(name = "is_authenticated")
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
