package com.aliens.backend.mathcing.business.model;

import com.aliens.backend.block.domain.Block;
import com.aliens.backend.mathcing.domain.MatchingApplication;

import java.util.List;

public class BlockHistoryGroup {
    private final List<Block> blockHistories;

    private BlockHistoryGroup(final List<Block> blockHistories) {
        this.blockHistories = blockHistories;
    }

    public static BlockHistoryGroup of(final List<Block> blockHistories) {
        return new BlockHistoryGroup(blockHistories);
    }

    public List<Block> getBlockHistoriesWith(MatchingApplication matchingApplication) {
        List<Block> filteredBlockHistories = blockHistories.stream()
                .filter(blockHistory -> blockHistory.getBlockingMemberId().equals(matchingApplication.getMemberId()))
                .toList();
        return filteredBlockHistories;
    }
}
