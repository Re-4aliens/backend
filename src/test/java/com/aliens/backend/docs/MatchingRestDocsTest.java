package com.aliens.backend.docs;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
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
import org.springframework.http.MediaType;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MatchingRestDocsTest extends BaseRestDocsTest {

    @Autowired
    MatchingApplicationController matchingApplicationController;
    @Autowired
    MatchingProcessController matchingProcessController;
    @Autowired
    MatchingApplicationService matchingApplicationService;

    ObjectMapper objectMapper = new ObjectMapper();
    String baseUrl = "/matchings";
    Member member;
    List<Member> members;
    String GIVEN_ACCESS_TOKEN;
    MatchingApplicationRequest request;

    @BeforeEach
    void setUp() {
        dummyGenerator.generateMatchingRound(MockTime.TUESDAY);
        request = new MatchingApplicationRequest(Language.KOREAN, Language.ENGLISH);
    }

    @Test
    @DisplayName("API - 매칭 신청")
    void applyMatchTest() throws Exception {
        // given
        createSingleMember();

        // when & then
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
        // given & when
        createSingleMember();
        LoginMember loginMember = member.getLoginMember();
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
        createSingleMember();
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

    @Test
    @DisplayName("API - 내 매칭 파트너 조회")
    void getMyMatchingPartnersTest() throws Exception {
        // given
        operateMatching(MockTime.TUESDAY);
        Member targetMember = members.get(0);
        GIVEN_ACCESS_TOKEN = dummyGenerator.generateAccessToken(targetMember);

        // when & then
        mockMvc.perform(get(baseUrl + "/partners")
                        .header("Authorization", GIVEN_ACCESS_TOKEN))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("matching-process-get-partners",
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result[]").description("결과 배열"),
                                fieldWithPath("result[].matchedMemberId").description("매칭된 멤버 ID"),
                                fieldWithPath("result[].relationship").description("매칭 관계")
                        )));
    }

    private void createSingleMember() {
        member = dummyGenerator.generateSingleMember();
        GIVEN_ACCESS_TOKEN = dummyGenerator.generateAccessToken(member);
    }

    private void operateMatching(MockTime mockTime) {
        members = dummyGenerator.generateMultiMember(10);
        dummyGenerator.generateAppliersToMatch(10L);
        dummyGenerator.operateMatching(mockTime);
    }
}
