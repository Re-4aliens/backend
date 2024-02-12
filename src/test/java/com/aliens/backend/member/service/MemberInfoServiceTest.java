package com.aliens.backend.member.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.email.domain.EmailAuthentication;
import com.aliens.backend.email.domain.repository.EmailAuthenticationRepository;
import com.aliens.backend.global.BaseServiceTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.member.controller.dto.request.SignUpRequest;
import com.aliens.backend.member.controller.dto.request.TemporaryPasswordRequest;
import com.aliens.backend.member.controller.dto.response.MemberPageResponse;
import com.aliens.backend.member.controller.dto.response.MemberResponse;
import com.aliens.backend.member.domain.MemberStatus;
import com.aliens.backend.member.sevice.MemberInfoService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;


class MemberInfoServiceTest extends BaseServiceTest {

    @Autowired MemberInfoService memberInfoService;
    @Autowired EmailAuthenticationRepository emailRepository;
    @Autowired DummyGenerator dummyGenerator;

    MultipartFile file;
    LoginMember loginMember;

    @BeforeEach
    void setUp() {
        setUpEmailEntity(DummyGenerator.GIVEN_EMAIL);
        Member member = dummyGenerator.generateSingleMember();
        loginMember = member.getLoginMember();
        file = dummyGenerator.generateMultipartFile();
    }

    @Test
    @DisplayName("회원가입")
    void signUpTest() {
        //Given
        String expectedMessage = MemberResponse.SIGN_UP_SUCCESS.getMessage();
        SignUpRequest signUpRequest = createSignUpRequest();

        //When
        String result = memberInfoService.signUp(signUpRequest, file);

        //Then
        Assertions.assertEquals(expectedMessage, result);
    }

    @Test
    @DisplayName("회원 탈퇴")
    void withdrawTest() {
        //Given
        String expectedMessage = MemberResponse.WITHDRAW_SUCCESS.getMessage();

        //When
        String result  = memberInfoService.withdraw(loginMember);

        //Then
        Assertions.assertEquals(expectedMessage,result);
    }

    @Test
    @DisplayName("임시 비밀번호 발급")
    void temporaryPassword() {
        //Given
        String expectedMessage = MemberResponse.TEMPORARY_PASSWORD_GENERATED_SUCCESS.getMessage();
        TemporaryPasswordRequest request = new TemporaryPasswordRequest(
                DummyGenerator.GIVEN_EMAIL,
                DummyGenerator.GIVEN_NAME
        );

        //When
        String result = memberInfoService.generateTemporaryPassword(request);

        //Then
        Assertions.assertEquals(expectedMessage, result);
    }

    @Test
    @DisplayName("비밀번호 변경")
    void changePassword() {
        //Given
        String expectedMessage = MemberResponse.PASSWORD_CHANGE_SUCCESS.getMessage();
        String newPassword = "newPassword";

        //When
        String result = memberInfoService.changePassword(loginMember, newPassword);

        //Then
        Assertions.assertEquals(expectedMessage,result);
    }

    @Test
    @DisplayName("프로필 이미지 변경")
    void changeProfileImage() {
        //Given
        String expectedMessage = MemberResponse.PROFILE_IMAGE_CHANGE_SUCCESS.getMessage();

        //When
        String result = memberInfoService.changeProfileImage(loginMember, file);

        //Then
        Assertions.assertEquals(expectedMessage,result);
    }

    @Test
    @DisplayName("자기소개 변경")
    void changeAboutMe() {
        //Given
        String expectedMessage = MemberResponse.ABOUT_ME_CHANGE_SUCCESS.getMessage();
        String newAboutMe = "newAboutMe";

        //When
        String result = memberInfoService.changeAboutMe(loginMember, newAboutMe);

        //Then
        Assertions.assertEquals(expectedMessage,result);
    }

    @Test
    @DisplayName("MBTI 변경")
    void changeMBTI() {
        //Given
        String expectedMessage = MemberResponse.MBTI_CHANGE_SUCCESS.getMessage();
        String newMBTI = "ESTJ";

        //When
        String result = memberInfoService.changeMBTI(loginMember, newMBTI);

        //Then
        Assertions.assertEquals(expectedMessage,result);
    }

    @Test
    @DisplayName("상태 요청")
    void getStatus() {
        //Given
        String expectedMessage = MemberStatus.NOT_APPLIED_NOT_MATCHED.getMessage();

        //When
        String result = memberInfoService.getStatus(loginMember);

        //Then
        Assertions.assertEquals(expectedMessage, result);
    }

    @Test
    @DisplayName("회원 개인정보 요청")
    void getMemberInfo() {
        //Given
        MemberPageResponse expectedResponse = new MemberPageResponse(
                DummyGenerator.GIVEN_NAME,
                DummyGenerator.GIVEN_MBTI,
                DummyGenerator.GIVEN_GENDER,
                DummyGenerator.GIVEN_NATIONALITY,
                DummyGenerator.GIVEN_BIRTHDAY,
                DummyGenerator.GIVEN_ABOUT_ME,
                DummyGenerator.GIVEN_FILE_URL);

        //When
        MemberPageResponse result = memberInfoService.getMemberPage(loginMember);

        //Then
        Assertions.assertEquals(expectedResponse,result);
    }

    private void setUpEmailEntity(final String givenEmail) {
        EmailAuthentication emailEntity = new EmailAuthentication(givenEmail);
        emailEntity.authenticate();
        emailRepository.save(emailEntity);
    }

    private SignUpRequest createSignUpRequest() {
        SignUpRequest signUpRequest = new SignUpRequest(
                DummyGenerator.GIVEN_EMAIL,
                DummyGenerator.GIVEN_PASSWORD,
                DummyGenerator.GIVEN_NAME,
                DummyGenerator.GIVEN_MBTI,
                DummyGenerator.GIVEN_GENDER,
                DummyGenerator.GIVEN_NATIONALITY,
                DummyGenerator.GIVEN_BIRTHDAY,
                DummyGenerator.GIVEN_ABOUT_ME);
        return signUpRequest;
    }
}
