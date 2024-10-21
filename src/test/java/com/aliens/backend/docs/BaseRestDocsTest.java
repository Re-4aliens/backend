package com.aliens.backend.docs;

import com.aliens.backend.auth.controller.AuthController;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.block.controller.BlockController;
import com.aliens.backend.board.controller.BoardController;
import com.aliens.backend.board.controller.CommentController;
import com.aliens.backend.board.controller.GreatController;
import com.aliens.backend.board.controller.MarketController;
import com.aliens.backend.chat.controller.ChatController;
import com.aliens.backend.chat.controller.ChatReportController;
import com.aliens.backend.email.controller.EmailController;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.inquire.InquiryController;
import com.aliens.backend.member.controller.MemberController;
import com.aliens.backend.notification.controller.NotificationController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@ActiveProfiles("test")
public abstract class BaseRestDocsTest {

    @SpyBean
    protected ChatController chatController;
    @SpyBean
    protected AuthController authController;
    @SpyBean
    protected ChatReportController chatReportController;
    @SpyBean
    protected MemberController memberController;
    @SpyBean
    protected EmailController emailController;
    @SpyBean
    protected BlockController blockController;
    @SpyBean
    protected BoardController boardController;
    @SpyBean
    protected CommentController commentController;
    @SpyBean
    protected GreatController greatController;
    @SpyBean
    protected MarketController marketBoardController;
    @SpyBean
    protected NotificationController notificationController;
    @SpyBean
    protected InquiryController inquiryController;


    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected DummyGenerator dummyGenerator;

    protected Member member;
    protected String GIVEN_ACCESS_TOKEN;
    protected ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        member = dummyGenerator.generateSingleMember();
        GIVEN_ACCESS_TOKEN = dummyGenerator.generateAccessToken(member);
    }
}