package com.aliens.backend.block.domain;

import com.aliens.backend.auth.domain.Member;
import jakarta.persistence.*;

@Entity
public class Block {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "blockId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blockingMemberId")
    private Member blockingMember;

    @ManyToOne
    @JoinColumn(name = "blockedMemberId")
    private Member blockedMember;

    protected Block() {
    }

    private Block(Member blockingMember, Member blockedMember) {
        this.blockingMember = blockingMember;
        this.blockedMember = blockedMember;
    }

    public static Block of(Member blockedMember, Member blockingMember) {
        return new Block(blockingMember, blockedMember);
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