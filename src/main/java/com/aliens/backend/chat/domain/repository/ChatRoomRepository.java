package com.aliens.backend.chat.domain.repository;

import com.aliens.backend.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    @Query("SELECT c FROM ChatRoom c JOIN c.participants p WHERE p.member.id = :memberId")
    List<ChatRoom> findByMemberId(Long memberId);
}