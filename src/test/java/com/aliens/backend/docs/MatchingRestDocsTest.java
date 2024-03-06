package com.aliens.backend.matching.controller;

import com.aliens.backend.docs.BaseRestDocsTest;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.MatchingSuccess;
import com.aliens.backend.mathcing.service.model.Language;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static com.aliens.backend.mathcing.controller.dto.input.MatchingInput.MatchingApplicationInput;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class MatchingRestDocsTest extends BaseRestDocsTest {

    @Test
    @DisplayName("API - 매칭 신청")
    void applyMatchApiTest() throws Exception {
        MatchingApplicationInput request  = MatchingApplicationInput.of(Language.KOREAN, Language.ENGLISH);
        String message = MatchingSuccess.APPLY_MATCHING_SUCCESS.getMessage();
        SuccessResponse<String> response = SuccessResponse.of(MatchingSuccess.APPLY_MATCHING_SUCCESS, message);
        doReturn(response).when(matchingController).applyMatch(any(), any());


        mockMvc.perform(post("/matchings/applications")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(document("post-matching-application",
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("매칭 신청 결과")
                        )
                ));
    }
}
