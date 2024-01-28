package com.aliens.backend.docs;

import com.aliens.backend.auth.service.TokenProvider;
import com.aliens.backend.member.controller.dto.request.SignUpRequest;
import com.aliens.backend.member.controller.dto.request.TemporaryPasswordRequest;
import com.aliens.backend.member.controller.dto.response.MemberPageResponse;
import com.aliens.backend.member.controller.dto.response.MemberResponse;
import com.aliens.backend.member.domain.MemberStatus;
import com.aliens.backend.member.sevice.MemberInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
class MemberDocTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberInfoService memberInfoService;
    @MockBean
    private TokenProvider tokenProvider;

    ObjectMapper objectMapper;
    MockMultipartFile multipartFile;
    String accessToken;

    @BeforeEach
    void setUp() {
        multipartFile = new MockMultipartFile("profile-data",
                "profile-data",
                "image/png",
                "test data".getBytes());
        accessToken= "accessToken";
        objectMapper = new ObjectMapper();
    }

    @Test
    @DisplayName("회원가입 API 테스트")
    void signUp() throws Exception {
        final SignUpRequest request = createSignUpRequest();
        final String response = MemberResponse.SIGN_UP_SUCCESS.getMessage();
        when(memberInfoService.signUp(any(),any())).thenReturn(response);

        MockMultipartFile signUpRequestFile = getSignUpRequestFile(request);

        // When and Then
        mockMvc.perform(multipart("/members")
                        .file(signUpRequestFile)
                        .file("profileImage", multipartFile.getBytes())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
                )
                .andExpect(status().isOk())
                .andDo(document("member-signup",
                        requestParts(
                                partWithName("signUpRequest").description("회원가입 요청 데이터"),
                                partWithName("profileImage").description("프로필 이미지 파일")
                        ),
                        responseFields(
                                fieldWithPath("response").description("회원가입 결과")
                        )
                ));
    }

    @Test
    @DisplayName("프로필 이미지 변경 API 테스트")
    void changeProfileImage() throws Exception {
        String response = MemberResponse.PROFILE_IMAGE_CHANGE_SUCCESS.getMessage();
        when(memberInfoService.changeProfileImage(any(),any())).thenReturn(response);

        mockMvc.perform(multipart("/members/profile-image")
                        .file("newProfileImage", multipartFile.getBytes())
                        .header("Authorization", accessToken)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andDo(document("member-change-profile-image",
                        requestParts(
                                partWithName("newProfileImage").description("새로운 프로필 이미지 파일")
                        ),
                        responseFields(
                                fieldWithPath("response").description("프로필 이미지 변경 결과")
                        )
                ));
    }

    @Test
    @DisplayName("임시 비밀번호 발급 API 테스트")
    void generateTemporaryPassword() throws Exception {
        final TemporaryPasswordRequest request = new TemporaryPasswordRequest("tmp@example.com", "tmpName");
        final String response = MemberResponse.TEMPORARY_PASSWORD_GENERATED_SUCCESS.getMessage();
        when(memberInfoService.generateTemporaryPassword(any())).thenReturn(response);

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
                                fieldWithPath("response").description("임시 비밀번호 발급 결과")
                        )
                ));
    }

    @Test
    @DisplayName("비밀번호 변경 API 테스트")
    void changePassword() throws Exception {
        String newPassword = "newPassword";
        String response = MemberResponse.PASSWORD_CHANGE_SUCCESS.getMessage();
        when(memberInfoService.changePassword(any(), any())).thenReturn(response);

        this.mockMvc.perform(patch("/members/password")
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(Collections.singletonMap("newPassword", newPassword)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member-change-password",
                        requestFields(
                                fieldWithPath("newPassword").description("새로운 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("response").description("비밀번호 변경 결과")
                        )
                ));
    }

    @Test
    @DisplayName("회원 탈퇴 API 테스트")
    void withdraw() throws Exception {
        String response = MemberResponse.WITHDRAW_SUCCESS.getMessage();
        when(memberInfoService.withdraw(any())).thenReturn(response);

        mockMvc.perform(patch("/members/withdraw")
                        .header("Authorization", accessToken)
                )
                .andExpect(status().isOk())
                .andDo(document("member-withdraw",
                        responseFields(
                                fieldWithPath("response").description("회원 탈퇴 결과")
                        )
                ));
    }

    @Test
    @DisplayName("상태 요청 API 테스트")
    void getStatus() throws Exception {
        String response = MemberStatus.NOT_APPLIED_NOT_MATCHED.getMessage();
        when(memberInfoService.getStatus(any())).thenReturn(response);

        mockMvc.perform(get("/members/status")
                        .header("Authorization", accessToken)
                )
                .andExpect(status().isOk())
                .andDo(document("member-get-status",
                        responseFields(
                                fieldWithPath("response").description("상태 요청 결과")
                        )
                ));
    }

    @Test
    @DisplayName("회원 개인정보 요청 API 테스트")
    void getMemberPage() throws Exception {
        MemberPageResponse response = getMemberPageResponse();
        when(memberInfoService.getMemberPage(any())).thenReturn(response);

        mockMvc.perform(get("/members")
                        .header("Authorization", accessToken)
                )
                .andExpect(status().isOk())
                .andDo(document("member-get-member-page",
                        responseFields(
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("mbti").description("MBTI"),
                                fieldWithPath("gender").description("성별"),
                                fieldWithPath("nationality").description("국적"),
                                fieldWithPath("birthday").description("생년월일"),
                                fieldWithPath("selfIntroduction").description("자기 소개"),
                                fieldWithPath("profileImageURL").description("프로필 이미지 주소")
                        )
                ));
    }


    @Test
    @DisplayName("자기소개 변경 API 테스트")
    void changeAboutMe() throws Exception {
        String newAboutMe = "새로운 자기소개";
        String response = MemberResponse.PASSWORD_CHANGE_SUCCESS.getMessage();
        when(memberInfoService.changeAboutMe(any(),any())).thenReturn(response);

        mockMvc.perform(patch("/members/about-me")
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(Collections.singletonMap("newAboutMe", newAboutMe)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member-change-about-me",
                        requestFields(
                                fieldWithPath("newAboutMe").description("새로운 자기소개")
                        ),
                        responseFields(
                                fieldWithPath("response").description("자기소개 변경 결과")
                        )
                ));
    }

    @Test
    @DisplayName("MBTI 변경 API 테스트")
    void changeMBTI() throws Exception {
        String newMBTI = "ENFP";
        String response = MemberResponse.MBTI_CHANGE_SUCCESS.getMessage();
        when(memberInfoService.changeMBTI(any(),any())).thenReturn(response);

        mockMvc.perform(patch("/members/mbti")
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(Collections.singletonMap("newMBTI", newMBTI)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("member-change-mbti",
                        requestFields(
                                fieldWithPath("newMBTI").description("새로운 MBTI")
                        ),
                        responseFields(
                                fieldWithPath("response").description("MBTI 변경 결과")
                        )
                ));
    }

    public SignUpRequest createSignUpRequest() {
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

    public MockMultipartFile getSignUpRequestFile(final SignUpRequest request) throws JsonProcessingException {
        String signupRequestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile signUpRequestFile = new MockMultipartFile(
                "signUpRequest",
                "signupRequest.json",
                MediaType.APPLICATION_JSON_VALUE,
                signupRequestJson.getBytes(StandardCharsets.UTF_8)
        );
        return signUpRequestFile;
    }

    public MemberPageResponse getMemberPageResponse() {
        MemberPageResponse response = new MemberPageResponse("name","MBTI","MALE","KOREA","1998-11-25","반갑습니다","tmpImage.com");
        return response;
    }
}