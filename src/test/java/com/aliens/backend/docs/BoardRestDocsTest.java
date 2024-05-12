package com.aliens.backend.docs;

import com.aliens.backend.board.controller.dto.request.BoardCreateRequest;
import com.aliens.backend.board.controller.dto.request.ReportBoardRequest;
import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.controller.dto.response.MemberProfileDto;
import com.aliens.backend.board.domain.enums.BoardCategory;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.BoardSuccess;
import com.aliens.backend.global.response.success.ReportSuccess;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class BoardRestDocsTest extends BaseRestDocsTest {

    @Test
    @DisplayName("API - 게시글 생성")
    void createBoardTest() throws Exception {
        // Given
        final BoardCreateRequest request = new BoardCreateRequest(
                "Title",
                "Content",
                BoardCategory.FREE);
        final SuccessResponse<?> response = SuccessResponse.of(BoardSuccess.POST_BOARD_SUCCESS);
        doReturn(response).when(boardController).createBoard(any(), any(), any());

        final MockMultipartFile multipartFile = createMultipartFile();
        final MockMultipartFile requestMultipartFile = new MockMultipartFile("request",
                null, "application/json", objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));

        // When & Then
        mockMvc.perform(multipart("/boards/normal")
                        .file("boardImages", multipartFile.getBytes())
                        .file(requestMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", GIVEN_ACCESS_TOKEN))

                .andExpect(status().is2xxSuccessful())
                .andDo(document("board-create",
                        requestParts(
                                partWithName("boardImages").description("게시물 등록 이미지 파일"),
                                partWithName("request").description("게시물 정보")
                        ),
                        requestPartFields("request",
                                fieldWithPath("title").description("게시물 제목"),
                                fieldWithPath("content").description("게시물 내용"),
                                fieldWithPath("boardCategory").description("게시물 카테고리 ex) ALL, FREE, INFO, MUSIC, GAME, FOOD, FASHION")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("응답 결과")
                        )
                ));
    }

    private MockMultipartFile createMultipartFile() {
        return new MockMultipartFile("boardImages",
                "test-image.png",
                "image/png",
                "test data".getBytes());
    }

    @Test
    @DisplayName("API - 전체 게시글 조회")
    void getAllBoardsTest() throws Exception {
        // Given
        final Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        final List<BoardResponse> boardResponses = List.of(
                createBoardResponse(),
                createBoardResponse());

        final SuccessResponse<List<BoardResponse>> response = SuccessResponse.of(BoardSuccess.GET_ALL_BOARDS_SUCCESS, boardResponses);
        doReturn(response).when(boardController).getAllBoards(pageable);

        // When & Then
        mockMvc.perform(get("/boards")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("api-board-get-all",
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

    @Test
    @DisplayName("API - 특정 카테고리 게시글 조회")
    void getAllBoardsWithCategoryTest() throws Exception {
        // Given
        final String category = "FASHION";
        final List<BoardResponse> boardResponses = List.of(
                createBoardResponse(),
                createBoardResponse());

        final SuccessResponse<List<BoardResponse>> response = SuccessResponse.of(BoardSuccess.GET_ALL_BOARDS_WITH_CATEGORY_SUCCESS, boardResponses);
        doReturn(response).when(boardController).getAllBoardsWithCategory(any(), any());

        // When & Then
        mockMvc.perform(get("/boards/category")
                        .param("category", category)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("api-board-get-all-with-category",
                        queryParameters(
                                parameterWithName("category").description("게시글 카테고리"),
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

    @Test
    @DisplayName("API - 게시글 검색")
    void searchAllBoardPageTest() throws Exception {
        // Given
        final String searchKeyword = "검색어";
        final List<BoardResponse> boardResponses = List.of(
                createBoardResponse(),
                createBoardResponse());

        final SuccessResponse<List<BoardResponse>> response = SuccessResponse.of(BoardSuccess.SEARCH_ALL_BOARDS_SUCCESS, boardResponses);
        doReturn(response).when(boardController).searchAllBoardPage(any(), any());

        // When & Then
        mockMvc.perform(get("/boards/search")
                        .param("search-keyword", searchKeyword)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("api-board-search",
                        queryParameters(
                                parameterWithName("search-keyword").description("검색어"),
                                parameterWithName("page").description("페이지 번호 (0부터 시작)"),
                                parameterWithName("size").description("페이지당 아이템 개수")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("게시글 검색 결과"),
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

    @Test
    @DisplayName("API - 공지사항 게시글 조회")
    void getAllAnnouncementBoardsTest() throws Exception {
        // Given
        final List<BoardResponse> boardResponses = List.of(
                createBoardResponse(),
                createBoardResponse());

        final SuccessResponse<List<BoardResponse>> response = SuccessResponse.of(BoardSuccess.GET_ALL_ANNOUNCEMENT_BOARDS_SUCCESS, boardResponses);
        doReturn(response).when(boardController).getAllAnnouncementBoards(any());

        // When & Then
        mockMvc.perform(get("/boards/announcements")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("api-board-get-all-announcements",
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 (0부터 시작)"),
                                parameterWithName("size").description("페이지당 아이템 개수")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("공지사항 게시글 조회 결과"),
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

    @Test
    @DisplayName("API - 내가 작성한 게시글 조회")
    void getPageMyBoardsTest() throws Exception {
        // Given
        final List<BoardResponse> boardResponses = List.of(
                createBoardResponse(),
                createBoardResponse());

        final SuccessResponse<List<BoardResponse>> response = SuccessResponse.of(BoardSuccess.GET_MY_BOARD_PAGE_SUCCESS, boardResponses);
        doReturn(response).when(boardController).getPageMyBoards(any(), any());

        // When & Then
        mockMvc.perform(get("/boards/writes")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("api-board-get-my-boards",
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

    @Test
    @DisplayName("API - 특정 카테고리 게시글 검색")
    void searchBoardsWithCategoryTest() throws Exception {
        // Given
        final String searchKeyword = "검색어";
        final String category = "FASHION";
        final List<BoardResponse> boardResponses = List.of(
                createBoardResponse(),
                createBoardResponse());

        final SuccessResponse<List<BoardResponse>> response = SuccessResponse.of(BoardSuccess.SEARCH_BOARD_WITH_CATEGORY_SUCCESS, boardResponses);
        doReturn(response).when(boardController).searchBoardsWithCategory(any(), any(), any());

        // When & Then
        mockMvc.perform(get("/boards/category/search")
                        .param("search-keyword", searchKeyword)
                        .param("category", category)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("api-board-search-with-category",
                        queryParameters(
                                parameterWithName("search-keyword").description("검색어"),
                                parameterWithName("category").description("게시글 카테고리"),
                                parameterWithName("page").description("페이지 번호 (0부터 시작)"),
                                parameterWithName("size").description("페이지당 아이템 개수")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("게시글 검색 결과"),
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

    @Test
    @DisplayName("API - 게시글 삭제")
    void deleteBoardTest() throws Exception {
        // Given
        final SuccessResponse<?> response = SuccessResponse.of(BoardSuccess.DELETE_BOARD_SUCCESS);
        doReturn(response).when(boardController).deleteBoard(any(), any());

        // When & Then
        mockMvc.perform(delete("/boards?id={id}", 1)
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("api-board-delete",
                        queryParameters(
                                parameterWithName("id").description("게시글 ID")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("게시글 삭제 결과")
                        )
                ));
    }


    @Test
    @DisplayName("API - 게시글 신고")
    void reportBoard() throws Exception {
        // Given
        final ReportBoardRequest request = new ReportBoardRequest(
                1L,"혐오 발언");
        final SuccessResponse<?> response = SuccessResponse.of(ReportSuccess.REPORT_BOARD_SUCCESS);
        doReturn(response).when(boardController).reportBoard(any(), any());

        // When & Then
        mockMvc.perform(post("/boards/report")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", GIVEN_ACCESS_TOKEN))

                .andExpect(status().is2xxSuccessful())
                .andDo(document("board-report",
                        requestFields(
                                fieldWithPath("boardId").description("신고하는 게시글 ID"),
                                fieldWithPath("reason").description("신고 사유")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("응답 결과")
                        )
                ));
    }
}
