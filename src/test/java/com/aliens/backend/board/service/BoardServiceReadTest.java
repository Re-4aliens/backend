package com.aliens.backend.board.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.controller.dto.response.MarketBoardResponse;
import com.aliens.backend.board.domain.enums.BoardCategory;
import com.aliens.backend.global.BaseIntegrationTest;
import com.aliens.backend.global.DummyGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class BoardServiceReadTest extends BaseIntegrationTest {

    @Autowired
    BoardReadService boardReadService;

    @Autowired
    DummyGenerator dummyGenerator;

    LoginMember givenLoginMember;
    Member givenMember;
    PageRequest givenPageable = PageRequest.of(0, 10);

    @BeforeEach
    void setUp() {
        givenMember = dummyGenerator.generateSingleMember();
        givenLoginMember = givenMember.getLoginMember();
    }

    @Test
    @DisplayName("전체 게시판 조회")
    void getAllBoardPageTest() {
        //Given
        for (int i = 0; i < 3; i++) {
            dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.FREE);
        }

        //When
        List<BoardResponse> response = boardReadService.getBoardPage(givenPageable);

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
    @DisplayName("카테고리 게시판 조회")
    void getBoardPageWithCategoryTest() {
        //Given
        String GIVEN_CATEGORY = "fashion";
        dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.FASHION);
        dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.FASHION);
        dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.ALL);

        //When
        List<BoardResponse> response = boardReadService.getBoardPageWithCategory(GIVEN_CATEGORY, givenPageable);

        //Then
        assertAll(
                () -> assertThat(response).hasSize(2),
                () -> assertThat(response).extracting("id").containsAnyElementsOf(List.of(1L, 2L, 3L)),
                () -> assertThat(response).extracting("title").contains(DummyGenerator.GIVEN_BOARD_TITLE),
                () -> assertThat(response).extracting("content").contains(DummyGenerator.GIVEN_BOARD_CONTENT),
                () -> assertThat(response).extracting("greatCount").containsOnly(1L),
                () -> assertThat(response).extracting("commentCount").containsOnly(1L)
        );
    }

    @Test
    @DisplayName("전체 게시판 검색")
    void searchBoardPageWithKeywordTest() {
        //Given
        for(int i = 0; i < 5; i++) {
            dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.FASHION);
        }
        String GIVEN_CONTENT = "임시";
        dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.FASHION, GIVEN_CONTENT + "hi");
        dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.ALL,GIVEN_CONTENT + "nice to meet you");

        //When
        List<BoardResponse> response = boardReadService.searchBoardPageWithKeyword(GIVEN_CONTENT, givenPageable);

        //Then
        assertAll(
                () -> assertThat(response).hasSize(2),
                () -> assertThat(response).extracting("id").containsAnyElementsOf(List.of(6L, 7L)),
                () -> assertThat(response).extracting("title").contains(DummyGenerator.GIVEN_BOARD_TITLE),
                () -> assertThat(response).extracting("content").allMatch(content -> ((String) content).startsWith(GIVEN_CONTENT)),
                () -> assertThat(response).extracting("greatCount").containsOnly(1L),
                () -> assertThat(response).extracting("commentCount").containsOnly(1L)
        );
    }

    @Test
    @DisplayName("공지사항 게시판 조회")
    void getAnnouncePageTest() {
        //Given
        for(int i = 0; i < 5; i++) {
            dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.FASHION);
        }
        dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.ANNOUNCEMENT);
        dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.ANNOUNCEMENT);

        //When
        List<BoardResponse> response = boardReadService.getAnnouncePage(givenPageable);

        //Then
        assertAll(
                () -> assertThat(response).hasSize(2),
                () -> assertThat(response).extracting("id").containsAnyElementsOf(List.of(6L, 7L)),
                () -> assertThat(response).extracting("title").contains(DummyGenerator.GIVEN_BOARD_TITLE),
                () -> assertThat(response).extracting("content").contains(DummyGenerator.GIVEN_BOARD_CONTENT),
                () -> assertThat(response).extracting("greatCount").containsOnly(1L),
                () -> assertThat(response).extracting("commentCount").containsOnly(1L)
        );
    }

    @Test
    @DisplayName("장터 게시판 조회")
    void getMarketBoardPageTest() {
        //Given
        for (int i = 0; i < 5; i++) {
            dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.FASHION);
        }
        dummyGenerator.generateSingleMarketBoard(givenMember);
        dummyGenerator.generateSingleMarketBoard(givenMember);

        //When
        List<MarketBoardResponse> response = boardReadService.getMarketBoardPage(givenPageable);

        //Then
        assertAll(
                () -> assertThat(response).hasSize(2),
                () -> assertThat(response).extracting("id").containsAnyElementsOf(List.of(6L, 7L)),
                () -> assertThat(response).extracting("title").contains(DummyGenerator.GIVEN_BOARD_TITLE),
                () -> assertThat(response).extracting("content").contains(DummyGenerator.GIVEN_BOARD_CONTENT),
                () -> assertThat(response).extracting("greatCount").containsOnly(1L),
                () -> assertThat(response).extracting("commentCount").containsOnly(1L),
                () -> assertThat(response).extracting("price").allMatch(p -> p.equals(DummyGenerator.GIVEN_PRICE)),
                () -> assertThat(response).extracting("saleStatus").allMatch(s -> s.equals(DummyGenerator.GIVEN_SALE_STATUS)),
                () -> assertThat(response).extracting("productStatus").allMatch(p -> p.equals(DummyGenerator.GIVEN_PRODUCT_STATUS))
        );
    }

    @Test
    @DisplayName("장터 게시판 상세조회")
    void getMarketBoardDetailsTest() {
        //Given
        dummyGenerator.generateSingleMarketBoard(givenMember);

        //When
        MarketBoardResponse response = boardReadService.getMarketBoardDetails(1L);

        //Then
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).contains(DummyGenerator.GIVEN_BOARD_TITLE);
        assertThat(response.content()).contains(DummyGenerator.GIVEN_BOARD_CONTENT);
        assertThat(response.greatCount()).isEqualTo(1L);
        assertThat(response.greatCount()).isEqualTo(1L);
        assertThat(response.price()).isEqualTo(DummyGenerator.GIVEN_PRICE);
        assertThat(response.saleStatus()).isEqualTo(DummyGenerator.GIVEN_SALE_STATUS);
        assertThat(response.productStatus()).isEqualTo(DummyGenerator.GIVEN_PRODUCT_STATUS);
    }

    @Test
    @DisplayName("장터 게시판 검색")
    void searchMarketBoardPageTest() {
        //Given
        for (int i = 0; i < 10; i++) {
            dummyGenerator.generateSingleMarketBoard(givenMember);
        }
        String givenContent = "임시";
        dummyGenerator.generateSingleMarketBoard(givenMember, givenContent + "hi");
        dummyGenerator.generateSingleMarketBoard(givenMember, givenContent + "nice to meet you");

        //When
        List<MarketBoardResponse> response = boardReadService.searchMarketBoardPage(givenContent, givenPageable);

        //Then
        assertAll(
                () -> assertThat(response).hasSize(2),
                () -> assertThat(response).extracting("id").containsAnyOf(11L, 12L),
                () -> assertThat(response).extracting("title").contains(DummyGenerator.GIVEN_BOARD_TITLE),
                () -> assertThat(response).extracting("content").allMatch(c -> c.toString().contains(givenContent)),
                () -> assertThat(response).extracting("greatCount").containsOnly(1L),
                () -> assertThat(response).extracting("commentCount").containsOnly(1L),
                () -> assertThat(response).extracting("price").allMatch(p -> p.equals(DummyGenerator.GIVEN_PRICE)),
                () -> assertThat(response).extracting("saleStatus").allMatch(s -> s.equals(DummyGenerator.GIVEN_SALE_STATUS)),
                () -> assertThat(response).extracting("productStatus").allMatch(p -> p.equals(DummyGenerator.GIVEN_PRODUCT_STATUS))
        );
    }

    @Test
    @DisplayName("내가 작성한 게시글 조회")
    void getMyBoardPageTest() {
        //Given
        for (int i = 0; i < 5; i++) {
            dummyGenerator.generateSingleMarketBoard(givenMember);
            dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.FASHION);
        }


        Member newMember = dummyGenerator.generateSingleMember();
        LoginMember loginMember = newMember.getLoginMember();

        String givenContent = "개인적으로쓴 글";
        dummyGenerator.generateSingleMarketBoard(newMember,givenContent);
        dummyGenerator.generateSingleMarketBoard(newMember,givenContent);

        //When
        List<BoardResponse> response = boardReadService.getMyBoardPage(loginMember, givenPageable);

        //Then
        assertAll(
                () -> assertThat(response).hasSize(2),
                () -> assertThat(response).extracting("id").containsAnyOf(11L, 12L),
                () -> assertThat(response).extracting("title").contains(DummyGenerator.GIVEN_BOARD_TITLE),
                () -> assertThat(response).extracting("content").allMatch(c -> c.toString().contains(givenContent)),
                () -> assertThat(response).extracting("greatCount").containsOnly(1L),
                () -> assertThat(response).extracting("commentCount").containsOnly(1L)
        );
    }

    @Test
    @DisplayName("카테고리와 키워드를 갖고 게시글 검색")
    void searchBoardPageWithKeywordAndCategoryTest() {
        //Given
        for (int i = 0; i < 5; i++) {
            dummyGenerator.generateSingleMarketBoard(givenMember);
            dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.FASHION);
        }

        String givenContent = "개인적으로쓴 글";
        dummyGenerator.generateSingleMarketBoard(givenMember,givenContent);
        dummyGenerator.generateSingleMarketBoard(givenMember,givenContent);

        //When
        List<BoardResponse> response = boardReadService.searchBoardPageWithKeywordAndCategory("개인", "market", givenPageable);

        //Then
        assertAll(
                () -> assertThat(response).hasSize(2),
                () -> assertThat(response).extracting("id").containsAnyOf(11L, 12L),
                () -> assertThat(response).extracting("title").contains(DummyGenerator.GIVEN_BOARD_TITLE),
                () -> assertThat(response).extracting("content").allMatch(c -> c.toString().contains(givenContent)),
                () -> assertThat(response).extracting("greatCount").containsOnly(1L),
                () -> assertThat(response).extracting("commentCount").containsOnly(1L)
        );
    }
}