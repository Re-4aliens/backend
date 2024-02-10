package com.aliens.backend.chat.service;

import com.aliens.backend.chat.controller.dto.request.ChatBlockRequest;
import com.aliens.backend.chat.domain.ChatBlock;
import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.domain.repository.ChatBlockRepository;
import com.aliens.backend.chat.domain.repository.ChatRoomRepository;
import com.aliens.backend.global.success.ChatSuccessCode;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatBlockService {
    private final ChatBlockRepository chatBlockRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatBlockService(ChatBlockRepository chatBlockRepository, ChatRoomRepository chatRoomRepository) {
        this.chatBlockRepository = chatBlockRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    @Transactional
    public String blockPartner(Long memberId, ChatBlockRequest chatBlockRequest) {
        ChatBlock chatBlock = ChatBlock.of(memberId, chatBlockRequest);
        chatBlockRepository.save(chatBlock);
        blockChatRoom(chatBlockRequest.chatRoomId());
        return ChatSuccessCode.BLOCK_SUCCESS.getMessage();
    }

    private void blockChatRoom(Long chatRoomId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByRoomId(chatRoomId);
        chatRooms.forEach(ChatRoom::block);
        chatRoomRepository.saveAll(chatRooms);
    }
}