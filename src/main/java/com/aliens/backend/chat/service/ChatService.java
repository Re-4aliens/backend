package com.aliens.backend.chat.service;

import com.aliens.backend.chat.controller.dto.request.ReadRequest;
import com.aliens.backend.chat.controller.dto.response.ChatSummary;
import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.chat.controller.dto.response.ReadResponse;
import com.aliens.backend.chat.domain.ChatRepository.MessageRepository;
import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.global.property.WebSocketProperties;
import com.aliens.backend.global.success.ChatSuccessCode;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketProperties webSocketProperties;

    public ChatService(MessageRepository messageRepository, SimpMessagingTemplate messagingTemplate, WebSocketProperties webSocketProperties) {
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
        this.webSocketProperties = webSocketProperties;
    }

    public String sendMessage(MessageSendRequest messageSendRequest) {
        Message message = Message.of(messageSendRequest);
        saveMessage(message);
        publishMessage(message,messageSendRequest.roomId());
        sendNotification(message);
        return ChatSuccessCode.SEND_MESSAGE_SUCCESS.getMessage();
    }

    public String readMessages(ReadRequest readRequest) {
        updateReadState(readRequest.roomId(), readRequest.memberId());
        publishReadState(readRequest.roomId(), readRequest.memberId());
        return ChatSuccessCode.READ_MESSAGES_SUCCESS.getMessage();
    }

    public List<ChatSummary> getChatSummary() {
        List<ChatSummary> chatSummarys = new ArrayList<>();
        return chatSummarys;
    }

    public List<Message> getMessages() {
        List<Message> messages = new ArrayList<>();
        return messages;
    }

    private void saveMessage(Message message) {
        messageRepository.save(message);
    }

    private void publishMessage(Message message, Long ChatRoomId) {
        messagingTemplate.convertAndSend(webSocketProperties.getTopic()+"/"+ChatRoomId, message);
    }

    private void sendNotification(Message message) {
    }

    private void updateReadState(Long chatRoomId, Long readBy) {
        messageRepository.markMessagesAsRead(chatRoomId, readBy);
    }

    private void publishReadState(Long chatRoomId, Long memberId) {
        ReadResponse readResponse = new ReadResponse(memberId);
        messagingTemplate.convertAndSend(webSocketProperties.getTopic()+"/"+chatRoomId, readResponse);
    }
}
