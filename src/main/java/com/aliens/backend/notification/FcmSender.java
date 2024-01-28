package com.aliens.backend.notification;

import com.aliens.backend.global.error.CommonError;
import com.aliens.backend.global.exception.RestApiException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FcmSender {

    @EventListener
    public void listenSingleMessageRequest(Message message) {
        sendSingleMessage(message);
    }

    @EventListener
    public void listenMultiMessageRequest(MulticastMessage message) {
        sendMultiMessage(message);
    }

    private void sendSingleMessage(Message message) {
        FirebaseMessaging.getInstance().sendAsync(message);
    }

    private void sendMultiMessage(MulticastMessage message) {
        try {
            FirebaseMessaging.getInstance().sendMulticastAsync(message);
        } catch (Exception e) {
            throw new RestApiException(CommonError.FCM_MESSAGING_ERROR);
        }
    }
}
