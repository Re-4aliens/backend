package com.aliens.backend.board.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.board.controller.dto.request.MarketChangeRequest;
import com.aliens.backend.board.controller.dto.request.BoardCreateRequest;
import com.aliens.backend.board.controller.dto.request.MarketBoardCreateRequest;
import com.aliens.backend.board.domain.Board;
import com.aliens.backend.board.domain.enums.BoardCategory;
import com.aliens.backend.board.domain.enums.ProductQuality;
import com.aliens.backend.board.domain.enums.SaleStatus;
import com.aliens.backend.board.domain.repository.BoardRepository;
import com.aliens.backend.global.BaseIntegrationTest;
import com.aliens.backend.global.DummyGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

class BoardServiceTest extends BaseIntegrationTest {
    @Autowired BoardService boardService;
    @Autowired BoardRepository boardRepository;
    @Autowired DummyGenerator dummyGenerator;

    MultipartFile file;
    LoginMember givenLoginMember;
    String givenTitle = "제목";
    String givenContent = "내용";
    Member givenMember;


    @BeforeEach
    void setUp() {
        givenMember = dummyGenerator.generateSingleMember();
        givenLoginMember = givenMember.getLoginMember();
        file = dummyGenerator.generateMultipartFile();
    }

    @Test
    @DisplayName("일반 게시물 등록")
    void postNormalBoardTest() {
        //Given
        final BoardCategory givenCategory = BoardCategory.ALL;
        final List<MultipartFile> givenFiles = List.of(file);
        BoardCreateRequest request = new BoardCreateRequest(givenTitle, givenContent, givenCategory);

        //When
        boardService.postNormalBoard(request, givenFiles, givenLoginMember);

        //Then
        final Board response = boardRepository.findAllWithBoardImage().get(0);
        Assertions.assertEquals(1, response.getImages().size());
        Assertions.assertTrue(response.isSameWithRequest(request));
    }

    @Test
    @DisplayName("일반 게시물 등록 - 이미지 없음")
    void postNormalBoardWithOutImagesTest() {
        //Given
        final BoardCategory givenCategory = BoardCategory.ALL;
        final BoardCreateRequest request = new BoardCreateRequest(givenTitle, givenContent, givenCategory);

        //When
        boardService.postNormalBoard(request, null, givenLoginMember);

        //Then
        final Board response = boardRepository.findAllWithBoardImage().get(0);
        Assertions.assertTrue(response.getImages().isEmpty());
        Assertions.assertTrue(response.isSameWithRequest(request));
    }

    @Test
    @DisplayName("장터 게시물 등록")
    void postMarketBoardTest() {
        //Given
        final List<MultipartFile> givenFiles = List.of(file);
        final String givenPrice = "10000";
        MarketBoardCreateRequest request = new MarketBoardCreateRequest(
                givenTitle,
                givenContent,
                SaleStatus.SELL,
                givenPrice,
                ProductQuality.ALMOST_NEW);

        //When
        boardService.postMarketBoard(request, givenFiles, givenLoginMember);

        //Then
        final Board response = boardRepository.findAlLWithMarketInfo().get(0);
        final BoardCreateRequest boardCreateRequest = BoardCreateRequest.from(request);

        Assertions.assertEquals(1, response.getImages().size());
        Assertions.assertTrue(response.isSameWithRequest(boardCreateRequest));
        Assertions.assertEquals(response.getPrice(), givenPrice);
    }

    @Test
    @DisplayName("장터 게시물 등록 - 이미지 없음")
    void postMarketBoardWithOutImagesTest() {
        //Given
        final String givenPrice = "10000";
        final MarketBoardCreateRequest request = new MarketBoardCreateRequest(givenTitle,
                givenContent,
                SaleStatus.SELL,
                givenPrice,
                ProductQuality.ALMOST_NEW);

        //When
        boardService.postMarketBoard(request, null, givenLoginMember);

        //Then
        final Board response = boardRepository.findAlLWithMarketInfo().get(0);
        final BoardCreateRequest boardCreateRequest = BoardCreateRequest.from(request);

        Assertions.assertTrue(response.getImages().isEmpty());
        Assertions.assertTrue(response.isSameWithRequest(boardCreateRequest));
        Assertions.assertEquals(response.getPrice(), givenPrice);
    }

    @Test
    @DisplayName("장터 게시물 수정")
    void changeMarketBoardTest() {
        //Given
        dummyGenerator.generateSingleMarketBoard(givenMember);

        final String changeTitle = "수정 제목";
        final String changeContent = "수정 내용";
        final SaleStatus changeSaleStatus = SaleStatus.END;
        final String changePrice = "100";
        final ProductQuality changeProductQuality = ProductQuality.SLIGHT_DEFECT;
        final MarketChangeRequest request = new MarketChangeRequest(
                changeTitle,
                changeContent,
                changeSaleStatus,
                changePrice,
                changeProductQuality);

        //When
        boardService.changeMarketBoard(1L, request, givenLoginMember);

        //Then
        final Board response = boardRepository.findAlLWithMarketInfo().get(0);

        Assertions.assertEquals(2, response.getImages().size());
        Assertions.assertTrue(response.isSameWithRequest(request));
    }

    @Test
    @DisplayName("게시물 삭제")
    void deleteBoardTest() {
        //Given
        dummyGenerator.generateSingleMarketBoard(givenMember);

        //When
        boardService.deleteBoard(givenLoginMember, 1L);

        //Then
        Assertions.assertNull(boardRepository.findById(1L).orElse(null));
    }
}
