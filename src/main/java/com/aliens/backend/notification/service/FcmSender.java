package com.aliens.backend.notification.service;

import com.aliens.backend.global.response.error.CommonError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.log.InfoLogResponse;
import com.aliens.backend.global.response.success.NotificationSuccess;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FcmSender {
    private final ObjectMapper objectMapper;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public FcmSender(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @EventListener
    public void listenSingleMessageRequest(Message message) {
        sendSingleMessage(message);
    }

    @EventListener
    public void listenMultiMessageRequest(MulticastMessage message) {
        sendMultiMessage(message);
    }

    private void sendSingleMessage(Message message) {
        try {
            FirebaseMessaging.getInstance().sendAsync(message);
            InfoLogResponse response = InfoLogResponse.from(NotificationSuccess.SEND_SINGLE_NOTIFICATION_SUCCESS);
            log.info(objectMapper.writeValueAsString(response));
        } catch (Exception e) {
            InfoLogResponse response = InfoLogResponse.from(CommonError.FCM_MESSAGING_ERROR);
            try {
                log.error(objectMapper.writeValueAsString(response));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
            throw new RestApiException(CommonError.FCM_MESSAGING_ERROR);
        }
    }

    private void sendMultiMessage(MulticastMessage message) {
        try {
            FirebaseMessaging.getInstance().sendMulticastAsync(message);
            InfoLogResponse response = InfoLogResponse.from(NotificationSuccess.SEND_MULTI_NOTIFICATION_SUCCESS);
            log.info(objectMapper.writeValueAsString(response));
        } catch (Exception e) {
            InfoLogResponse response = InfoLogResponse.from(CommonError.FCM_MESSAGING_ERROR);
            try {
                log.error(objectMapper.writeValueAsString(response));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
            throw new RestApiException(CommonError.FCM_MESSAGING_ERROR);
        }
    }
}
