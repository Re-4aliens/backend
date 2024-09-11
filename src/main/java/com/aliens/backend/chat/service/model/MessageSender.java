package com.aliens.backend.chat.service.model;

import com.aliens.backend.chat.controller.dto.request.ReadRequest;
import com.aliens.backend.chat.controller.dto.response.ReadResponse;
import com.aliens.backend.chat.domain.Message;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageSender {
    private final SimpMessagingTemplate messagingTemplate;

    public MessageSender(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void send(Message message) {
        messagingTemplate.convertAndSend("/room/"+ message.getRoomId(), message);
    }

    public void sendReadResponse(ReadRequest request) {
        ReadResponse readResponse = new ReadResponse(request.memberId());
        messagingTemplate.convertAndSend("/room/" + request.roomId(), readResponse);
    }
}
