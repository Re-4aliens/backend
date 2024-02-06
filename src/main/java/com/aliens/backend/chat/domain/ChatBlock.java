package com.aliens.backend.chat.domain;

import com.aliens.backend.chat.controller.dto.request.ChatBlockRequest;
import jakarta.persistence.*;

@Entity
@Table(name = "CHAT_BLOCK")
public class ChatBlock {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(name = "blocked_member_id")
    private Long blockedMemberId;
    @Column(name = "blocking_member_id")
    private Long blockingMemberId;

    protected ChatBlock() {
    }

    public static ChatBlock of(Long blockingMemberId, ChatBlockRequest chatBlockRequest) {
        ChatBlock chatBlock = new ChatBlock();
        chatBlock.blockedMemberId = chatBlockRequest.partnerId();
        chatBlock.blockingMemberId = blockingMemberId;
        return chatBlock;
    }
}