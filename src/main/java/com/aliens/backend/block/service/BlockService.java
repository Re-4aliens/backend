package com.aliens.backend.block.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.block.domain.repository.BlockRepository;
import com.aliens.backend.block.controller.dto.BlockRequest;
import com.aliens.backend.block.domain.Block;
import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.domain.repository.ChatRoomRepository;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.MemberError;
import com.aliens.backend.global.response.success.ChatSuccess;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlockService {
    private final BlockRepository blockRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;

    public BlockService(BlockRepository blockRepository, ChatRoomRepository chatRoomRepository, final MemberRepository memberRepository) {
        this.blockRepository = blockRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public String blockPartner(LoginMember loginMember, BlockRequest blockRequest) {
        blockMember(loginMember, blockRequest);
        blockChatRoom(blockRequest);
        return ChatSuccess.BLOCK_SUCCESS.getMessage();
    }

    private void blockMember(final LoginMember loginMember, final BlockRequest blockRequest) {
        Member blockingMember = getMember(loginMember);
        Member blockedMember = findMemberById(blockRequest.partnerId());

        Block block = Block.of(blockingMember, blockedMember);
        blockRepository.save(block);
    }

    private void blockChatRoom(final BlockRequest blockRequest) {
        List<ChatRoom> chatRooms = findChatRoomsById(blockRequest.chatRoomId());
        chatRooms.forEach(ChatRoom::block);
        chatRoomRepository.saveAll(chatRooms);
    }

    private List<ChatRoom> findChatRoomsById(final Long chatRoomId) {
        return chatRoomRepository.findByRoomId(chatRoomId);
    }

    private Member findMemberById(final Long partnerId) {
        return memberRepository.findById(partnerId).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }

    private Member getMember(final LoginMember loginMember) {
        return memberRepository.findById(loginMember.memberId()).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }
}