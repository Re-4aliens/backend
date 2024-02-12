package com.aliens.backend.block.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.block.controller.dto.BlockRequest;
import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.block.domain.repository.BlockRepository;
import com.aliens.backend.chat.domain.repository.ChatRoomRepository;
import com.aliens.backend.global.BaseServiceTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.global.response.success.ChatSuccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class BlockServiceTest extends BaseServiceTest {

    @Autowired BlockService blockService;
    @Autowired BlockRepository blockRepository;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired DummyGenerator dummyGenerator;


    @Test
    @DisplayName("채팅 상대 차단")
    void blockPartner() {
        //Given
        List<Member> members = dummyGenerator.generateMultiMember(2);
        Member member = members.get(0);
        Member partner = members.get(1);
        LoginMember myLoginMember = member.getLoginMember();

        ChatRoom chatRoom = setChatRoom(member, partner);

        BlockRequest blockRequest = new BlockRequest(partner.getId(), chatRoom.getRoomId());

        //When
        String result = blockService.blockPartner(myLoginMember, blockRequest);

        //Then
        String expectedResponse = ChatSuccess.BLOCK_SUCCESS.getMessage();
        Assertions.assertEquals(expectedResponse, result);
    }

    private ChatRoom setChatRoom(final Member member, final Member partner) {
        ChatRoom chatRoom = ChatRoom.of(member, partner);
        chatRoomRepository.save(chatRoom);
        return chatRoom;
    }
}