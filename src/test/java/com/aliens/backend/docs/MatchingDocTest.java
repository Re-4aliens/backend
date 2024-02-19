package com.aliens.backend.docs;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.global.BaseServiceTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.matching.util.time.MockTime;
import com.aliens.backend.mathcing.business.model.Language;
import com.aliens.backend.mathcing.controller.MatchingApplicationController;
import com.aliens.backend.mathcing.controller.MatchingProcessController;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class MatchingDocTest extends BaseServiceTest {
    @Autowired MockMvc mockMvc;
    @Autowired MatchingApplicationController matchingApplicationController;
    @Autowired MatchingProcessController matchingProcessController;
    @Autowired MatchingApplicationService matchingApplicationService;
    @Autowired DummyGenerator dummyGenerator;

    ObjectMapper objectMapper = new ObjectMapper();
    String baseUrl = "/matchings";
    Member member;
    String GIVEN_ACCESS_TOKEN;
    MatchingApplicationRequest request;

    @BeforeEach
    void setUp() {
        dummyGenerator.generateMatchingRound(MockTime.TUESDAY);
        member = dummyGenerator.generateSingleMember();
        GIVEN_ACCESS_TOKEN = dummyGenerator.generateAccessToken(member);
        request = new MatchingApplicationRequest(Language.KOREAN, Language.ENGLISH);
    }

    @Test
    @DisplayName("API - 매칭 신청")
    void applyMatchTest() throws Exception {
        mockMvc.perform(post(baseUrl + "/applications")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(document("matching-application-post",
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("매칭 신청 결과")
                        )));
    }

    @Test
    @DisplayName("API - 매칭 신청 내역 조회")
    void getMatchingApplicationTest() throws Exception {
        // given
        LoginMember loginMember = member.getLoginMember();

        // when
        matchingApplicationService.saveParticipant(loginMember, request);

        // then
        mockMvc.perform(get(baseUrl + "/applications")
                        .header("Authorization", GIVEN_ACCESS_TOKEN))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("matching-application-get",
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("매칭 신청 내역 조회 결과"),
                                fieldWithPath("result.matchingRound").description("매칭 회차"),
                                fieldWithPath("result.memberId").description("회원 ID"),
                                fieldWithPath("result.firstPreferLanguage").description("첫 번째 선호 언어"),
                                fieldWithPath("result.secondPreferLanguage").description("두 번째 선호 언어")
                        )));
    }

    @Test
    @DisplayName("API - 매칭 신청 취소")
    void cancelMatchingApplicationTest() throws Exception {
        // given
        LoginMember loginMember = member.getLoginMember();
        matchingApplicationService.saveParticipant(loginMember, request);

        // when & then
        mockMvc.perform(delete(baseUrl + "/applications")
                        .header("Authorization", GIVEN_ACCESS_TOKEN))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("matching-application-cancel",
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("매칭 신청 취소 결과")
                        )));
    }
}
