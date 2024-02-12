package com.aliens.backend.chatting.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.chat.controller.dto.request.ChatReportRequest;
import com.aliens.backend.chat.domain.ChatReportCategory;
import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.domain.repository.ChatRoomRepository;
import com.aliens.backend.chat.service.ChatReportService;
import com.aliens.backend.global.BaseServiceTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.global.response.success.ChatSuccess;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class ChatReportServiceTest extends BaseServiceTest {

    @Autowired ChatReportService chatReportService;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired DummyGenerator dummyGenerator;

    @Test
    @DisplayName("채팅 상대 신고")
    void blockPartner() {
        //given
        List<Member> members = dummyGenerator.generateMultiMember(2);
        Member member = members.get(0);
        Member partner = members.get(1);
        LoginMember loginMember = member.getLoginMember();

        ChatRoom chatRoom = ChatRoom.of(member, partner);
        chatRoomRepository.save(chatRoom);

        ChatReportRequest request = createRequest(partner, chatRoom);

        //When
        String result = chatReportService.reportPartner(loginMember, request);

        //Then
        Assertions.assertEquals(ChatSuccess.REPORT_SUCCESS.getMessage(), result);
    }

    private ChatReportRequest createRequest(final Member partner, final ChatRoom chatRoom) {
        ChatReportCategory givenCategory = ChatReportCategory.ETC;
        String givenContent = "신고 사유";

        return new ChatReportRequest(partner.getId(),
                chatRoom.getRoomId(),
                givenCategory,
                givenContent
        );
    }
}