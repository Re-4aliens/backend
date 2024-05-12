package com.aliens.backend.chat.domain;

import com.aliens.backend.auth.domain.Member;
import jakarta.persistence.*;

@Entity
public class ChatReport {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "chatReportId")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reportingMemberId")
    private Member reportingMember;

    @ManyToOne
    @JoinColumn(name = "reportedMemberId")
    private Member reportedMember;

    @Column
    private ChatReportCategory category;
    @Column
    private String content;

    protected ChatReport() {
    }

    private ChatReport(Member reportingMember,
                       Member reportedMember,
                       ChatReportCategory category,
                       String content) {
        this.reportingMember = reportingMember;
        this.reportedMember = reportedMember;
        this.category = category;
        this.content = content;
    }

    public static ChatReport of(Member reportingMember,
                                Member reportedMember,
                                ChatReportCategory category,
                                String content) {
        return new ChatReport(reportingMember, reportedMember, category, content);
    }
}
