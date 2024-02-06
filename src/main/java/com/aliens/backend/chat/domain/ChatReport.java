package com.aliens.backend.chat.domain;

import com.aliens.backend.chat.controller.dto.request.ChatReportRequest;
import com.aliens.backend.global.error.ChatError;
import com.aliens.backend.global.exception.RestApiException;
import jakarta.persistence.*;

@Entity
@Table(name = "CHAT_REPORT")
public class ChatReport {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column
    private Long id;
    @Column(name = "reported_member_id")
    private Long reportedMemberId;
    @Column(name = "reporting_member_id")
    private Long reportingMemberId;
    @Column
    private ChatReportCategory category;
    @Column
    private String content;

    protected ChatReport() {
    }

    public static ChatReport of(Long reportingMemberId, ChatReportRequest chatReportRequest) {
        ChatReport chatReport = new ChatReport();
        chatReport.reportedMemberId = chatReportRequest.partnerId();
        chatReport.reportingMemberId = reportingMemberId;
        chatReport.category = ChatReportCategory.fromString(chatReportRequest.category())
                .orElseThrow(() -> new RestApiException(ChatError.INVALID_REPORT_CATEGORY));
        chatReport.content = chatReportRequest.content();
        return chatReport;
    }
}
