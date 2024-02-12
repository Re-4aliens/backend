package com.aliens.backend.chat.domain;

import com.aliens.backend.auth.domain.Member;
import jakarta.persistence.*;

@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column
    private Long id;

    private Long roomId;

    @Column
    private ChatRoomStatus status;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Member partner;

    protected ChatRoom() {
    }

    public static ChatRoom of(final Member me, final Member partner) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.member = me;
        chatRoom.partner = partner;
        return chatRoom;
    }

    public Long getRoomId() {
        return roomId;
    }

    public ChatRoomStatus getStatus() {
        return status;
    }

    public void block() {
        this.status = ChatRoomStatus.BLOCKED;
    }
}