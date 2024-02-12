package com.aliens.backend.matching.integration;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.MemberRole;
import com.aliens.backend.global.BaseTest;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import com.aliens.backend.mathcing.service.model.Language;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
public class MatchingApplicationApiTest extends BaseTest {

    @Autowired MockMvc mockMvc;
    @SpyBean MatchingApplicationService matchingApplicationService;

    ObjectMapper objectMapper = new ObjectMapper();
    String accessToken;
    MatchingApplicationRequest matchingApplicationRequest;
    LoginMember loginMember;

    @BeforeEach
    void setUp() {
        accessToken = "accessToken";
        loginMember = new LoginMember(1L, MemberRole.MEMBER);
        matchingApplicationRequest = new MatchingApplicationRequest(Language.KOREAN, Language.ENGLISH);
    }

    @Test
    @DisplayName("매칭 신청 API 테스트")
    void applyMatchApiTest() throws Exception {
        doNothing().when(matchingApplicationService).saveParticipant(loginMember, matchingApplicationRequest);

        mockMvc.perform(post("/matchings/applications")
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(matchingApplicationRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
//                .andDo(document("post-matching-application",
//                requestParts(
//                        partWithName("matchingApplicationInput").description("매칭 신청 정보 입력")
//                ),
//                responseFields(
//                        fieldWithPath("response").description("매칭 신청 결과")
//                )
//        )
//                );

    }
}
