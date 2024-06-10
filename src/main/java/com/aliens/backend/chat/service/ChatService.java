package com.aliens.backend.chat.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.chat.controller.dto.event.ChatRoomBlockEvent;
import com.aliens.backend.chat.controller.dto.event.ChatRoomCreationEvent;
import com.aliens.backend.chat.controller.dto.request.MessageSendRequest;
import com.aliens.backend.chat.controller.dto.request.ReadRequest;
import com.aliens.backend.chat.controller.dto.response.ChatSummaryResponse;
import com.aliens.backend.chat.controller.dto.response.ReadResponse;
import com.aliens.backend.chat.domain.ChatParticipant;
import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.domain.Message;
import com.aliens.backend.chat.domain.repository.ChatRoomRepository;
import com.aliens.backend.chat.domain.repository.MessageRepository;
import com.aliens.backend.chat.service.model.ChatMessageSummary;
import com.aliens.backend.chat.service.model.MemberPair;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.ChatError;
import com.aliens.backend.global.response.error.MemberError;
import com.aliens.backend.global.response.success.ChatSuccess;
import com.aliens.backend.notification.domain.FcmTokenRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ChatService {
    private final MessageRepository messageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final FcmTokenRepository fcmTokenRepository;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ChatService(final MessageRepository messageRepository,
                       final SimpMessagingTemplate messagingTemplate,
                       final ChatRoomRepository chatRoomRepository,
                       final FcmTokenRepository fcmTokenRepository,
                       final MemberRepository memberRepository,
                       final ApplicationEventPublisher eventPublisher) {
        this.messageRepository = messageRepository;
        this.messagingTemplate = messagingTemplate;
        this.chatRoomRepository = chatRoomRepository;
        this.fcmTokenRepository = fcmTokenRepository;
        this.memberRepository = memberRepository;
        this.eventPublisher = eventPublisher;
    }

    public String sendMessage(MessageSendRequest messageSendRequest) {
        Message message = Message.of(messageSendRequest);
        Message savedMessage = saveMessage(message);
        publishMessage(savedMessage, messageSendRequest.roomId());
        sendNotification(savedMessage);
        return ChatSuccess.SEND_MESSAGE_SUCCESS.getMessage();
    }

    public String readMessages(ReadRequest readRequest) {
        updateReadState(readRequest.roomId(), readRequest.memberId());
        publishReadState(readRequest.roomId(), readRequest.memberId());
        return ChatSuccess.READ_MESSAGES_SUCCESS.getMessage();
    }

    public ChatSummaryResponse getChatSummaries(LoginMember loginMember) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByMemberId(loginMember.memberId());
        List<Long> chatRoomIds = chatRooms.stream().map(ChatRoom::getId).toList();
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

    private Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    private void publishMessage(Message message, Long ChatRoomId) {
        messagingTemplate.convertAndSend("/room/"+ ChatRoomId, message);
    }

    private void sendNotification(Message message) {
        var fcmMessage = createFcmMessage(message);
        eventPublisher.publishEvent(fcmMessage);
    }

    private com.google.firebase.messaging.Message createFcmMessage(Message message) {
        Member sender = memberRepository.findById(message.getSenderId()).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
        Member receiver = memberRepository.findById(message.getReceiverId()).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
        return com.google.firebase.messaging.Message.builder()
                .setNotification(com.google.firebase.messaging.Notification.builder()
                        .setTitle(sender.getProfileName())
                        .setBody(message.getContent())
                        .build())
                .setToken(findFcmTokenByMember(receiver))
                .build();
    }

    private String findFcmTokenByMember(Member receiver) {
        return fcmTokenRepository.findByMember(receiver).getToken();
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

    @EventListener
    public void handleChatRoomCreationEvent(ChatRoomCreationEvent event) {
        createChatRooms(event.matchedPairs());
    }

    private void createChatRooms(Set<MemberPair> matchedPairs) {
        List<ChatRoom> chatRooms = matchedPairs.stream()
                .map(this::createChatRoom)
                .toList();
        chatRoomRepository.saveAll(chatRooms);
    }

    private ChatRoom createChatRoom(MemberPair pair) {
        ChatParticipant participant1 = ChatParticipant.of(null, pair.first(), pair.second());
        ChatParticipant participant2 = ChatParticipant.of(null, pair.second(), pair.first());
        return new ChatRoom(participant1, participant2);
    }

    @EventListener
    public void handleChatRoomBlockEvent(ChatRoomBlockEvent event) {
        blockChatRoom(event.chatRoomId());
    }

    private void blockChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = findChatRoomsById(chatRoomId);
        chatRoom.block();
        chatRoomRepository.save(chatRoom);
    }

    private ChatRoom findChatRoomsById(final Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new RestApiException(ChatError.CHAT_ROOM_NOT_FOUND));
    }
}
