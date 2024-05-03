package com.aliens.backend.chat.domain;

import com.aliens.backend.auth.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


class ChatParticipantTest {
        @Test
        @DisplayName("ChatParticipant 생성 테스트")
        void CreateChatParticipant() {
            //given
            ChatRoom chatRoom = new ChatRoom();
            Member member = mock(Member.class);
            Member partner = mock(Member.class);

            //when
            ChatParticipant chatParticipant = ChatParticipant.of(chatRoom, member, partner);

            //then
            assertNotNull(chatParticipant);
        }
}