package com.aliens.backend.chat.domain;

import com.aliens.backend.auth.domain.Member;
import jakarta.persistence.*;

@Entity
public class ChatParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "partner_id")
    private Member partner;

    protected ChatParticipant() {
    }

    private ChatParticipant(final ChatRoom chatRoom, final Member member, final Member partner) {
        this.chatRoom = chatRoom;
        this.member = member;
        this.partner = partner;
    }

    public static ChatParticipant of(final ChatRoom chatRoom, final Member member, final Member partner) {
        return new ChatParticipant(chatRoom, member, partner);
    }

    protected void setChatRoom(final ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
}