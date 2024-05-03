package com.aliens.backend.docs;

import com.aliens.backend.board.controller.dto.response.CommentResponse;
import com.aliens.backend.board.controller.dto.request.ParentCommentCreateRequest;
import com.aliens.backend.board.controller.dto.request.ChildCommentCreateRequest;
import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.controller.dto.response.MemberProfileDto;
import com.aliens.backend.board.domain.enums.BoardCategory;
import com.aliens.backend.board.domain.enums.CommentStatus;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.CommentSuccess;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentRestDocsTest extends BaseRestDocsTest{

    @Test
    @DisplayName("API - 부모 댓글 작성")
    void createParentCommentTest() throws Exception {
        // Given
        String content = "부모 댓글 내용";
        ParentCommentCreateRequest request = new ParentCommentCreateRequest(1L, content);
        final SuccessResponse<?> response = SuccessResponse.of(CommentSuccess.PARENT_COMMENT_CREATE_SUCCESS, "부모 댓글 작성에 성공했습니다.");
        doReturn(response).when(commentController).createParentComment(any(), any());

        // When & Then
        mockMvc.perform(post("/comments/parent")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())

                .andDo(document("api-create-parent-comment",
                        requestFields(
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("boardId").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("응답 결과")
                        )
                ));
    }

    @Test
    @DisplayName("API - 자식 댓글 작성")
    void createChildCommentTest() throws Exception {
        // Given
        String givenContent = "댓글 내용";
        ChildCommentCreateRequest request = new ChildCommentCreateRequest(1L,givenContent,1L);

        final SuccessResponse<?> response = SuccessResponse.of(CommentSuccess.CHILD_COMMENT_CREATE_SUCCESS, "자식 댓글 작성에 성공했습니다.");
        doReturn(response).when(commentController).createChildComment(any(), any());

        // When & Then
        mockMvc.perform(post("/comments/child")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                )
                .andExpect(status().isOk())

                .andDo(document("api-create-child-comment",
                        requestFields(
                                fieldWithPath("boardId").description("게시글 ID"),
                                fieldWithPath("content").description("댓글 내용"),
                                fieldWithPath("parentCommentId").description("부모 댓글 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("응답 결과")
                        )
                ));
    }

    @Test
    @DisplayName("API - 본인이 댓글 단 게시글 조회")
    void getPageMyCommentedBoardsTest() throws Exception {
        // Given
        final List<BoardResponse> boardResponses = List.of(
                createBoardResponse(),
                createBoardResponse());

        final SuccessResponse<List<BoardResponse>> response = SuccessResponse.of(CommentSuccess.GET_MY_COMMENTED_BOARD_PAGE_SUCCESS, boardResponses);
        doReturn(response).when(commentController).getPageMyCommentedBoards(any(), any());

        // When & Then
        mockMvc.perform(get("/comments/my-boards")
                        .param("page", "0")
                        .param("size", "10")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("api-get-commented-board-page",
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 (0부터 시작)"),
                                parameterWithName("size").description("페이지당 아이템 개수")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("게시글 조회 결과"),
                                fieldWithPath("result[].id").description("게시글 ID"),
                                fieldWithPath("result[].category").description("게시글 카테고리"),
                                fieldWithPath("result[].title").description("게시글 제목"),
                                fieldWithPath("result[].content").description("게시글 내용"),
                                fieldWithPath("result[].greatCount").description("게시글 좋아요 수"),
                                fieldWithPath("result[].commentCount").description("게시글 댓글 수"),
                                fieldWithPath("result[].imageUrls").description("게시글 이미지 URL 리스트"),
                                fieldWithPath("result[].createdAt").description("게시글 생성 시간"),
                                fieldWithPath("result[].memberProfileDto.name").description("게시글 작성자 이름"),
                                fieldWithPath("result[].memberProfileDto.profileImageUrl").description("게시글 작성자 프로필 이미지 URL"),
                                fieldWithPath("result[].memberProfileDto.nationality").description("게시글 작성자 국적")
                        )
                ));
    }

    private BoardResponse createBoardResponse() {
        return new BoardResponse(1L,
                BoardCategory.FREE,
                "제목 1",
                "내용 1",
                1L,
                1L,
                List.of("/test", "/test"),
                Instant.now(),
                new MemberProfileDto("작성자 이름", "/test","KOREA")
        );
    }

    @Test
    @DisplayName("API - 해당 게시글의 모든 댓글 조회")
    void getCommentsByBoardIdTest() throws Exception {
        // Given
        final List<CommentResponse> commentResponseList = List.of(createCommentResponseWithoutChildren(), createCommentResponseWithChildren());
        final SuccessResponse<List<CommentResponse>> response = SuccessResponse.of(CommentSuccess.GET_COMMENTS_BY_BOARD_ID_SUCCESS, commentResponseList);
        doReturn(response).when(commentController).getCommentsByBoardId(any());

        // When & Then
        mockMvc.perform(get("/comments/boards")
                        .param("board-id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("api-get-comments-by-board-id",
                        queryParameters(
                                parameterWithName("board-id").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("댓글 조회 결과"),
                                fieldWithPath("result[].status").description("댓글 상태"),
                                fieldWithPath("result[].id").description("댓글 ID"),
                                fieldWithPath("result[].content").description("댓글 내용"),
                                fieldWithPath("result[].createdAt").description("댓글 생성 시간"),
                                fieldWithPath("result[].memberProfileDto.name").description("댓글 작성자 이름"),
                                fieldWithPath("result[].memberProfileDto.profileImageUrl").description("댓글 작성자 프로필 이미지 URL"),
                                fieldWithPath("result[].memberProfileDto.nationality").description("댓글 작성자 국적"),
                                fieldWithPath("result[].children").description("자식 댓글 리스트").optional(), // 자식 댓글 리스트는 선택적(optional)으로 표시
                                fieldWithPath("result[].children[].status").description("자식 댓글 상태"),
                                fieldWithPath("result[].children[].id").description("자식 댓글 ID"),
                                fieldWithPath("result[].children[].content").description("자식 댓글 내용"),
                                fieldWithPath("result[].children[].createdAt").description("자식 댓글 생성 시간"),
                                fieldWithPath("result[].children[].memberProfileDto.name").description("자식 댓글 작성자 이름"),
                                fieldWithPath("result[].children[].memberProfileDto.profileImageUrl").description("자식 댓글 작성자 프로필 이미지 URL"),
                                fieldWithPath("result[].children[].memberProfileDto.nationality").description("자식 댓글 작성자 국적"),
                                fieldWithPath("result[].children[].children").description("자식 댓글의 자식 댓글 리스트").optional() // 자식 댓글의 자식 댓글이 없을 수 있으므로 optional로 표시
                        )
                ));
    }
    private CommentResponse createCommentResponseWithChildren() {
        CommentResponse parentComment = new CommentResponse(
                CommentStatus.ACTIVE,
                1L,
                "부모 댓글 내용",
                Instant.now(),
                new MemberProfileDto("작성자 이름", "/test", "KOREA")
        );

        CommentResponse childComment1 = new CommentResponse(
                CommentStatus.ACTIVE,
                2L,
                "자식 댓글 내용 1",
                Instant.now(),
                new MemberProfileDto("작성자 이름", "/test", "KOREA")
        );

        CommentResponse childComment2 = new CommentResponse(
                CommentStatus.ACTIVE,
                3L,
                "자식 댓글 내용 2",
                Instant.now(),
                new MemberProfileDto("작성자 이름", "/test", "KOREA")
        );

        parentComment.setChildren(List.of(childComment1, childComment2));
        return parentComment;
    }

    private CommentResponse createCommentResponseWithoutChildren() {
        return new CommentResponse(
                CommentStatus.ACTIVE,
                1L,
                "댓글 내용",
                Instant.now(),
                new MemberProfileDto("작성자 이름", "/test", "KOREA"),
                null
        );
    }

    @Test
    @DisplayName("API - 댓글 삭제")
    void deleteCommentTest() throws Exception {
        // Given

        final SuccessResponse<?> response = SuccessResponse.of(CommentSuccess. DELETE_COMMENT_SUCCESS);
        doReturn(response).when(commentController).deleteComment(any(), any());

        // When & Then
        mockMvc.perform(delete("/comments?comment-id={id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                )
                .andExpect(status().isOk())

                .andDo(document("api-delete-child-comment",
                        queryParameters(
                                parameterWithName("comment-id").description("댓글 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("응답 결과")
                        )
                ));
    }
}
