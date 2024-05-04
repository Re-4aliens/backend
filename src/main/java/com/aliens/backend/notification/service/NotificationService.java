package com.aliens.backend.notification.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.MemberError;
import com.aliens.backend.global.response.error.NotificationError;
import com.aliens.backend.notification.domain.NotificationType;
import com.aliens.backend.notification.domain.repository.NotificationRepository;
import com.aliens.backend.notification.controller.dto.NotificationRequest;
import com.aliens.backend.notification.controller.dto.NotificationResponse;
import com.aliens.backend.notification.domain.FcmToken;
import com.aliens.backend.notification.domain.FcmTokenRepository;
import com.aliens.backend.notification.domain.Notification;
import com.google.firebase.messaging.MulticastMessage;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;


    public NotificationService(final NotificationRepository notificationRepository,
                               final FcmTokenRepository fcmTokenRepository,
                               final MemberRepository memberRepository,
                               final ApplicationEventPublisher eventPublisher) {
        this.notificationRepository = notificationRepository;
        this.fcmTokenRepository = fcmTokenRepository;
        this.memberRepository = memberRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void registerFcmToken(final LoginMember loginMember, final String fcmToken) {
        Member member = getMember(loginMember.memberId());
        Optional<FcmToken> optionalFcmToken = fcmTokenRepository.findById(loginMember.memberId());

        if (optionalFcmToken.isPresent()) {
            FcmToken fcmTokenEntity = optionalFcmToken.get();
            fcmTokenEntity.changeToken(fcmToken);
            return;
        }

        FcmToken fcmTokenEntity = FcmToken.of(member, fcmToken);
        fcmTokenRepository.save(fcmTokenEntity);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(final LoginMember loginMember) {
        List<Notification> notifications = notificationRepository.findNotificationsByMemberId(loginMember.memberId());
        return notifications.stream().map(Notification::getNotificationResponse).toList();
    }

    @Transactional
    public void readNotification(final LoginMember loginMember, final Long notificationId) {
        Notification notification = getNotification(notificationId);

        if(!notification.isOwner(loginMember.memberId())) {
            throw new RestApiException(NotificationError.IS_NOT_OWNER);
        }

        notification.read();
    }

    private Notification getNotification(final Long notificationId) {
        return notificationRepository.findById(notificationId).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }

    private Member getMember(final Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }

    @EventListener
    public void saveNotification(NotificationRequest request) {
        List<String> tokens = new ArrayList<>();

        for(Long memberId : request.memberIds()) {
            Member member = getMember(memberId);
            Notification notification = Notification.of(request, member);

            notificationRepository.save(notification);

            FcmToken fcmToken = getFcmTokenByMemberId(memberId);
            tokens.add(fcmToken.getToken());
        }

        MulticastMessage multiMessage = makeMessage(request, tokens);
        eventPublisher.publishEvent(multiMessage);
    }

    private FcmToken getFcmTokenByMemberId(final Long memberId) {
        return fcmTokenRepository.findById(memberId).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }

    private MulticastMessage makeMessage(NotificationRequest request,
                                         List<String> tokens) {

        return MulticastMessage.builder()
                .putData("type", NotificationType.PERSONAL.toString())
                .putData("content", request.content())
                .addAllTokens(tokens)
                .build();
    }
}
