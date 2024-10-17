package com.aliens.backend.board.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.domain.Board;
import com.aliens.backend.board.domain.Great;
import com.aliens.backend.board.domain.repository.BoardRepository;
import com.aliens.backend.board.domain.repository.GreatRepository;
import com.aliens.backend.board.domain.repository.custom.GreatCustomRepository;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.BoardError;
import com.aliens.backend.global.response.error.MemberError;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GreatService {
    private final GreatRepository greatRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final GreatCustomRepository greatCustomRepository;

    public GreatService(final GreatRepository greatRepository,
                        final MemberRepository memberRepository,
                        final BoardRepository boardRepository,
                        final GreatCustomRepository greatCustomRepository) {
        this.greatRepository = greatRepository;
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
        this.greatCustomRepository = greatCustomRepository;
    }

    @Transactional
    public void greatAtBoard(final Long id, final LoginMember loginMember) {
        Board board = findBoardById(id);
        Member member = getMember(loginMember);

        if (isAlreadyExits(board, member)) return;

        Great great = Great.of(board, member);
        board.addGreat(great);
        greatRepository.save(great);
    }

    private boolean isAlreadyExits(final Board board, final Member member) {
        if(greatRepository.existsByMemberAndBoard(member, board)) {
            greatRepository.deleteGreatByMemberAndBoard(member, board);
            board.minusGreatCount();
            return true;
        }
        return false;
    }

    private Member getMember(final LoginMember loginMember) {
        return memberRepository.findById(loginMember.memberId()).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }

    private Board findBoardById(final Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new RestApiException(BoardError.INVALID_BOARD_ID));
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> getGreatBoardPage(final LoginMember loginMember, Pageable pageable) {
        return greatCustomRepository.getGreatBoardPage(loginMember.memberId(), pageable);
    }
}
