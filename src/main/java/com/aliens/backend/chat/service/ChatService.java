package com.aliens.backend.chat.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.chat.controller.dto.request.ReadRequest;
import com.aliens.backend.chat.controller.dto.response.ChatSummaryResponse;
import com.aliens.backend.chat.controller.dto.response.ReadResponse;
import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.chat.domain.repository.ChatRoomRepository;
import com.aliens.backend.chat.domain.repository.MessageRepository;
import com.aliens.backend.chat.service.model.ChatMessageSummary;
import com.aliens.backend.global.response.success.ChatSuccess;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;

    public ChatService(MessageRepository messageRepository, ChatRoomRepository chatRoomRepository, SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public String sendMessage(MessageSendRequest messageSendRequest) {
        Message message = Message.of(messageSendRequest);
        saveMessage(message);
        publishMessage(message, messageSendRequest.roomId());
        sendNotification(message);
        return ChatSuccess.SEND_MESSAGE_SUCCESS.getMessage();
    }

    public String readMessages(ReadRequest readRequest) {
        updateReadState(readRequest.roomId(), readRequest.memberId());
        publishReadState(readRequest.roomId(), readRequest.memberId());
        return ChatSuccess.READ_MESSAGES_SUCCESS.getMessage();
    }

    public ChatSummaryResponse getChatSummaries(LoginMember loginMember) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByMemberId(loginMember.memberId());
        List<Long> chatRoomIds = chatRooms.stream().map(ChatRoom::getRoomId).toList();
        List<ChatMessageSummary> chatMessageSummaries = messageRepository.aggregateMessageSummaries(chatRoomIds, loginMember.memberId());
        ChatSummaryResponse chatSummaryResponse = new ChatSummaryResponse(chatRooms, chatMessageSummaries);
        return chatSummaryResponse;
    }

    public List<Message> getMessages(Long chatRoomId, String lastMessageId) {
        List<Message> messages = findMessages(chatRoomId, lastMessageId);
        return messages;
    }

    public List<ChatRoom> getChatRooms(Long memberId) {
        return chatRoomRepository.findByMemberId(memberId);
    }

    private void saveMessage(Message message) {
        messageRepository.save(message);
    }

    private void publishMessage(Message message, Long ChatRoomId) {
        messagingTemplate.convertAndSend("/room/"+ ChatRoomId, message);
    }

    private void sendNotification(Message message) {
    }

    private void updateReadState(Long chatRoomId, Long readBy) {
        messageRepository.markMessagesAsRead(chatRoomId, readBy);
    }

    private void publishReadState(Long chatRoomId, Long memberId) {
        ReadResponse readResponse = new ReadResponse(memberId);
        messagingTemplate.convertAndSend("/room/" + chatRoomId, readResponse);
    }

    private List<Message> findMessages(Long chatRoomId, String lastMessageId) {
        return messageRepository.findMessages(chatRoomId,lastMessageId);
    }
}
