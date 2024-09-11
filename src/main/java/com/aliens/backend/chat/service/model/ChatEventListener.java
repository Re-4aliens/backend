package com.aliens.backend.chat.service.model;

import com.aliens.backend.chat.controller.dto.event.ChatRoomBlockEvent;
import com.aliens.backend.chat.controller.dto.event.ChatRoomCreationEvent;
import com.aliens.backend.chat.controller.dto.event.ChatRoomExpireEvent;
import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.domain.repository.ChatParticipantRepository;
import com.aliens.backend.chat.domain.repository.ChatRoomRepository;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.ChatError;
import jakarta.transaction.Transactional;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ChatEventListener {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;

    public ChatEventListener(ChatRoomRepository chatRoomRepository,
                             ChatParticipantRepository chatParticipantRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatParticipantRepository = chatParticipantRepository;
    }

    @Scheduled(cron = "${matching.round.update-date}")
    @Transactional
    public void openWaitingChatRooms() {
        chatRoomRepository.openWaitingChatRooms();
    }

    @EventListener
    @Transactional
    public void handleChatRoomExpireEvent(ChatRoomExpireEvent chatRoomExpireEvent) {
        chatRoomRepository.expireAllChatRooms();
        chatParticipantRepository.deleteAll();
    }

    @EventListener
    public void handleChatRoomBlockEvent(ChatRoomBlockEvent event) {
        ChatRoom chatRoom = findChatRoomsById(event.chatRoomId());
        chatRoom.block();
        chatRoomRepository.save(chatRoom);
    }

    private ChatRoom findChatRoomsById(final Long id) {
        return chatRoomRepository.findById(id).orElseThrow(() -> new RestApiException(ChatError.CHAT_ROOM_NOT_FOUND));
    }

    @EventListener
    public void handleChatRoomCreationEvent(ChatRoomCreationEvent event) {
        List<ChatRoom> chatRooms = event.matchedPairs().stream()
                .map(ChatRoom::createChatRoom)
                .toList();
        chatRoomRepository.saveAll(chatRooms);
    }
}
