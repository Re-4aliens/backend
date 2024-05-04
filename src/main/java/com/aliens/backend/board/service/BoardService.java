package com.aliens.backend.board.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.board.controller.dto.request.MarketChangeRequest;
import com.aliens.backend.board.controller.dto.request.BoardCreateRequest;
import com.aliens.backend.board.controller.dto.request.MarketBoardCreateRequest;
import com.aliens.backend.board.domain.Board;
import com.aliens.backend.board.domain.BoardImage;
import com.aliens.backend.board.domain.MarketInfo;
import com.aliens.backend.board.domain.repository.BoardRepository;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.BoardError;
import com.aliens.backend.global.response.error.MemberError;
import com.aliens.backend.uploader.AwsS3Uploader;
import com.aliens.backend.uploader.dto.S3File;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final AwsS3Uploader imageUploader;
    private final MemberRepository memberRepository;

    public BoardService(final BoardRepository boardRepository,
                        final AwsS3Uploader imageUploader,
                        final MemberRepository memberRepository) {
        this.boardRepository = boardRepository;
        this.imageUploader = imageUploader;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void postNormalBoard(final BoardCreateRequest request,
                          final List<MultipartFile> boardImages,
                          final LoginMember loginMember) {
        Member member = getMember(loginMember);
        checkWriterTooManyPost(member);

        Board board = Board.normalOf(request, member);
        if(notHasImages(boardImages)) {
            boardRepository.save(board);
            return;
        }

        List<BoardImage> boardImageEntitys = uploadS3(boardImages, board);
        board.setImages(boardImageEntitys);
        boardRepository.save(board);
    }

    private void checkWriterTooManyPost(final Member member) {
        Optional<Board> board = boardRepository.findFirstByMemberOrderById(member);
        if(!board.isEmpty() && board.get().isJustCreated()) {
            throw new RestApiException(BoardError.TOO_MANY_POST);
        }
    }

    private boolean notHasImages(final List<MultipartFile> boardImages) {
        return boardImages == null || boardImages.isEmpty();
    }

    private List<BoardImage> uploadS3(final List<MultipartFile> boardImages, final Board board) {
        List<BoardImage> boardImageEntities = new ArrayList<>();
        List<S3File> s3Files = imageUploader.multiUpload(boardImages);

        for (S3File imageFile : s3Files) {
            BoardImage boardImage = BoardImage.from(imageFile);
            boardImage.setBoard(board); // BoardImage 엔티티에 Board 연관 설정
            boardImageEntities.add(boardImage);
        }
        return boardImageEntities;
    }

    private Member getMember(final LoginMember loginMember) {
        return memberRepository.findById(loginMember.memberId()).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }

    @Transactional
    public void postMarketBoard(final MarketBoardCreateRequest marketRequest,
                                final List<MultipartFile> marketBoardImages,
                                final LoginMember loginMember) {
        Member member = getMember(loginMember);
        checkWriterTooManyPost(member);

        BoardCreateRequest boardRequest = BoardCreateRequest.from(marketRequest);

        MarketInfo marketInfo = MarketInfo.from(marketRequest);
        Board board = Board.marketOf(boardRequest, member, marketInfo);

        if(notHasImages(marketBoardImages)) {
            boardRepository.save(board);
            return;
        }

        List<BoardImage> boardImageEntitys = uploadS3(marketBoardImages, board);
        board.setImages(boardImageEntitys);
        boardRepository.save(board);
    }

    private Board getBoardById(final Long id) {
        return boardRepository.findById(id).orElseThrow(() -> new RestApiException(BoardError.INVALID_BOARD_ID));
    }

    @Transactional
    public void changeMarketBoard(final Long boardId, final MarketChangeRequest request, final LoginMember loginMember) {
        Board board = getBoardById(boardId);
        checkWriter(board, loginMember);
        board.changeByRequest(request);
    }

    private void checkWriter(final Board board, final LoginMember loginMember) {
        if (!board.isWriter(loginMember.memberId())) {
            throw new RestApiException(BoardError.NOT_WRITER);
        }
    }

    @Transactional
    public void deleteBoard(final LoginMember loginMember, final Long boardId) {
        Board board = getBoardById(boardId);
        checkWriter(board, loginMember);

        deleteImageOnS3(board);
        boardRepository.delete(board);
    }

    private void deleteImageOnS3(final Board board) {
        List<BoardImage> images = board.getImages();
        images.forEach(i -> imageUploader.delete(i.getName()));
    }
}
