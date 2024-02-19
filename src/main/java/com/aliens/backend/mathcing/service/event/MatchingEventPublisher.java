package com.aliens.backend.mathcing.service.event;

import com.aliens.backend.chat.controller.dto.event.ChatRoomCreationEvent;
import com.aliens.backend.chat.controller.dto.event.ChatRoomExpireEvent;
import com.aliens.backend.chat.service.model.MemberPair;
import com.aliens.backend.mathcing.business.model.Participant;
import com.aliens.backend.mathcing.domain.MatchingResult;
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

    public void expireChatRoom(List<MatchingResult> matchingResults) {
        MemberPairGroup expiredMemberPairGroup = MemberPairGroup.fromMatchingResults(matchingResults);
        Set<MemberPair> expiredMemberPairs = expiredMemberPairGroup.getMemberPairs();
        eventPublisher.publishEvent(new ChatRoomExpireEvent(expiredMemberPairs));
    }

    public void sendNotification(List<Participant> participants) {
        List<Participant> participantsHasPartner = participants.stream()
                .filter(Participant::hasPartner).toList();
        MulticastMessage multicastMessage = MatchingNotificationMessage.createMulticastMessage(participantsHasPartner);
        eventPublisher.publishEvent(multicastMessage);
    }
}
