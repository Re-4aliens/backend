package com.aliens.backend.mathcing.service.event;

import com.aliens.backend.chat.controller.dto.event.ChatRoomCreationEvent;
import com.aliens.backend.chat.service.model.MemberPair;
import com.aliens.backend.mathcing.business.model.Participant;
import com.aliens.backend.mathcing.service.model.MemberPairGroup;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class MatchingEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public MatchingEventPublisher(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void createChatRoom(List<Participant> participants) {
        MemberPairGroup memberPairGroup = MemberPairGroup.from(participants);
        Set<MemberPair> memberPairs = memberPairGroup.getMemberPairs();
        eventPublisher.publishEvent(new ChatRoomCreationEvent(memberPairs));
    }
}
