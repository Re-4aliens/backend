package com.aliens.backend.board.domain;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.board.controller.dto.request.ReportBoardRequest;
import jakarta.persistence.*;

@Entity
public class BoardReport {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reporting_member_id")
    private Member member;

    @Column
    private Long boardId;

    @Column
    private String reason;

    protected BoardReport() {
    }

    private BoardReport(Member member,
                        Long boardId,
                        String reason) {
        this.member = member;
        this.boardId = boardId;
        this.reason = reason;
    }

    public static BoardReport of(final Member member, final ReportBoardRequest request) {
        return new BoardReport(member,
                request.boardId(),
                request.reason());
    }
}

