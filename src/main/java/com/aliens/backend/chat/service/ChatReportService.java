package com.aliens.backend.chat.service;

import com.aliens.backend.chat.controller.dto.request.ChatReportRequest;
import com.aliens.backend.chat.domain.ChatReport;
import com.aliens.backend.chat.domain.repository.ChatReportRepository;
import com.aliens.backend.global.success.ChatSuccessCode;
import org.springframework.stereotype.Service;

@Service
public class ChatReportService {

    private final ChatReportRepository chatReportRepository;

    public ChatReportService(ChatReportRepository chatReportRepository) {
        this.chatReportRepository = chatReportRepository;
    }

    public String reportPartner(Long memberId, ChatReportRequest chatReportRequest) {
        ChatReport chatReport = ChatReport.of(memberId, chatReportRequest);
        chatReportRepository.save(chatReport);
        return ChatSuccessCode.REPORT_SUCCESS.getMessage();
    }
}
