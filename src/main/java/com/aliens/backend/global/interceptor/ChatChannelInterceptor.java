package com.aliens.backend.global.interceptor;

import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.service.ChatAuthValidator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class ChatChannelInterceptor implements ChannelInterceptor {

    ChatAuthValidator chatAuthValidator;

    public ChatChannelInterceptor(ChatAuthValidator chatAuthValidator) {
        this.chatAuthValidator = chatAuthValidator;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        ChatRoom chatRoom = (ChatRoom) accessor.getSessionAttributes().get("chatRooms");
        if (accessor != null && StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
        }
        return message;
    }
}