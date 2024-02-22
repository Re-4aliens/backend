package com.aliens.backend.docs;

import com.aliens.backend.auth.controller.AuthController;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.block.controller.BlockController;
import com.aliens.backend.chat.controller.ChatController;
import com.aliens.backend.chat.controller.ChatReportController;
import com.aliens.backend.email.controller.EmailController;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.mathcing.controller.MatchingController;
import com.aliens.backend.member.controller.MemberController;
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
    protected MatchingController matchingController;
    @SpyBean
    protected EmailController emailController;
    @SpyBean
    protected BlockController blockController;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected DummyGenerator dummyGenerator;

    protected String GIVEN_ACCESS_TOKEN;
    protected ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        Member member = dummyGenerator.generateSingleMember();
        GIVEN_ACCESS_TOKEN = dummyGenerator.generateAccessToken(member);
    }
}