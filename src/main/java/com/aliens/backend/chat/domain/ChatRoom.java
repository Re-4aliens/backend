package com.aliens.backend.chat.domain;

import com.aliens.backend.chat.domain.model.MemberPair;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "chatRoomId")
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

    public static ChatRoom createChatRoom(MemberPair pair) {
        ChatParticipant participant1 = ChatParticipant.of(null, pair.first(), pair.second());
        ChatParticipant participant2 = ChatParticipant.of(null, pair.second(), pair.first());
        return new ChatRoom(participant1, participant2);
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