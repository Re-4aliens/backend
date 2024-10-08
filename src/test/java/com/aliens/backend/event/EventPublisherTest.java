package com.aliens.backend.event;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.chat.controller.dto.event.ChatRoomBlockEvent;
import com.aliens.backend.chat.controller.dto.event.ChatRoomCreationEvent;
import com.aliens.backend.chat.service.model.ChatEventListener;
import com.aliens.backend.chat.domain.model.MemberPair;
import com.aliens.backend.global.BaseIntegrationTest;
import com.aliens.backend.global.DummyGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class EventPublisherTest extends BaseIntegrationTest {

    @Autowired ChatEventListener chatEventListener;
    @Autowired ApplicationEventPublisher publisher;
    @Autowired DummyGenerator dummyGenerator;

    @Test
    @DisplayName("채팅방 생성 이벤트 발행 및 처리")
    void handleChatRoomCreationEventTest() {
        // Given
        Set<MemberPair> matchedPairs = generateMultiMemberPair(5);
        ChatRoomCreationEvent event = new ChatRoomCreationEvent(matchedPairs);
        doNothing().when(chatEventListener).handleChatRoomCreationEvent(event);

        // When
        publisher.publishEvent(event);

        // Then
        verify(chatEventListener, times(1)).handleChatRoomCreationEvent(event);
    }

    @Test
    @DisplayName("채팅방 차단 이벤트 발행 및 처리")
    void handleChatRoomBlockEventTest() {
        // Given
        Long givenChatRoomId = 1L;
        ChatRoomBlockEvent event = new ChatRoomBlockEvent(givenChatRoomId);
        doNothing().when(chatEventListener).handleChatRoomBlockEvent(event);

        // When
        publisher.publishEvent(event);

        // Then
        verify(chatEventListener, times(1)).handleChatRoomBlockEvent(event);
    }

    private Set<MemberPair> generateMultiMemberPair(Integer memberCount) {
        List<Member> members = dummyGenerator.generateMultiMember(memberCount);
        Set<MemberPair> memberPairs = new HashSet<>();
        for (int i = 0; i < members.size(); i++) {
            for (int j = 0; j < members.size(); j++) {
                if (i != j) {
                    memberPairs.add(new MemberPair(members.get(i), members.get(j)));
                }
            }
        }
        return memberPairs;
    }
}
