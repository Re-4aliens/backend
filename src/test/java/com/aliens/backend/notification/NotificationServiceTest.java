package com.aliens.backend.notification;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.board.domain.enums.BoardCategory;
import com.aliens.backend.global.BaseIntegrationTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.notification.controller.dto.NotificationRequest;
import com.aliens.backend.notification.controller.dto.NotificationResponse;
import com.aliens.backend.notification.domain.FcmToken;
import com.aliens.backend.notification.domain.repository.FcmTokenRepository;
import com.aliens.backend.notification.domain.Notification;
import com.aliens.backend.notification.domain.repository.NotificationRepository;
import com.aliens.backend.notification.service.NotificationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class NotificationServiceTest extends BaseIntegrationTest {

    @Autowired NotificationService notificationService;
    @Autowired DummyGenerator dummyGenerator;
    @Autowired NotificationRepository notificationRepository;
    @Autowired FcmTokenRepository fcmTokenRepository;

    LoginMember loginMember;
    Member member;

    @BeforeEach
    void setUp() {
        member = dummyGenerator.generateSingleMember();
        loginMember = member.getLoginMember();
    }

    @Test
    @DisplayName("fcm토큰 저장")
    void registerFcmTokenTest() {
        //Given
        String givenToken = "{fcmToken : fcmTokenExample}";
        String expectedToken = "fcmTokenExample";
        //When
        notificationService.registerFcmToken(loginMember, givenToken);

        //Then
        FcmToken result = fcmTokenRepository.findAll().get(0);
        Assertions.assertEquals(expectedToken, givenToken);
    }

    @ParameterizedTest
    @DisplayName("알림 가져오기 - 최대 20개 호출")
    @CsvSource(value = {"5:5", "25:20"}, delimiter = ':')
    void getNotificationsTest(int givenAmount, int expectedResponseAmount ) {
        //Given
        dummyGenerator.generateNotificationWithCount(member, givenAmount);

        //When
        List<NotificationResponse> notifications = notificationService.getNotifications(loginMember);

        //Then
        Assertions.assertEquals(notifications.size(), expectedResponseAmount);
    }

    @Test
    @DisplayName("알림 읽음 처리")
    void readNotificationTest() {
        //Given
        Notification notification = dummyGenerator.generateSingleNotification(member);

        //When
        notificationService.readNotification(loginMember, notification.getId());

        //Then
        Notification result = notificationRepository.findAll().get(0);
        Assertions.assertTrue(result.getNotificationResponse().isRead());
    }

    @Test
    @DisplayName("알림 저장")
    void saveNotificationTest() {
        //Given
        NotificationRequest request = new NotificationRequest(BoardCategory.ALL, 1L, DummyGenerator.GIVEN_COMMENT_CONTENT, List.of(1L));

        //When
        notificationService.saveNotification(request);

        //Then
        List<Notification> result = notificationRepository.findAll();
        Assertions.assertEquals(result.size(), 1);
    }
}
