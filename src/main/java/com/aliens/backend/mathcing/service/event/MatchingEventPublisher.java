package com.aliens.backend.mathcing.service.event;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.chat.controller.dto.event.ChatRoomCreationEvent;
import com.aliens.backend.chat.controller.dto.event.ChatRoomExpireEvent;
import com.aliens.backend.chat.domain.model.MemberPair;
import com.aliens.backend.mathcing.business.model.Participant;
import com.aliens.backend.mathcing.service.model.MatchingNotificationMessage;
import com.aliens.backend.mathcing.service.model.MemberPairGroup;
import com.google.firebase.messaging.MulticastMessage;
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
        MemberPairGroup matchedMemberPairGroup = MemberPairGroup.fromParticipants(participants);
        Set<MemberPair> matchedMemberPairs = matchedMemberPairGroup.getMemberPairs();
        eventPublisher.publishEvent(new ChatRoomCreationEvent(matchedMemberPairs));
    }

    public void expireChatRoom() {
        eventPublisher.publishEvent(new ChatRoomExpireEvent());
    }

    public void sendMatchedNotification(Set<Member> members) {
        MulticastMessage multicastMessage = MatchingNotificationMessage.createMulticastMessage(members);
        eventPublisher.publishEvent(multicastMessage);
    }
}
