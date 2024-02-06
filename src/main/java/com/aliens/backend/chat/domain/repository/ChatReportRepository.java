package com.aliens.backend.chat.domain.repository;

import com.aliens.backend.chat.domain.ChatReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatReportRepository extends JpaRepository<ChatReport, Long> {
}
