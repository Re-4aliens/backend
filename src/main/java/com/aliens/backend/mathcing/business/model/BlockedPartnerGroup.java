package com.aliens.backend.mathcing.business.model;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.block.domain.Block;

import java.util.List;

public class BlockedPartnerGroup {
    private final List<Member> blockedPartners;

    public BlockedPartnerGroup(final List<Member> blockedPartners) {
        this.blockedPartners = blockedPartners;
    }

    public static BlockedPartnerGroup from(final List<Block> blockHistories) {
        List<Member> blockedPartners = blockHistories.stream()
                .map(Block::getBlockedMember).toList();
        return new BlockedPartnerGroup(blockedPartners);
    }

    public boolean contains(Participant participant) {
        return blockedPartners.contains(participant.member());
    }

    @Override
    public String toString() {
        return "BlockedPartnerGroup{" +
                "blockedPartners=" + blockedPartners +
                '}';
    }
}
