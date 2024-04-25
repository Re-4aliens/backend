package com.aliens.backend.docs;

import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.controller.dto.response.MemberProfileDto;
import com.aliens.backend.board.domain.enums.BoardCategory;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.CommentSuccess;
import com.aliens.backend.global.response.success.GreatSuccess;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GreatRestDocsTest extends BaseRestDocsTest{

    @Test
    @DisplayName("API - 게시글 좋아요 * 이미 있다면 좋아요 취소")
    void deleteCommentTest() throws Exception {
        // Given
        final SuccessResponse<?> response = SuccessResponse.of(GreatSuccess.GREAT_AT_BOARD_SUCCESS);
        doReturn(response).when(greatController).greatAtBoard(any(), any());

        // When & Then
        mockMvc.perform(post("/great?board-id={id}",1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                )
                .andExpect(status().isOk())

                .andDo(document("api-great-at-board",
                        queryParameters(
                                parameterWithName("board-id").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("응답 결과")
                        )
                ));
    }

    @Test
    @DisplayName("API - 좋아요한 게시글 조회")
    void getAllGreatBoardsTest() throws Exception {
        // Given
        final List<BoardResponse> boardResponses = List.of(
                createBoardResponse(),
                createBoardResponse());

        final SuccessResponse<?> response = SuccessResponse.of(GreatSuccess.GET_ALL_GREAT_BOARDS_SUCCESS, boardResponses);
        doReturn(response).when(greatController).getAllGreatBoards(any(), any());

        // When & Then
        mockMvc.perform(get("/great/my-board")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("api-great-get-my-boards",
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 (0부터 시작)"),
                                parameterWithName("size").description("페이지당 아이템 개수")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("내가 작성한 게시글 조회 결과"),
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
}
