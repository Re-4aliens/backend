package com.aliens.backend.notification.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.NotificationSuccess;
import com.aliens.backend.notification.service.NotificationService;
import com.aliens.backend.notification.controller.dto.NotificationResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/notifications")
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/fcm")
    public SuccessResponse<String> registerFcmToken(@Login LoginMember loginMember,
                                            @RequestBody String fcmToken ) {

        notificationService.registerFcmToken(loginMember, fcmToken);

        return SuccessResponse.of(
                NotificationSuccess.REGISTER_TOKEN_SUCCESS
        );
    }

    @GetMapping()
    public SuccessResponse<List<NotificationResponse>> getNotifications(@Login LoginMember loginMember) {

        return SuccessResponse.of(
                NotificationSuccess.GET_NOTIFICATION_SUCCESS,
                notificationService.getNotifications(loginMember)
        );
    }

    @PatchMapping()
    public SuccessResponse<String> readNotification(@Login LoginMember loginMember,
                                                    @RequestParam("id") final Long notificationId) {

        notificationService.readNotification(loginMember, notificationId);

        return SuccessResponse.of(
                NotificationSuccess.READ_NOTIFICATION_SUCCESS
        );
    }
}
