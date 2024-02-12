package com.aliens.backend.chat.domain;

import com.aliens.backend.auth.domain.Member;
import jakarta.persistence.*;

@Entity
public class ChatReport {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporting_member_id")
    private Member reportingMember;

    @ManyToOne
    @JoinColumn(name = "reported_member_id")
    private Member reportedMember;

    @Column
    private ChatReportCategory category;
    @Column
    private String content;

    protected ChatReport() {
    }

    public static ChatReport of(Member reportingMember,
                                Member reportedMember,
                                ChatReportCategory category,
                                String content) {
        ChatReport chatReport = new ChatReport();
        chatReport.reportingMember = reportingMember;
        chatReport.reportedMember = reportedMember;
        chatReport.category = category;
        chatReport.content = content;
        return chatReport;
    }
}
