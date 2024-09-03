package com.aliens.backend.mathcing.service.model;

import com.aliens.backend.auth.domain.Member;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;

import java.util.List;
import java.util.Set;

public record MatchingNotificationMessage(

) {
    public static MulticastMessage createMulticastMessage(Set<Member> members) {
        List<String> participantFcmTokens = members.stream().map(Member::getFcmToken).toList();
        Notification notification = Notification.builder()
                .setTitle("매칭이 완료되었습니다!")
                .setBody("파트너를 확인해보세요!")
                .build();
        MulticastMessage multicastMessage = MulticastMessage.builder()
                .addAllTokens(participantFcmTokens)
                .setNotification(notification)
                .build();
        return multicastMessage;
    }
}
