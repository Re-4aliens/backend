package com.aliens.backend.notification.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.CommonError;
import com.aliens.backend.global.response.error.MemberError;
import com.aliens.backend.global.response.error.NotificationError;
import com.aliens.backend.notification.domain.repository.NotificationRepository;
import com.aliens.backend.notification.controller.dto.NotificationRequest;
import com.aliens.backend.notification.controller.dto.NotificationResponse;
import com.aliens.backend.notification.domain.FcmToken;
import com.aliens.backend.notification.domain.repository.FcmTokenRepository;
import com.aliens.backend.notification.domain.Notification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final MemberRepository memberRepository;


    public NotificationService(final NotificationRepository notificationRepository,
                               final FcmTokenRepository fcmTokenRepository,
                               final MemberRepository memberRepository) {
        this.notificationRepository = notificationRepository;
        this.fcmTokenRepository = fcmTokenRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void registerFcmToken(final LoginMember loginMember, final String fcmToken) {
        Member member = getMember(loginMember.memberId());
        String parsedFcmToken = parseFcmToken(fcmToken);
        Optional<FcmToken> optionalFcmToken = fcmTokenRepository.findByMember(member);

        if (optionalFcmToken.isPresent()) {
            FcmToken fcmTokenEntity = optionalFcmToken.get();
            fcmTokenEntity.changeToken(parsedFcmToken);
            return;
        }

        FcmToken fcmTokenEntity = FcmToken.of(member, parsedFcmToken);
        fcmTokenRepository.save(fcmTokenEntity);
    }

    private String parseFcmToken(final String fcmToken) {
        return fcmToken.substring(13, fcmToken.length()-2);
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

    @Transactional
    public void saveNotification(NotificationRequest request) {
        for(Member member : request.members()) {
            Notification notification = Notification.of(request, member);
            notificationRepository.save(notification);
        }
    }

    private FcmToken getFcmTokenByMember(final Member member) {
        return fcmTokenRepository.findByMember(member).orElseThrow(() -> new RestApiException(CommonError.FCM_MESSAGING_ERROR));
    }

    @Transactional(readOnly = true)
    public Boolean getStatus(final LoginMember loginMember) {
        Member member = getMember(loginMember.memberId());
        FcmToken token = getFcmTokenByMember(member);
        return token.isAccepted();
    }

    @Transactional
    public void changeAcceptation(final LoginMember loginMember, final Boolean decision) {
        Member member = getMember(loginMember.memberId());
        FcmToken token = getFcmTokenByMember(member);
        token.changeAccept(decision);
    }
}