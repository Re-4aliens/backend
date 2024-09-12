package com.aliens.backend.notification.service;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.global.response.error.CommonError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.MemberError;
import com.aliens.backend.notification.controller.dto.NotificationRequest;
import com.aliens.backend.notification.domain.repository.FcmTokenRepository;
import com.google.firebase.messaging.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class FcmSender {
    private final static String MATCHING_SUCCESS_MESSAGE_TITLE = "매칭이 완료되었습니다!";
    private final static String MATCHING_SUCCESS_MESSAGE_BODY = "파트너를 확인해보세요!";
    private final MemberRepository memberRepository;
    private final FcmTokenRepository fcmTokenRepository;

    public FcmSender(MemberRepository memberRepository,
                     FcmTokenRepository fcmTokenRepository) {
        this.memberRepository = memberRepository;
        this.fcmTokenRepository = fcmTokenRepository;
    }

    public void sendBoardNotification(NotificationRequest request, List<String> tokens) {
        MulticastMessage message = makeMessage(request, tokens);
        sendMultiFcm(message);
    }

    private MulticastMessage makeMessage(NotificationRequest request,
                                         List<String> tokens) {
        var notification = com.google.firebase.messaging.Notification.builder()
                .setTitle(String.valueOf(request.boardCategory()))
                .setBody(request.content()).
                build();

        return MulticastMessage.builder()
                .setNotification(notification)
                .addAllTokens(tokens)
                .build();
    }

    private void sendMultiFcm(MulticastMessage message) {
        try {
            FirebaseMessaging.getInstance().sendEachForMulticast(message);
        } catch (Exception e) {
            throw new RestApiException(CommonError.FCM_MESSAGING_ERROR);
        }
    }

    public void sendChatMessage(com.aliens.backend.chat.domain.Message message) {
        var fcmMessage = createFcmMessage(message);
        sendSingleFcm(fcmMessage);
    }

    private com.google.firebase.messaging.Message createFcmMessage(com.aliens.backend.chat.domain.Message request) {
        Member sender = getMember(request.getSenderId());
        Member receiver = getMember(request.getReceiverId());

        return com.google.firebase.messaging.Message.builder()
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(sender.getMemberPage().name())
                        .setBody(request.getContent())
                        .build())
                .setToken(findFcmTokenByMember(receiver))
                .build();
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }

    private String findFcmTokenByMember(Member member) {
        return fcmTokenRepository.findByMember(member).get().getToken();
    }

    private void sendSingleFcm(Message message) {
        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (Exception e) {
            throw new RestApiException(CommonError.FCM_MESSAGING_ERROR);
        }
    }

    public void sentMatchingNotification(Set<Member> members) {
        List<String> tokens = members.stream().map(this::findFcmTokenByMember).toList();
        Notification notification = Notification.builder()
                .setTitle(MATCHING_SUCCESS_MESSAGE_TITLE)
                .setBody(MATCHING_SUCCESS_MESSAGE_BODY)
                .build();

        MulticastMessage multicastMessage = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(notification)
                .build();

        sendMultiFcm(multicastMessage);
    }
}
