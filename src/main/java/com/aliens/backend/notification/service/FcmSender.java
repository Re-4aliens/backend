package com.aliens.backend.notification.service;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.board.domain.Comment;
import com.aliens.backend.global.response.error.CommonError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.MemberError;
import com.aliens.backend.notification.domain.repository.FcmTokenRepository;
import com.google.firebase.messaging.*;
import com.aliens.backend.global.response.log.InfoLogResponse;
import com.aliens.backend.global.response.success.NotificationSuccess;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class FcmSender {
    private final static String COMMENT_BODY = "새로운 댓글이 달렸어요 : ";
    private final static String FRIENDSHIP_TITLE = "Friendship";
    private final static String MATCHING_SUCCESS_MESSAGE_BODY = "Friendship 매칭이 완료되었습니다. 파트너를 확인해보세요!";
    private final MemberRepository memberRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final ObjectMapper objectMapper;
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public FcmSender(MemberRepository memberRepository,
                     FcmTokenRepository fcmTokenRepository,
                     ObjectMapper objectMapper) {
        this.memberRepository = memberRepository;
        this.fcmTokenRepository = fcmTokenRepository;
        this.objectMapper = objectMapper;
    }

    public void sendBoardNotification(Comment comment, Member writer) {
        Notification notification = Notification.builder()
                .setTitle(FRIENDSHIP_TITLE)
                .setBody(COMMENT_BODY + comment.getContent())
                .build();

        String token = findFcmTokenByMember(writer);
        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();
        sendSingleFcm(message);
    }

    public void sendBoardNotification(Comment comment, List<Member> writers) {
        Notification notification = Notification.builder()
                .setTitle(FRIENDSHIP_TITLE)
                .setBody(COMMENT_BODY + comment.getContent())
                .build();

        for(Member writer : writers) {
            String token = findFcmTokenByMember(writer);
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(notification)
                    .build();
            sendSingleFcm(message);
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
            InfoLogResponse response = InfoLogResponse.from(NotificationSuccess.SEND_SINGLE_NOTIFICATION_SUCCESS);
            log.info(objectMapper.writeValueAsString(response));
        } catch (Exception e) {
            throw new RestApiException(CommonError.FCM_MESSAGING_ERROR);
        }
    }

    public void sendMatchedNotification(Set<Member> members) {
        List<String> tokens = members.stream().map(this::findFcmTokenByMember).toList();
        Notification notification = Notification.builder()
                .setTitle(FRIENDSHIP_TITLE)
                .setBody(MATCHING_SUCCESS_MESSAGE_BODY)
                .build();

        tokens.forEach(token -> {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(notification)
                    .build();
            sendSingleFcm(message);
        });
    }
}
