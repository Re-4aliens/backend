package com.aliens.backend.docs;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.matching.util.time.MockClock;
import com.aliens.backend.matching.util.time.MockTime;
import com.aliens.backend.mathcing.business.model.Language;
import com.aliens.backend.mathcing.controller.MatchingApplicationController;
import com.aliens.backend.mathcing.controller.MatchingProcessController;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MatchingRestDocsTest extends BaseRestDocsTest {
    @Autowired MockClock mockClock;
    @Autowired MatchingApplicationController matchingApplicationController;
    @Autowired MatchingProcessController matchingProcessController;
    @Autowired MatchingApplicationService matchingApplicationService;

    String baseUrl = "/matchings";
    MatchingApplicationRequest request = new MatchingApplicationRequest(Language.KOREAN, Language.ENGLISH);

    @BeforeEach
    void setUpMatch() {
        dummyGenerator.generateMatchingRound(MockTime.TUESDAY);
        mockClock.mockTime(MockTime.VALID_RECEPTION_TIME_ON_TUESDAY);
    }

    @Test
    @DisplayName("API - 매칭 신청")
    void applyMatchTest() throws Exception {
        // when & then
        mockMvc.perform(post(baseUrl + "/applications")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(document("matching-application-post",
                        requestFields(
                                fieldWithPath("firstPreferLanguage").description("1순위 선호 언어"),
                                fieldWithPath("secondPreferLanguage").description("2순위 선호 언어")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("매칭 신청 결과")
                        )));
    }

    @Test
    @DisplayName("API - 매칭 신청 내역 조회")
    void getMatchingApplicationTest() throws Exception {
        // given & when
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
        dummyGenerator.applySingleMemberToMatch(member, request);
        List<Member> members = dummyGenerator.generateMultiMember(10);
        dummyGenerator.generateAppliersToMatch(members);
        dummyGenerator.operateMatching();

        // when & then
        mockMvc.perform(get(baseUrl + "/partners")
                        .header("Authorization", GIVEN_ACCESS_TOKEN))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("matching-process-get-partners",
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result[]").description("결과 배열"),
                                fieldWithPath("result[].roomStatus").description("채팅 방 상태"),
                                fieldWithPath("result[].chatRoomId").description("채팅 방 ID"),
                                fieldWithPath("result[].partnerMemberId").description("파트너 ID"),
                                fieldWithPath("result[].name").description("파트너 이름"),
                                fieldWithPath("result[].mbti").description("파트너 mbti"),
                                fieldWithPath("result[].gender").description("파트너 성별"),
                                fieldWithPath("result[].nationality").description("파트너 국적"),
                                fieldWithPath("result[].profileImageUrl").description("파트너 프로필 이미지URL"),
                                fieldWithPath("result[].aboutMe").description("파트너 자기소개"),
                                fieldWithPath("result[].firstPreferLanguage").description("파트너 제 1 선호언어"),
                                fieldWithPath("result[].secondPreferLanguage").description("파트너 제 2 선호언어"),
                                fieldWithPath("result[].relation").description("매칭 관계")
                        )));
    }

    @Test
    @DisplayName("API - 매칭 시작 시간 조회")
    void getMatchingBeginTimeTest() throws Exception {
        mockMvc.perform(get(baseUrl + "/applications/begin-time"))
                .andExpect(status().is2xxSuccessful())
                .andDo(document("matching-begin-time-get",
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("매칭 시작 시간 조회 결과"),
                                fieldWithPath("result.round").description("매칭 회차"),
                                fieldWithPath("result.matchingBeginTime").description("매칭 시작 시간")
                        )));
    }

    @Test
    @DisplayName("API - 매칭 신청 정보 수정")
    void modifyMatchingApplicationTest() throws Exception {
        // given
        LoginMember loginMember = member.getLoginMember();
        matchingApplicationService.saveParticipant(loginMember, request);
        MatchingApplicationRequest modifyRequest = new MatchingApplicationRequest(Language.JAPANESE, Language.ENGLISH);

        // when & then
        mockMvc.perform(put(baseUrl + "/applications")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(modifyRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(document("matching-application-modify",
                        requestFields(
                                fieldWithPath("firstPreferLanguage").description("1순위 선호 언어"),
                                fieldWithPath("secondPreferLanguage").description("2순위 선호 언어")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("매칭 신청 정보 수정 결과")
                        )));
    }
}
