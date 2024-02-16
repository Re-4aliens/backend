package com.aliens.backend.block.domain;

import com.aliens.backend.auth.domain.Member;
import jakarta.persistence.*;

@Entity
public class Block {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blocking_member_id")
    private Member blockingMember;

    @ManyToOne
    @JoinColumn(name = "blocked_member_id")
    private Member blockedMember;

    protected Block() {
    }

    public static Block of(Member blockedMember, Member blockingMember) {
        Block block = new Block();
        block.blockingMember = blockingMember;
        block.blockedMember = blockedMember;
        return block;
    }

    public Member getBlockingMember() {
        return blockingMember;
    }

    public Member getBlockedMember() {
        return blockedMember;
    }

    public Long getBlockingMemberId() {
        return blockingMember.getId();
    }

    public Long getBlockedMemberId() {
        return blockedMember.getId();
    }
}