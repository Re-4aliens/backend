package com.aliens.backend.chat.controller;

import com.aliens.backend.chat.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;


@Controller
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/send")
    public void sendMessage() {
        chatService.sendMessage();
    }

    @MessageMapping("/read")
    public void readMessage() {
        chatService.readMessage();
    }

    @MessageMapping("/summary")
    public void getChatSummary() {
        chatService.getChatSummary();
    }

}
