package com.aliens.backend.board.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.board.controller.dto.response.CommentResponse;
import com.aliens.backend.board.controller.dto.request.ParentCommentCreateRequest;
import com.aliens.backend.board.controller.dto.request.ChildCommentCreateRequest;
import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.domain.Board;
import com.aliens.backend.board.domain.Comment;
import com.aliens.backend.board.domain.enums.BoardCategory;
import com.aliens.backend.board.domain.enums.CommentStatus;
import com.aliens.backend.board.domain.repository.BoardRepository;
import com.aliens.backend.board.domain.repository.CommentRepository;
import com.aliens.backend.global.BaseIntegrationTest;
import com.aliens.backend.global.DummyGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class CommentServiceTest extends BaseIntegrationTest {
    @Autowired
    CommentService commentService;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    DummyGenerator dummyGenerator;

    Member givenMember;
    LoginMember givenLoginMember;


    @BeforeEach
    void setUp() {
        givenMember = dummyGenerator.generateSingleMember();
        givenLoginMember = givenMember.getLoginMember();
    }

    @Test
    @DisplayName("부모 댓글 등록")
    void postParentCommentTest() {
        //Given
        Board givenBoard = dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.FREE);
        String givenContent = "댓글 내용";
        ParentCommentCreateRequest request = new ParentCommentCreateRequest(givenBoard.getId(), givenContent);

        //When
        commentService.postParentComment(request, givenLoginMember);

        //Then
        Comment response = commentRepository.findAll().get(1);
        Assertions.assertEquals(givenContent, response.getContent());
    }

    @Test
    @DisplayName("자식 댓글 등록")
    void postChildCommentTest() {
        //Given
        Board givenBoard = dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.FREE);

        String givenParentContent = "부모 댓글 내용";
        Comment parentComment = Comment.parentOf(givenParentContent, givenBoard, givenMember);
        commentRepository.save(parentComment);

        String givenChildContent = "자식 댓글 내용";
        ChildCommentCreateRequest request = new ChildCommentCreateRequest(
                givenBoard.getId(),
                givenChildContent,
                parentComment.getId());

        //When
        commentService.postChildComment(request, givenLoginMember);

        //Then
        Comment response = commentRepository.findAll().get(2);
        Assertions.assertEquals(givenChildContent, response.getContent());
    }

    @Test
    @DisplayName("본인이 댓글 단 게시글 조회")
    void getCommentedBoardPageTest() {
        //Given
        Pageable givenPageable = PageRequest.of(0, 10);
        for (int i = 0; i < 3; i++) {
            dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.FREE);
        }

        //When
        List<BoardResponse> response = commentService.getCommentedBoardPage(givenLoginMember, givenPageable);

        //Then
        assertAll(
                () -> assertThat(response).hasSize(3),
                () -> assertThat(response).extracting("id").containsAnyElementsOf(List.of(1L, 2L, 3L)),
                () -> assertThat(response).extracting("title").contains(DummyGenerator.GIVEN_BOARD_TITLE),
                () -> assertThat(response).extracting("content").contains(DummyGenerator.GIVEN_BOARD_CONTENT),
                () -> assertThat(response).extracting("greatCount").containsOnly(1L),
                () -> assertThat(response).extracting("commentCount").containsOnly(1L)
        );
    }

    @Test
    @DisplayName("주어진 게시글의 댓글 조회 - 자식 댓글 없음")
    void getCommentsByBoardIdTest() {
        //Given
        dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.FREE);

        //When
        List<CommentResponse> response = commentService.getCommentsByBoardId(1L);

        //Then
        assertThat(response).hasSize(1);
        assertThat(response).extracting("status").containsOnly(CommentStatus.ACTIVE);
        assertThat(response).extracting("id").containsOnly(1L);
        assertThat(response).extracting("content").contains(DummyGenerator.GIVEN_COMMENT_CONTENT);
        assertThat(response).extracting("createdAt").isNotNull();
        assertThat(response).extracting("children").containsNull();
        assertThat(response).extracting("memberProfileDto").containsOnly(givenMember.getprofileDto());
    }

    @Test
    @DisplayName("주어진 게시글의 댓글 조회 - 자식 댓글 포함")
    void getCommentsWithChildrenByBoardIdTest() {
        //Given
        Board givenBoard = dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.FREE);

        String givenChildContent = "자식 댓글 내용";
        ChildCommentCreateRequest request = new ChildCommentCreateRequest(givenBoard.getId(),
                givenChildContent,
                1L);
        Comment comment = Comment.childOf(request, givenBoard, givenMember);

        givenBoard.addComment(comment);
        boardRepository.save(givenBoard);
        commentRepository.save(comment);

        //When
        List<CommentResponse> response = commentService.getCommentsByBoardId(1L);

        //Then
        assertThat(response).hasSize(1);
        assertThat(response).extracting("status").containsOnly(CommentStatus.ACTIVE);
        assertThat(response).extracting("id").containsOnly(1L);
        assertThat(response).extracting("content").contains(DummyGenerator.GIVEN_COMMENT_CONTENT);
        assertThat(response).extracting("createdAt").isNotNull();
        assertThat(response).extracting("children").hasSize(1);
        assertThat(response).extracting("memberProfileDto").containsOnly(givenMember.getprofileDto());
    }


    @Test
    @DisplayName("댓글 삭제")
    void deleteCommentTest() {
        //Given
        dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.FREE);

        //When
        commentService.deleteComment(givenLoginMember, 1L);

        //Then
        Comment comment = commentRepository.findById(1L).get();
        Assertions.assertEquals(CommentStatus.DELETE, comment.getStatus());
    }
}
