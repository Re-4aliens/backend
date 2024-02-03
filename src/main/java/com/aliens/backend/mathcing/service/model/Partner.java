package com.aliens.backend.mathcing.service.model;

public record Partner(
        Relationship relationship,
        Participant participant
) {
    public static Partner of(Relationship relationship, Participant participant) {
        return new Partner(relationship, participant);
    }
}
