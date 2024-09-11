package com.aliens.backend.global.config.interceptor;

import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.chat.controller.dto.request.ReadRequest;
import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.service.model.ChatAuthValidator;
import com.aliens.backend.global.property.WebSocketProperties;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatChannelInterceptor implements ChannelInterceptor {

    private final ChatAuthValidator chatAuthValidator;
    private final WebSocketProperties properties;
    private MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();

    public ChatChannelInterceptor(ChatAuthValidator chatAuthValidator, WebSocketProperties properties) {
        this.chatAuthValidator = chatAuthValidator;
        this.properties = properties;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        List<ChatRoom> chatRooms = (List<ChatRoom>) accessor.getSessionAttributes().get("chatRooms");
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            chatAuthValidator.validateRoomFromTopic(accessor.getDestination(), chatRooms);
        }
        if(StompCommand.SEND.equals(accessor.getCommand())){
            if(accessor.getDestination().equals(properties.getAppDestinationPrefix() +"/send")){
                MessageSendRequest messageSendRequest = (MessageSendRequest) messageConverter.fromMessage(message, MessageSendRequest.class);
                chatAuthValidator.validateRoom(messageSendRequest.roomId(), chatRooms);
            }
            if(accessor.getDestination().equals(properties.getAppDestinationPrefix() +"/read")){
                ReadRequest readRequest = (ReadRequest) messageConverter.fromMessage(message, ReadRequest.class);
                chatAuthValidator.validateRoom(readRequest.roomId(), chatRooms);
            }
        }
        return message;
    }
}