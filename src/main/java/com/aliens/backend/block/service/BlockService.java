package com.aliens.backend.block.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.block.controller.dto.BlockRequest;
import com.aliens.backend.block.domain.Block;
import com.aliens.backend.block.domain.repository.BlockRepository;
import com.aliens.backend.chat.controller.dto.event.ChatRoomBlockEvent;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.MemberError;
import com.aliens.backend.global.response.success.ChatSuccess;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class BlockService {
    private final BlockRepository blockRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    public BlockService(final BlockRepository blockRepository, final MemberRepository memberRepository, final ApplicationEventPublisher eventPublisher) {
        this.blockRepository = blockRepository;
        this.memberRepository = memberRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public String blockPartner(LoginMember loginMember, BlockRequest blockRequest) {
        blockMember(loginMember, blockRequest);
        blockChatRoom(blockRequest);
        return ChatSuccess.BLOCK_SUCCESS.getMessage();
    }

    private void blockMember(final LoginMember loginMember, final BlockRequest blockRequest) {
        Member blockingMember = findMemberById(loginMember.memberId());
        Member blockedMember = findMemberById(blockRequest.partnerId());

        Block block = Block.of(blockingMember, blockedMember);
        blockRepository.save(block);
    }

    private void blockChatRoom(final BlockRequest blockRequest) {
        ChatRoomBlockEvent event = new ChatRoomBlockEvent(blockRequest.chatRoomId());
        eventPublisher.publishEvent(event);
    }

    private Member findMemberById(final Long partnerId) {
        return memberRepository.findById(partnerId).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }
}