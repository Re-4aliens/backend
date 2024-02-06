package com.aliens.backend.chat.controller;

import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.chat.controller.dto.request.ReadRequest;
import com.aliens.backend.chat.controller.dto.response.ChatSummaryResponse;
import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.chat.service.ChatService;
import com.aliens.backend.global.config.resolver.Login;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Map;


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

    @GetMapping("chat/summaries")
    public ResponseEntity<Map<String, ChatSummaryResponse>> getChatSummaries(@Login Long memberId) {
        ChatSummaryResponse chatSummaryResponse = chatService.getChatSummaries(memberId);
        Map<String, ChatSummaryResponse> response = Collections.singletonMap("response", chatSummaryResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/chat/room/{roomId}/messages")
    public ResponseEntity<Map<String, List>> getMessages(@PathVariable("roomId") Long chatRoomId,
                                                         @RequestParam(value = "lastMessageId", required = false) String lastMessageId) {
        List<Message> messages = chatService.getMessages(chatRoomId,lastMessageId);
        Map<String, List> response = Collections.singletonMap("response", messages);
        return ResponseEntity.ok(response);
    }
}
