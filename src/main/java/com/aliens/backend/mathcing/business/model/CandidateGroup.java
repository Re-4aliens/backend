package com.aliens.backend.mathcing.business.model;

import java.util.LinkedList;
import java.util.Queue;

public class CandidateGroup {
    private final Queue<Participant> candidateQueue;

    public CandidateGroup(final Queue<Participant> candidateQueue) {
        this.candidateQueue = candidateQueue;
    }

    public static CandidateGroup initWithEmpty() {
        return new CandidateGroup(new LinkedList<>());
    }

    public static CandidateGroup of(ParticipantGroup participantGroup) {
        return new CandidateGroup(new LinkedList<>(participantGroup.getParticipants()));
    }

    public void add(Participant participant) {
        candidateQueue.add(participant);
    }

    public Participant poll() {
        return candidateQueue.poll();
    }

    public boolean isEmpty() {
        return candidateQueue.isEmpty();
    }
}
