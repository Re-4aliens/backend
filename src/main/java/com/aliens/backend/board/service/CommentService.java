package com.aliens.backend.board.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.board.controller.dto.response.CommentResponse;
import com.aliens.backend.board.controller.dto.request.ParentCommentCreateRequest;
import com.aliens.backend.board.controller.dto.request.ChildCommentCreateRequest;
import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.domain.Board;
import com.aliens.backend.board.domain.Comment;
import com.aliens.backend.board.domain.repository.BoardRepository;
import com.aliens.backend.board.domain.repository.CommentRepository;
import com.aliens.backend.board.domain.repository.custom.CommentCustomRepository;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.BoardError;
import com.aliens.backend.global.response.error.MemberError;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentCustomRepository commentCustomRepository;
    private final CommentEventPublisher eventPublisher;


    public CommentService(final CommentRepository commentRepository,
                          final MemberRepository memberRepository,
                          final BoardRepository boardRepository,
                          final CommentCustomRepository commentCustomRepository,
                          final CommentEventPublisher eventPublisher) {
        this.commentRepository = commentRepository;
        this.memberRepository = memberRepository;
        this.boardRepository = boardRepository;
        this.commentCustomRepository = commentCustomRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void postParentComment(final ParentCommentCreateRequest request,
                                  final LoginMember loginMember) {
        Member member = getMember(loginMember);
        Board board = findBoard(request.boardId());

        Comment comment = Comment.parentOf(request.content(), board, member);
        board.addComment(comment);

        commentRepository.save(comment);

        if(!comment.isWriter(board.getWriterId())) {
            eventPublisher.sendParentCommentNotification(board, request);
        }
    }

    private Board findBoard(final Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new RestApiException(BoardError.INVALID_BOARD_ID));
    }

    private Member getMember(final LoginMember loginMember) {
        return memberRepository.findById(loginMember.memberId()).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }

    @Transactional
    public void postChildComment(final ChildCommentCreateRequest request,
                                 final LoginMember loginMember) {
        Member member = getMember(loginMember);
        Board board = findBoard(request.boardId());
        Comment parentComment = findComment(request);

        Comment childComment = Comment.childOf(request, board, member);
        board.addComment(childComment);

        commentRepository.save(childComment);

        if(!parentComment.isWriter(member.getId())) {
            eventPublisher.sendChildCommentNotification(board, request, parentComment.getWriterId());
        }
    }

    private Comment findComment(ChildCommentCreateRequest request) {
        return commentRepository.findById(request.parentCommentId()).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> getCommentedBoardPage(final LoginMember loginMember, final Pageable pageable) {
        return commentCustomRepository.getCommentedBoardPage(loginMember.memberId(), pageable);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByBoardId(final Long boardId) {
        List<Comment> comments = commentCustomRepository.getCommentsByBoardId(boardId);

        ArrayList<CommentResponse> result = new ArrayList<>();
        sortComments(comments, result);

        return result;
    }

    private void sortComments(List<Comment> comments, ArrayList<CommentResponse> result) {
        for(Comment comment : comments) {
            if (!comment.isParent()){
                continue;
            }

            CommentResponse parentComment = comment.getCommentResponse();

            List<CommentResponse> childrenComment = comments.stream()
                    .filter(c -> (c.isChildFrom(comment.getId())))
                    .map(Comment::getCommentResponse)
                    .toList();

            if(isNotEmptyChildren(childrenComment)) {
                parentComment.setChildren(childrenComment);
            }
            result.add(parentComment);
        }
    }

    private boolean isNotEmptyChildren(final List<CommentResponse> childrenComment) {
        return childrenComment != null && !childrenComment.isEmpty();
    }

    @Transactional
    public void deleteComment(final LoginMember loginMember, final Long commentId) {
        Comment comment = getComment(commentId);
        if(!comment.isWriter(loginMember.memberId())) {
            throw new RestApiException(BoardError.INVALID_COMMENT_WRITER);
        }

        comment.deleteComment();
    }

    private Comment getComment(final Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new RestApiException(BoardError.INVALID_COMMENT_ID));
    }
}
