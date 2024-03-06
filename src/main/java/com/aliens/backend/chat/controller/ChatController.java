package com.aliens.backend.chat.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.chat.controller.dto.request.ReadRequest;
import com.aliens.backend.chat.controller.dto.response.ChatSummaryResponse;
import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.chat.service.ChatService;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.ChatSuccess;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
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

    @GetMapping("/chat/summaries")
    public SuccessResponse<ChatSummaryResponse> getChatSummaries(@Login LoginMember loginMember) {

        return SuccessResponse.of(
                ChatSuccess.GET_SUMMARIES_SUCCESS,
                chatService.getChatSummaries(loginMember)
        );
    }

    @GetMapping("/chat/room/{roomId}/messages")
    public SuccessResponse<List<Message>> getMessages(@PathVariable("roomId") Long chatRoomId,
                                       @RequestParam(value = "lastMessageId", required = false) String messageId) {

        return SuccessResponse.of(
                ChatSuccess.GET_MESSAGES_SUCCESS,
                chatService.getMessages(chatRoomId,messageId)
        );

    }
}
