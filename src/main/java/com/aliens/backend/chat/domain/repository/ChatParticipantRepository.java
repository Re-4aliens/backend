package com.aliens.backend.chat.domain.repository;

import com.aliens.backend.chat.domain.ChatParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatParticipant, Long> {

    @Query("SELECT cp FROM ChatParticipant cp WHERE cp.member.id = :memberId AND cp.partner.id = :matchedMemberId")
    Optional<ChatParticipant> findByMemberIdAndMatchedMemberId(Long memberId, Long matchedMemberId);
}