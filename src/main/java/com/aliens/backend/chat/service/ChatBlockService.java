package com.aliens.backend.chat.service;

import com.aliens.backend.chat.controller.dto.request.ChatBlockRequest;
import com.aliens.backend.chat.domain.ChatBlock;
import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.domain.repository.ChatBlockRepository;
import com.aliens.backend.chat.domain.repository.ChatRoomRepository;
import com.aliens.backend.global.error.ChatError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.success.ChatSuccessCode;
import org.springframework.stereotype.Service;

@Service
public class ChatBlockService {
    private final ChatBlockRepository chatBlockRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatBlockService(ChatBlockRepository chatBlockRepository, ChatRoomRepository chatRoomRepository) {
        this.chatBlockRepository = chatBlockRepository;
        this.chatRoomRepository = chatRoomRepository;
    }

    public String blockPartner(Long memberId, ChatBlockRequest chatBlockRequest) {
        ChatBlock chatBlock = ChatBlock.of(memberId, chatBlockRequest);
        chatBlockRepository.save(chatBlock);
        blockChatRoom(chatBlockRequest.chatRoomId());
        return ChatSuccessCode.BLOCK_SUCCESS.getMessage();
    }

    private void blockChatRoom(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new RestApiException(ChatError.CHAT_ROOM_NOT_FOUND));
        chatRoom.block();
        chatRoomRepository.save(chatRoom);
    }
}