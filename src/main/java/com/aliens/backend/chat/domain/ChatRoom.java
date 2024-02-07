package com.aliens.backend.chat.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "CHAT_ROOM")
public class ChatRoom {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column
    private Long id;
    private Long roomId;
    @Column(name = "member_id")
    private Long memberId;
    @Column(name = "partner_id")
    private Long partnerId;
    @Column
    private ChatRoomStatus status;

    protected ChatRoom() {
    }

    public Long getRoomId() {
        return roomId;
    }

    public Long getPartnerId() {
        return partnerId;
    }

    public ChatRoomStatus getStatus() {
        return status;
    }

    public void block() {
        this.status = ChatRoomStatus.BLOCKED;
    }
}