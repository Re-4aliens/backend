package com.aliens.backend.chat.controller;

import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.chat.controller.dto.request.ReadRequest;
import com.aliens.backend.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;


@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/send")
    public void sendMessage(@Payload MessageSendRequest messageSendRequest) {
        chatService.sendMessage(messageSendRequest);
    }

    @MessageMapping("/read")
    public void readMessage(@Payload ReadRequest readRequest) {
        chatService.readMessages(readRequest);
    }

    @MessageMapping("/summary")
    public void getChatSummary() {
        chatService.getChatSummary();
    }
}
