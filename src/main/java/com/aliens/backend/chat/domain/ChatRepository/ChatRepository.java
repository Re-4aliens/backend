package com.aliens.backend.chat.domain.ChatRepository;

import com.aliens.backend.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findByMemberId(Long memberId);
}