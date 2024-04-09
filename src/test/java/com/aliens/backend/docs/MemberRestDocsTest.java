package com.aliens.backend.docs;

import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.MemberSuccess;
import com.aliens.backend.member.controller.dto.request.SignUpRequest;
import com.aliens.backend.member.controller.dto.request.TemporaryPasswordRequest;
import com.aliens.backend.member.controller.dto.response.MemberPageResponse;
import com.aliens.backend.member.controller.dto.response.MemberResponse;
import com.aliens.backend.member.domain.MatchingStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class MemberRestDocsTest extends BaseRestDocsTest {

    @Test
    @DisplayName("API - 회원가입")
    void signUp() throws Exception {
        final SignUpRequest request = createSignUpRequest();

        final String message = MemberResponse.SIGN_UP_SUCCESS.getMessage();
        SuccessResponse<String> response = SuccessResponse.of(MemberSuccess.SIGN_UP_SUCCESS, message);
        MockMultipartFile multipartFile = createMultipartFile();

        doReturn(response).when(memberController).signUp(any(), any());

        // When and Then
        mockMvc.perform(multipart("/members")
                        .file("profileImage", multipartFile.getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA)

                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is2xxSuccessful())
                .andDo(document("member-signup",
                        requestParts(
                                partWithName("profileImage").description("프로필 이미지 파일")
                        ),
                        requestFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("mbti").description("MBTI"),
                                fieldWithPath("gender").description("성별"),
                                fieldWithPath("nationality").description("국적"),
                                fieldWithPath("birthday").description("생년월일"),
                                fieldWithPath("aboutMe").description("자기소개")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("회원가입 결과")
                        )
                ));
    }

    @Test
    @DisplayName("API - 프로필 이미지 변경")
    void changeProfileImage() throws Exception {
        String message = MemberResponse.PROFILE_IMAGE_CHANGE_SUCCESS.getMessage();
        SuccessResponse<String> response = SuccessResponse.of(MemberSuccess.PROFILE_IMAGE_CHANGE_SUCCESS, message);
        MultipartFile multipartFile = createMultipartFile();
        doReturn(response).when(memberController).changeProfileImage(any(), any());

        // When and Then
        mockMvc.perform(multipart("/members/profile-image")
                        .file("newProfileImage", multipartFile.getBytes())
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andDo(document("member-change-profile-image",
                        requestParts(
                                partWithName("newProfileImage").description("새로운 프로필 이미지 파일")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("프로필 이미지 변경 결과")
                        )
                ));
    }

    @Test
    @DisplayName("API - 임시 비밀번호 발급")
    void generateTemporaryPassword() throws Exception {
        final TemporaryPasswordRequest request = new TemporaryPasswordRequest("tmp@example.com", "tmpName");
        final String message = MemberResponse.TEMPORARY_PASSWORD_GENERATED_SUCCESS.getMessage();
        SuccessResponse<String> response = SuccessResponse.of(MemberSuccess.TEMPORARY_PASSWORD_GENERATED_SUCCESS, message);

        doReturn(response).when(memberController).temporaryPassword(any());

        // When and Then
        this.mockMvc.perform(post("/members/temporary-password")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member-temporary-password",
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("name").description("이름")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("임시 비밀번호 발급 결과")
                        )
                ));
    }

    @Test
    @DisplayName("API - 비밀번호 변경")
    void changePassword() throws Exception {
        String newPassword = "newPassword";
        String message = MemberResponse.PASSWORD_CHANGE_SUCCESS.getMessage();
        SuccessResponse<String> response = SuccessResponse.of(MemberSuccess.PASSWORD_CHANGE_SUCCESS, message);

        doReturn(response).when(memberController).changePassword(any(), any());

        // When and Then
        this.mockMvc.perform(patch("/members/password")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(Collections.singletonMap("newPassword", newPassword)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member-change-password",
                        requestFields(
                                fieldWithPath("newPassword").description("새로운 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("비밀번호 변경 결과")
                        )
                ));
    }

    @Test
    @DisplayName("API - 회원 탈퇴")
    void withdraw() throws Exception {
        String message = MemberResponse.WITHDRAW_SUCCESS.getMessage();
        SuccessResponse<String> response = SuccessResponse.of(MemberSuccess.WITHDRAW_SUCCESS, message);
        doReturn(response).when(memberController).withdraw(any());

        // When and Then
        mockMvc.perform(patch("/members/withdraw")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                )
                .andExpect(status().isOk())
                .andDo(document("member-withdraw",
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("회원 탈퇴 결과")
                        )
                ));
    }

    @Test
    @DisplayName("API - 상태 요청")
    void getStatus() throws Exception {
        String message = MatchingStatus.NOT_APPLIED_NOT_MATCHED.getMessage();
        SuccessResponse<String> response = SuccessResponse.of(MemberSuccess.GET_MEMBER_MATCHING_STATUS_SUCCESS, message);
        doReturn(response).when(memberController).getStatus(any());

        // When and Then
        mockMvc.perform(get("/members/status")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                )
                .andExpect(status().isOk())
                .andDo(document("member-get-status",
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("상태 요청 결과")
                        )
                ));
    }

    @Test
    @DisplayName("API - 회원 개인정보 요청")
    void getMemberPage() throws Exception {
        MemberPageResponse result = getMemberPageResponse();
        SuccessResponse<MemberPageResponse> response = SuccessResponse.of(MemberSuccess.GET_MEMBER_MATCHING_STATUS_SUCCESS, result);
        doReturn(response).when(memberController).getMemberPage(any());

        // When and Then
        mockMvc.perform(get("/members")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                )
                .andExpect(status().isOk())
                .andDo(document("member-get-member-page",
                        responseFields(
                                fieldWithPath("code").description("응답 코드"),
                                subsectionWithPath("result").description("응답 객체"),
                                fieldWithPath("result.name").description("이름"),
                                fieldWithPath("result.mbti").description("MBTI"),
                                fieldWithPath("result.gender").description("성별"),
                                fieldWithPath("result.nationality").description("국적"),
                                fieldWithPath("result.birthday").description("생년월일"),
                                fieldWithPath("result.selfIntroduction").description("자기 소개"),
                                fieldWithPath("result.profileImageURL").description("프로필 이미지 주소")
                        )
                ));
    }

    @Test
    @DisplayName("API - 자기소개 변경")
    void changeAboutMe() throws Exception {
        String newAboutMe = "새로운 자기소개";
        String message = MemberResponse.PASSWORD_CHANGE_SUCCESS.getMessage();
        SuccessResponse<String> response = SuccessResponse.of(MemberSuccess.PASSWORD_CHANGE_SUCCESS, message);
        doReturn(response).when(memberController).changeAboutMe(any(),any());

        // When and Then
        mockMvc.perform(patch("/members/about-me")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(Collections.singletonMap("newAboutMe", newAboutMe)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member-change-about-me",
                        requestFields(
                                fieldWithPath("newAboutMe").description("새로운 자기소개")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("자기소개 변경 결과")
                        )
                ));
    }

    @Test
    @DisplayName("API - MBTI 변경")
    void changeMBTI() throws Exception {
        String newMBTI = "ENFP";
        String message = MemberResponse.MBTI_CHANGE_SUCCESS.getMessage();

        SuccessResponse<String> response = SuccessResponse.of(MemberSuccess.PASSWORD_CHANGE_SUCCESS, message);
        doReturn(response).when(memberController).changeMBTI(any(),any());

        // When and Then
        mockMvc.perform(patch("/members/mbti")
                        .header("Authorization", GIVEN_ACCESS_TOKEN)
                        .content(objectMapper.writeValueAsString(Collections.singletonMap("newMBTI", newMBTI)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member-change-mbti",
                        requestFields(
                                fieldWithPath("newMBTI").description("새로운 MBTI")
                        ),
                        responseFields(
                                fieldWithPath("code").description("성공 코드"),
                                fieldWithPath("result").description("MBTI 변경 결과")
                        )
                ));
    }

    private SignUpRequest createSignUpRequest() {
        SignUpRequest signUpRequest = new SignUpRequest("tmp@example.com",
                "password",
                "tmpName",
                "INTJ",
                "MALE",
                "KOREA",
                "1998-11-25",
                "반갑습니다"
        );
        return signUpRequest;
    }

    private MockMultipartFile getSignUpRequestFile(final SignUpRequest request) throws JsonProcessingException {
        String signupRequestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile signUpRequestFile = new MockMultipartFile(
                "signUpRequest",
                "signupRequest.json",
                MediaType.APPLICATION_JSON_VALUE,
                signupRequestJson.getBytes(StandardCharsets.UTF_8)
        );
        return signUpRequestFile;
    }

    private MemberPageResponse getMemberPageResponse() {
        MemberPageResponse response = new MemberPageResponse("name","MBTI","MALE","KOREA","1998-11-25","반갑습니다","tmpImage.com");
        return response;
    }

    private MockMultipartFile createMultipartFile() {
        return new MockMultipartFile("profile-data",
                "profile-data",
                "image/png",
                "test data".getBytes());
    }
}