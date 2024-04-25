package com.aliens.backend.board.domain;

import com.aliens.backend.auth.domain.Member;
import jakarta.persistence.*;

@Entity
public class Great {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
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
