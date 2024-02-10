package com.aliens.backend.chat.domain.repository;

import com.aliens.backend.chat.domain.ChatBlock;
import com.aliens.backend.chat.domain.ChatReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatBlockRepository extends JpaRepository<ChatBlock, Long> {
}
