package com.aliens.backend.global.aspect.log.notification;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.global.response.log.InfoLogResponse;
import com.aliens.backend.global.response.success.NotificationSuccess;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Aspect
@Component
public class NotificationLogAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public NotificationLogAspect(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.notification.pointcut.NotificationPointcut.registerFcmToken() " +
            "&& args(loginMember, fcmToken)", argNames = "loginMember,fcmToken")
    public void logRegisterFcmToken(LoginMember loginMember, String fcmToken) throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("loginMember", loginMember);
        data.put("fcmToken", fcmToken);
        InfoLogResponse response = InfoLogResponse.from(NotificationSuccess.REGISTER_TOKEN_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.notification.pointcut.NotificationPointcut.getFcmStatus() " +
            "&& args(loginMember)")
    public void logGetFcmStatus(LoginMember loginMember) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(NotificationSuccess.GET_FCM_STATUS_SUCCESS, loginMember);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.notification.pointcut.NotificationPointcut.changeAcceptation() " +
            "&& args(loginMember, decision)", argNames = "loginMember,decision")
    public void logChangeAcceptation(LoginMember loginMember, Boolean decision) throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("loginMember", loginMember);
        data.put("decision", decision);
        InfoLogResponse response = InfoLogResponse.from(NotificationSuccess.CHANGE_FCM_STATUS_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.notification.pointcut.NotificationPointcut.getNotifications() " +
            "&& args(loginMember)")
    public void logGetNotifications(LoginMember loginMember) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(NotificationSuccess.GET_NOTIFICATION_SUCCESS, loginMember);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.notification.pointcut.NotificationPointcut.readNotification() " +
            "&& args(loginMember, notificationId)", argNames = "loginMember,notificationId")
    public void logReadNotification(LoginMember loginMember, Long notificationId) throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("loginMember", loginMember);
        data.put("notificationId", notificationId);
        InfoLogResponse response = InfoLogResponse.from(NotificationSuccess.READ_NOTIFICATION_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }
}
