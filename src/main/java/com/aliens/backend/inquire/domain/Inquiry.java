package com.aliens.backend.inquire.domain;

import jakarta.persistence.*;

@Entity
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inquiryId")
    private Long id;

    @Column
    private String content;

    @Column
    private Long memberId;

    public Inquiry(String content, Long memberId) {
        this.content = content;
        this.memberId = memberId;
    }

    protected Inquiry() {
    }

    public String getContent() {
        return content;
    }
}