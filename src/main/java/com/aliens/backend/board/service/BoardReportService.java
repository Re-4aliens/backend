package com.aliens.backend.board.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.board.controller.dto.request.ReportBoardRequest;
import com.aliens.backend.board.domain.Board;
import com.aliens.backend.board.domain.BoardReport;
import com.aliens.backend.board.domain.repository.BoardReportRepository;
import com.aliens.backend.board.domain.repository.BoardRepository;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.BoardError;
import com.aliens.backend.global.response.error.MemberError;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardReportService {

    private final BoardReportRepository boardReportRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private static final int RESTRICTION_MAX_COUNT = 5;

    public BoardReportService(final BoardReportRepository boardReportRepository,
                              final BoardRepository boardRepository,
                              final MemberRepository memberRepository) {
        this.boardReportRepository = boardReportRepository;
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void report(final LoginMember loginMember, final ReportBoardRequest request) {
        Member member = getMember(loginMember);

        BoardReport boardReport = BoardReport.of(member, request);
        boardReportRepository.save(boardReport);

        if(boardReportRepository.countByBoardId(request.boardId()) >= RESTRICTION_MAX_COUNT) {
            Board board = getBoardById(request.boardId());
            boardRepository.delete(board);
        }
    }

    private Member getMember(final LoginMember loginMember) {
        return memberRepository.findById(loginMember.memberId()).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }

    private Board getBoardById(final Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new RestApiException(BoardError.INVALID_BOARD_ID));
    }

}
