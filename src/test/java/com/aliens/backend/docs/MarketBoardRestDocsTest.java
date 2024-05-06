package com.aliens.backend.docs;

import com.aliens.backend.board.controller.dto.request.MarketChangeRequest;
import com.aliens.backend.board.controller.dto.request.MarketBoardCreateRequest;
import com.aliens.backend.board.controller.dto.response.MarketBoardResponse;
import com.aliens.backend.board.controller.dto.response.MemberProfileDto;
import com.aliens.backend.board.domain.enums.ProductQuality;
import com.aliens.backend.board.domain.enums.SaleStatus;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.MarketBoardSuccess;
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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MarketBoardRestDocsTest extends BaseRestDocsTest  {

    @Test
    @DisplayName("API - 장터 게시글 생성")
    void createMarketBoardTest() throws Exception {
        // Given
        MarketBoardCreateRequest request = new MarketBoardCreateRequest(
                "Title",
                "Content",
                SaleStatus.SELL,
                "10000",
                ProductQuality.BRAND_NEW);
        SuccessResponse<?> response = SuccessResponse.of(MarketBoardSuccess.CREATE_MARKET_BOARD_SUCCESS);
        doReturn(response).when(marketBoardController).createMarketBoard(any(), any(), any());

        MockMultipartFile multipartFile = createMultipartFile();
        final MockMultipartFile requestMultipartFile = new MockMultipartFile("request",
                null, "application/json", objectMapper.writeValueAsString(request).getBytes(StandardCharsets.UTF_8));


        // When & Then
        mockMvc.perform(multipart("/boards/market")
                        .file("marketBoardImages", multipartFile.getBytes())
                        .file("marketBoardImages", multipartFile.getBytes())
                        .file(requestMultipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)

                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)

                        .header("Authorization", GIVEN_ACCESS_TOKEN))

                .andExpect(status().is2xxSuccessful())
                .andDo(document("market-board-create",
                        requestParts(
                                partWithName("marketBoardImages").description("게시물 등록 이미지 파일1"),
                                partWithName("marketBoardImages").description("게시물 등록 이미지 파일2"),
                                partWithName("request").description("장터 게시물 내용")
                        ),
                        requestPartFields("request",
                                fieldWithPath("title").description("장터 게시물 제목"),
                                fieldWithPath("content").description("장터 게시물 내용"),
                                fieldWithPath("saleStatus").description("판매 상태 ex) SELL, END"),
                                fieldWithPath("price").description("판매 가격"),
                                fieldWithPath("productQuality").description("상품 상태 ex) BRAND_NEW, ALMOST_NEW, SLIGHT_DEFECT, USED")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("회원가입 결과")
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
    @DisplayName("API - 장터 게시글 조회")
    void getAllBoardsTest() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<MarketBoardResponse> markets = List.of(
                createMarketBoardResponse(),
                createMarketBoardResponse());

        SuccessResponse<List<MarketBoardResponse>> response = SuccessResponse.of(MarketBoardSuccess.GET_MARKET_BOARD_PAGE_SUCCESS, markets);
        doReturn(response).when(marketBoardController).getMarketBoardPage(pageable);

        // When & Then
        mockMvc.perform(get("/boards/market")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("api-market-board-get-all",
                        queryParameters(
                                parameterWithName("page").description("페이지 번호 (0부터 시작)"),
                                parameterWithName("size").description("페이지당 아이템 개수")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("장터 게시글 조회 결과"),
                                fieldWithPath("result[].id").description("게시글 ID"),
                                fieldWithPath("result[].title").description("게시글 제목"),
                                fieldWithPath("result[].saleStatus").description("판매 상태"),
                                fieldWithPath("result[].price").description("상품 가격"),
                                fieldWithPath("result[].productQuality").description("상품 상태"),
                                fieldWithPath("result[].content").description("게시글 내용"),
                                fieldWithPath("result[].greatCount").description("게시글 좋아요 수"),
                                fieldWithPath("result[].commentCount").description("게시글 댓글 수"),
                                fieldWithPath("result[].commentCount").description("게시글 댓글 수"),
                                fieldWithPath("result[].imageUrls").description("이미지 파일 주소들"),
                                fieldWithPath("result[].createdAt").description("게시글 작성 날짜"),
                                fieldWithPath("result[].memberProfileDto.name").description("게시글 작성자 이름"),
                                fieldWithPath("result[].memberProfileDto.profileImageUrl").description("게시글 작성자 프로필 이미지 URL"),
                                fieldWithPath("result[].memberProfileDto.nationality").description("게시글 작성자 국적")
                        )
                ));
    }

    @Test
    @DisplayName("API - 장터 게시글 상세 조회")
    void getMarketBoardDetailsTest() throws Exception {
        // Given
        MarketBoardResponse market = createMarketBoardResponse();

        SuccessResponse<MarketBoardResponse> response = SuccessResponse.of(MarketBoardSuccess.GET_MARKET_BOARD_DETAILS_SUCCESS, market);
        doReturn(response).when(marketBoardController).getMarketBoardDetails(1L);

        // When & Then
        mockMvc.perform(get("/boards/market/details")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("api-market-board-get-details",
                        queryParameters(
                                parameterWithName("id").description("장터 게시글 id")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("장터 게시글 조회 결과"),
                                fieldWithPath("result.id").description("게시글 ID"),
                                fieldWithPath("result.title").description("게시글 제목"),
                                fieldWithPath("result.saleStatus").description("판매 상태"),
                                fieldWithPath("result.price").description("상품 가격"),
                                fieldWithPath("result.productQuality").description("상품 상태"),
                                fieldWithPath("result.content").description("게시글 내용"),
                                fieldWithPath("result.greatCount").description("게시글 좋아요 수"),
                                fieldWithPath("result.commentCount").description("게시글 댓글 수"),
                                fieldWithPath("result.imageUrls").description("이미지 파일 주소들"),
                                fieldWithPath("result.createdAt").description("게시글 작성 날짜"),
                                fieldWithPath("result.memberProfileDto.name").description("게시글 작성자 이름"),
                                fieldWithPath("result.memberProfileDto.profileImageUrl").description("게시글 작성자 프로필 이미지 URL"),
                                fieldWithPath("result.memberProfileDto.nationality").description("게시글 작성자 국적")
                        )
                ));
    }

    @Test
    @DisplayName("API - 장터 게시글 검색")
    void searchAllBoardPageTest() throws Exception {
        // Given
        String searchKeyword = "검색어";
        List<MarketBoardResponse> markets = List.of(
                createMarketBoardResponse(),
                createMarketBoardResponse());

        SuccessResponse<List<MarketBoardResponse>> response = SuccessResponse.of(MarketBoardSuccess.SEARCH_MARKET_BOARD_SUCCESS, markets);
        doReturn(response).when(marketBoardController).searchMarketBoards(any(), any());

        // When & Then
        mockMvc.perform(get("/boards/market/search")
                        .param("search-keyword", searchKeyword)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("api-market-board-search",
                        queryParameters(
                                parameterWithName("search-keyword").description("검색어"),
                                parameterWithName("page").description("페이지 번호 (0부터 시작)"),
                                parameterWithName("size").description("페이지당 아이템 개수")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("장터 게시글 조회 결과"),
                                fieldWithPath("result[].id").description("게시글 ID"),
                                fieldWithPath("result[].title").description("게시글 제목"),
                                fieldWithPath("result[].saleStatus").description("판매 상태"),
                                fieldWithPath("result[].price").description("상품 가격"),
                                fieldWithPath("result[].productQuality").description("상품 상태"),
                                fieldWithPath("result[].content").description("게시글 내용"),
                                fieldWithPath("result[].greatCount").description("게시글 좋아요 수"),
                                fieldWithPath("result[].commentCount").description("게시글 댓글 수"),
                                fieldWithPath("result[].imageUrls").description("이미지 파일 주소들"),
                                fieldWithPath("result[].createdAt").description("게시글 작성 날짜"),
                                fieldWithPath("result[].memberProfileDto.name").description("게시글 작성자 이름"),
                                fieldWithPath("result[].memberProfileDto.profileImageUrl").description("게시글 작성자 프로필 이미지 URL"),
                                fieldWithPath("result[].memberProfileDto.nationality").description("게시글 작성자 국적")
                        )
                ));
    }

    @Test
    @DisplayName("API - 장터 게시글 수정")
    void changeMarketBoardTest() throws Exception {
        // Given
        MarketChangeRequest request = new MarketChangeRequest(
                "Title",
                "Content",
                SaleStatus.SELL,
                "10000",
                ProductQuality.BRAND_NEW);
        SuccessResponse<?> response = SuccessResponse.of(MarketBoardSuccess.CHANGE_MARKET_BOARD_SUCCESS);
        doReturn(response).when(marketBoardController).changeMarketBoard(any(), any(), any());


        // When & Then
        mockMvc.perform(put("/boards/market")
                        .param("id", "1")
                        .content(objectMapper.writeValueAsString(request))
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                )

                .andExpect(status().is2xxSuccessful())
                .andDo(document("market-board-change",
                        requestFields(
                                fieldWithPath("title").description("장터 게시물 제목"),
                                fieldWithPath("content").description("장터 게시물 내용"),
                                fieldWithPath("saleStatus").description("판매 상태"),
                                fieldWithPath("price").description("판매 가격"),
                                fieldWithPath("productQuality").description("상품 상태")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("회원가입 결과")
                        )
                ));
    }

    private MarketBoardResponse createMarketBoardResponse() {
        return new MarketBoardResponse(
                1L,
                "title",
                SaleStatus.SELL,
                "10000",
                ProductQuality.ALMOST_NEW,
                "팔고 있어요",
                2L,
                1L,
                List.of("/test","/test"),
                Instant.now(),
                new MemberProfileDto("작성자 이름","/test", "KROEA"));
    }
}
