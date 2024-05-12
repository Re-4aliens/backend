package com.aliens.backend.board.domain;

import com.aliens.backend.auth.domain.Member;
import jakarta.persistence.*;

@Entity
public class Great {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "greatId")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private Board board;

    protected Great() {
    }

    public static Great of(final Board board,
                           final Member member) {
        Great great = new Great();
        great.board = board;
        great.member = member;
        return great;
    }
}
