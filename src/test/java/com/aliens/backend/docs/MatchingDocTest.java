package com.aliens.backend.docs;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.global.BaseServiceTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.MatchingSuccess;
import com.aliens.backend.mathcing.business.model.Language;
import com.aliens.backend.mathcing.controller.MatchingApplicationController;
import com.aliens.backend.mathcing.controller.MatchingProcessController;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
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

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class MatchingDocTest extends BaseServiceTest {

    @Autowired MockMvc mockMvc;
    @SpyBean MatchingApplicationController matchingApplicationController;
    @SpyBean MatchingProcessController matchingProcessController;
    @Autowired DummyGenerator dummyGenerator;

    ObjectMapper objectMapper = new ObjectMapper();
    String GIVEN_ACCESS_TOKEN;

    @BeforeEach
    void setUp() {
        dummyGenerator.generateMatchingRound();
        Member member = dummyGenerator.generateSingleMember();
        GIVEN_ACCESS_TOKEN = dummyGenerator.generateAccessToken(member);
    }

    @Test
    @DisplayName("API - 매칭 신청")
    void applyMatchApiTest() throws Exception {
        MatchingApplicationRequest request  = new MatchingApplicationRequest(Language.KOREAN, Language.ENGLISH);

        mockMvc.perform(post("/matchings/applications")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(document("matching-application-post",
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("매칭 신청 결과")
                        )
                ));
    }
}
