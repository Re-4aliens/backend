package com.aliens.backend.board.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.domain.Board;
import com.aliens.backend.board.domain.Great;
import com.aliens.backend.board.domain.enums.BoardCategory;
import com.aliens.backend.board.domain.repository.BoardRepository;
import com.aliens.backend.board.domain.repository.GreatRepository;
import com.aliens.backend.global.BaseIntegrationTest;
import com.aliens.backend.global.DummyGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class GreatServiceTest extends BaseIntegrationTest {
    @Autowired
    GreatService greatService;

    @Autowired
    GreatRepository greatRepository;

    @Autowired
    DummyGenerator dummyGenerator;

    @Autowired
    BoardRepository boardRepository;

    Member givenMember;
    LoginMember givenLoginMember;

    @BeforeEach
    void setUp() {
        givenMember = dummyGenerator.generateSingleMember();
        givenLoginMember = givenMember.getLoginMember();
    }

    @Test
    @DisplayName("게시글 좋아요 등록")
    void greatAtBoardTest() {
        //Given
        Member newMember = dummyGenerator.generateSingleMember();
        LoginMember newLoginMember = newMember.getLoginMember();
        Board givenBoard = dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.FREE);

        //When
        greatService.greatAtBoard(givenBoard.getId(), newLoginMember);

        // Then
        Great response = greatRepository.findAll().get(0);
        Assertions.assertNotNull(response);
    }

    @Test
    @DisplayName("좋아요한 게시글 조회")
    void getGreatBoardPageTest() {
        //Given
        PageRequest givenPageable = PageRequest.of(0, 10);
        for(int i = 0; i < 3; i++) {
            dummyGenerator.generateSingleNormalBoard(givenMember, BoardCategory.FREE);
        }

        //When
        List<BoardResponse> response = greatService.getGreatBoardPage(givenLoginMember, givenPageable);

        // Then
        assertAll(
                () -> assertThat(response).hasSize(3),
                () -> assertThat(response).extracting("greatCount").containsOnly(1L)
        );
    }
}
