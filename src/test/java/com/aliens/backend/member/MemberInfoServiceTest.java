package com.aliens.backend.member;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.auth.service.PasswordEncoder;
import com.aliens.backend.email.domain.EmailAuthentication;
import com.aliens.backend.email.domain.repository.EmailAuthenticationRepository;
import com.aliens.backend.global.BaseTest;
import com.aliens.backend.member.sevice.SymmetricKeyEncoder;
import com.aliens.backend.member.controller.dto.*;
import com.aliens.backend.member.controller.dto.request.SignUpRequest;
import com.aliens.backend.member.controller.dto.request.TemporaryPasswordRequest;
import com.aliens.backend.member.controller.dto.response.MemberPageResponse;
import com.aliens.backend.member.controller.dto.response.MemberResponse;
import com.aliens.backend.member.domain.Image;
import com.aliens.backend.member.domain.MemberInfo;
import com.aliens.backend.member.domain.repository.ImageRepository;
import com.aliens.backend.member.domain.repository.MemberInfoRepository;
import com.aliens.backend.member.domain.MemberStatus;
import com.aliens.backend.member.sevice.MemberInfoService;
import com.aliens.backend.uploader.dto.S3File;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;


@SpringBootTest
class MemberInfoServiceTest extends BaseTest {

    @Autowired
    MemberInfoService memberInfoService;
    @Autowired
    MemberInfoRepository memberInfoRepository;
    @Autowired
    EmailAuthenticationRepository emailAuthenticationRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    SymmetricKeyEncoder symmetricKeyEncoder;
    @Autowired
    PasswordEncoder passwordEncoder;

    MultipartFile file;
    LoginMember loginMember;
    String givenName = "김명준";
    String givenEmail = "tmp@example";
    String givenPassword = "tmpPassword";
    String givenGender = "MALE";
    String givenMbti = "INTJ";
    String givenBirthday = "1998-11-25";
    String givenNationality = "KOREA";
    String givenAboutMe = "반갑습니다.";
    String givenFileName = "tmpFile";
    String givenFileURL = "example.com";

    @BeforeEach
    void setUp() {
        setUpEmailEntity(givenEmail);
        Image image = setUpImage();
        Member member = setUpMember(image);
        setUpMemberInfo(member);
        setUpLoginMember(member);
        file = setUpMockMultipartFile();
    }

    @Test
    @DisplayName("회원가입")
    void signUpTest() {
        //Given
        SignUpRequest signUpRequest = createSignUpRequest();

        //When
        String result = memberInfoService.signUp(signUpRequest, file);

        //Then
        Assertions.assertEquals(MemberResponse.SIGN_UP_SUCCESS.getMessage(), result);
    }

    @Test
    @DisplayName("회원 탈퇴")
    void withdrawTest() {
        //When
        String result  = memberInfoService.withdraw(loginMember);

        //Then
        Assertions.assertEquals(MemberResponse.WITHDRAW_SUCCESS.getMessage(),result);
    }

    @Test
    @DisplayName("임시 비밀번호 발급")
    void temporaryPassword() {
        //Given
        TemporaryPasswordRequest temporaryPasswordRequest = new TemporaryPasswordRequest(givenEmail,givenName);

        //When
        String result = memberInfoService.generateTemporaryPassword(temporaryPasswordRequest);

        //Then
        Assertions.assertEquals(MemberResponse.TEMPORARY_PASSWORD_GENERATED_SUCCESS.getMessage(),result);
    }

    @Test
    @DisplayName("비밀번호 변경")
    void changePassword() {
        //Given
        String newPassword = "newPassword";

        //When
        String result = memberInfoService.changePassword(loginMember, newPassword);

        //Then
        Assertions.assertEquals(MemberResponse.PASSWORD_CHANGE_SUCCESS.getMessage(),result);
    }

    @Test
    @DisplayName("프로필 이미지 변경")
    void changeProfileImage() {
        //When
        String result = memberInfoService.changeProfileImage(loginMember, file);

        //Then
        Assertions.assertEquals(MemberResponse.PROFILE_IMAGE_CHANGE_SUCCESS.getMessage(),result);
    }

    @Test
    @DisplayName("자기소개 변경")
    void changeAboutMe() {
        //Given
        String newAboutMe = "newAboutMe";

        //When
        String result = memberInfoService.changeAboutMe(loginMember, newAboutMe);

        //Then
        Assertions.assertEquals(MemberResponse.ABOUT_ME_CHANGE_SUCCESS.getMessage(),result);
    }

    @Test
    @DisplayName("MBTI 변경")
    void changeMBTI() {
        //Given
        String newMBTI = "ESTJ";

        //When
        String result = memberInfoService.changeMBTI(loginMember, newMBTI);

        //Then
        Assertions.assertEquals(MemberResponse.MBTI_CHANGE_SUCCESS.getMessage(),result);
    }

    @Test
    @DisplayName("상태 요청")
    void getStatus() {
        //When
        String result = memberInfoService.getStatus(loginMember);

        //Then
        Assertions.assertEquals(MemberStatus.NOT_APPLIED_NOT_MATCHED.getMessage(),result);
    }

    @Test
    @DisplayName("회원 개인정보 요청")
    void getMemberInfo() {
        //Given
        MemberPageResponse expectedResponse = new MemberPageResponse(givenName,
                givenMbti,
                givenGender,
                givenNationality,
                givenBirthday,
                givenAboutMe,
                givenFileURL);

        //When
        MemberPageResponse result = memberInfoService.getMemberPage(loginMember);

        //Then
        Assertions.assertEquals(expectedResponse,result);
    }

    private void setUpLoginMember(final Member member) {
        loginMember = member.getLoginMember();
    }

    private void setUpMemberInfo(final Member member) {
        EncodedMember encodedRequest = new EncodedMember(symmetricKeyEncoder.encrypt(givenGender),
                symmetricKeyEncoder.encrypt(givenMbti),
                symmetricKeyEncoder.encrypt(givenBirthday),
                symmetricKeyEncoder.encrypt(givenNationality),
                symmetricKeyEncoder.encrypt(givenAboutMe));

        MemberInfo memberInfo = MemberInfo.of(encodedRequest, member);
        memberInfoRepository.save(memberInfo);
    }

    private Member setUpMember(final Image image) {
        EncodedSignUp signUp = new EncodedSignUp(givenName, givenEmail, passwordEncoder.encrypt(givenPassword));
        Member member = Member.of(signUp, image);
        return member;
    }

    private Image setUpImage() {
        Image image = Image.from(new S3File(givenFileName, givenFileURL));
        return image;
    }

    private MultipartFile setUpMockMultipartFile() {
        String fileName = "test";
        String path = "/test";
        String contentType = "image/png";
        byte[] content = fileName.getBytes();
        return new MockMultipartFile(fileName, path, contentType, content);
    }

    private void setUpEmailEntity(final String givenEmail) {
        EmailAuthentication emailEntity = new EmailAuthentication(givenEmail);
        emailEntity.authenticate();
        emailAuthenticationRepository.save(emailEntity);
    }

    private SignUpRequest createSignUpRequest() {
        SignUpRequest signUpRequest = new SignUpRequest(givenEmail,
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
}
