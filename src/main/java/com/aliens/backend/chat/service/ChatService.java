package com.aliens.backend.chat.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.chat.controller.dto.request.ReadRequest;
import com.aliens.backend.chat.controller.dto.response.ChatSummaryResponse;
import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.chat.domain.repository.ChatRoomRepository;
import com.aliens.backend.chat.domain.repository.MessageRepository;
import com.aliens.backend.chat.domain.model.ChatMessageSummary;
import com.aliens.backend.chat.service.model.MessageSender;
import com.aliens.backend.global.response.success.ChatSuccess;
import com.aliens.backend.notification.service.NotificationSender;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private final MessageSender messageSender;
    private final NotificationSender notificationSender;
    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatService(final MessageSender messageSender,
                       final NotificationSender notificationSender,
                       final MessageRepository messageRepository,
                       final ChatRoomRepository chatRoomRepository) {
        this.messageSender = messageSender;
        this.notificationSender = notificationSender;
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    @Transactional
    public String sendMessage(MessageSendRequest request) {
        Message savedMessage = saveMessage(request);
        messageSender.send(savedMessage);
        notificationSender.send(savedMessage);
        return ChatSuccess.SEND_MESSAGE_SUCCESS.getMessage();
    }

    private Message saveMessage(MessageSendRequest request) {
        Message message = Message.from(request);
        return messageRepository.save(message);
    }

    @Transactional
    public String readMessages(ReadRequest request) {
        messageRepository.markMessagesAsRead(request.roomId(), request.memberId());
        messageSender.sendReadResponse(request);
        return ChatSuccess.READ_MESSAGES_SUCCESS.getMessage();
    }

    public ChatSummaryResponse getChatSummaries(LoginMember loginMember) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByMemberId(loginMember.memberId());
        List<Long> chatRoomIds = chatRooms.stream().map(ChatRoom::getId).toList();
        List<ChatMessageSummary> chatMessageSummaries = messageRepository.aggregateMessageSummaries(chatRoomIds, loginMember.memberId());
        return new ChatSummaryResponse(chatRooms, chatMessageSummaries);
    }

    public List<Message> getMessages(Long chatRoomId, String lastMessageId) {
        return messageRepository.findMessages(chatRoomId,lastMessageId);
    }

    public List<ChatRoom> getChatRooms(Long memberId) {
        return chatRoomRepository.findByMemberId(memberId);
    }
}