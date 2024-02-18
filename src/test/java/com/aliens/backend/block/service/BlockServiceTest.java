package com.aliens.backend.block.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.block.controller.dto.BlockRequest;
import com.aliens.backend.block.domain.repository.BlockRepository;
import com.aliens.backend.chat.domain.repository.ChatRoomRepository;
import com.aliens.backend.chat.service.ChatService;
import com.aliens.backend.global.BaseServiceTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.global.response.success.ChatSuccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

class BlockServiceTest extends BaseServiceTest {

    @Autowired BlockService blockService;
    @Autowired BlockRepository blockRepository;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired DummyGenerator dummyGenerator;
    @Autowired ApplicationEventPublisher publisher;
    @SpyBean ChatService chatService;

    LoginMember myLoginMember;
    Member member;
    Member partner;
    Long chatRoomId;

    @BeforeEach
    void setUp() {
        List<Member> members = dummyGenerator.generateMultiMember(2);
        member = members.get(0);
        partner = members.get(1);
        myLoginMember = member.getLoginMember();
        chatRoomId = 1L;
        doNothing().when(chatService).handleChatRoomBlockEvent(any());
    }

    @Test
    @DisplayName("채팅 상대 차단")
    void blockPartner() {
        //Given
        BlockRequest blockRequest = new BlockRequest(partner.getId(), chatRoomId);

        //When
        String result = blockService.blockPartner(myLoginMember, blockRequest);

        //Then
        String expectedResponse = ChatSuccess.BLOCK_SUCCESS.getMessage();
        Assertions.assertEquals(expectedResponse, result);
    }
}