package com.aliens.backend.mathcing.business.model;

import com.aliens.backend.block.domain.Block;

import java.util.List;

public class BlockedPartnerGroup {
    private final List<Long> blockedPartners;

    public BlockedPartnerGroup(final List<Long> blockedPartners) {
        this.blockedPartners = blockedPartners;
    }

    public static BlockedPartnerGroup from(final List<Block> blockHistories) {
        List<Long> blockedPartners = blockHistories.stream()
                .mapToLong(Block::getBlockedMemberId).boxed().toList();
        return new BlockedPartnerGroup(blockedPartners);
    }

    public boolean contains(Participant participant) {
        return blockedPartners.contains(participant.memberId());
    }

    @Override
    public String toString() {
        return "BlockedPartnerGroup{" +
                "blockedPartners=" + blockedPartners +
                '}';
    }
}
