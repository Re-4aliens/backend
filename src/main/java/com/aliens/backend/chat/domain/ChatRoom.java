package com.aliens.backend.chat.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column
    private ChatRoomStatus status;
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatParticipant> participants;

    protected ChatRoom() {
    }

    public ChatRoom(final ChatParticipant participant1, final ChatParticipant participant2) {
        this.status = ChatRoomStatus.WAITING;
        this.participants = List.of(participant1, participant2);
        participant1.setChatRoom(this);
        participant2.setChatRoom(this);
    }

    public Long getId() {
        return id;
    }

    public ChatRoomStatus getStatus() {
        return status;
    }

    public void block() {
        this.status = ChatRoomStatus.BLOCKED;
    }
}