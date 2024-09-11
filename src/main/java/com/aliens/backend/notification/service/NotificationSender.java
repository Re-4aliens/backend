package com.aliens.backend.notification.service;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.MemberError;
import com.aliens.backend.notification.domain.FcmTokenRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class NotificationSender {

    private final ApplicationEventPublisher eventPublisher;
    private final MemberRepository memberRepository;
    private final FcmTokenRepository fcmTokenRepository;

    public NotificationSender(ApplicationEventPublisher eventPublisher, MemberRepository memberRepository, FcmTokenRepository fcmTokenRepository) {
        this.eventPublisher = eventPublisher;
        this.memberRepository = memberRepository;
        this.fcmTokenRepository = fcmTokenRepository;
    }

    public void send(Message message) {
        var fcmMessage = createFcmMessage(message);
        eventPublisher.publishEvent(fcmMessage);
    }

    private com.google.firebase.messaging.Message createFcmMessage(Message message) {
        Member sender = memberRepository.findById(message.getSenderId()).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
        Member receiver = memberRepository.findById(message.getReceiverId()).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
        return com.google.firebase.messaging.Message.builder()
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(sender.getProfileName())
                        .setBody(message.getContent())
                        .build())
                .setToken(findFcmTokenByMember(receiver))
                .build();
    }

    private String findFcmTokenByMember(Member receiver) {
        return fcmTokenRepository.findByMember(receiver).get().getToken();
    }
}
