package com.aliens.backend.member.sevice;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.auth.service.PasswordEncoder;
import com.aliens.backend.email.domain.EmailAuthentication;
import com.aliens.backend.email.domain.repository.EmailAuthenticationRepository;
import com.aliens.backend.global.encode.SymmetricKeyEncoder;
import com.aliens.backend.global.error.EmailError;
import com.aliens.backend.global.error.MemberError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.S3UploadProperties;
import com.aliens.backend.member.controller.dto.*;
import com.aliens.backend.member.controller.dto.event.TemporaryPasswordEvent;
import com.aliens.backend.member.controller.dto.request.TemporaryPasswordRequest;
import com.aliens.backend.member.controller.dto.response.MemberPageResponse;
import com.aliens.backend.member.controller.dto.response.MemberResponse;
import com.aliens.backend.member.controller.dto.request.SignUpRequest;
import com.aliens.backend.member.domain.*;
import com.aliens.backend.member.domain.repository.MemberInfoRepository;
import com.aliens.backend.uploader.AwsS3Uploader;
import com.aliens.backend.uploader.S3File;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class MemberInfoService {
    private final AwsS3Uploader uploader;
    private final SymmetricKeyEncoder symmetricKeyEncoder;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher publisher;

    private final S3UploadProperties s3UploadProperties;
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final EmailAuthenticationRepository emailAuthenticationRepository;

    public MemberInfoService(final AwsS3Uploader uploader,
                             final SymmetricKeyEncoder symmetricKeyEncoder,
                             final PasswordEncoder passwordEncoder,
                             final ApplicationEventPublisher publisher,
                             final S3UploadProperties s3UploadProperties, final MemberRepository memberRepository,
                             final MemberInfoRepository memberInfoRepository,
                             final EmailAuthenticationRepository emailAuthenticationRepository)
    {
        this.uploader = uploader;
        this.symmetricKeyEncoder = symmetricKeyEncoder;
        this.passwordEncoder = passwordEncoder;
        this.s3UploadProperties = s3UploadProperties;
        this.memberRepository = memberRepository;
        this.memberInfoRepository = memberInfoRepository;
        this.emailAuthenticationRepository = emailAuthenticationRepository;
        this.publisher = publisher;
    }

    @Transactional
    public String signUp(final SignUpRequest request, final MultipartFile profileImage) {
        checkEmail(request.email());

        Image image = saveImage(profileImage);

        Member member = getMemberEntity(request, image);

        MemberInfo memberInfo = getMemberInfoEntity(request, member);
        member.putMemberInfo(memberInfo);

        memberInfoRepository.save(memberInfo);

        return MemberResponse.SIGN_UP_SUCCESS.getMessage();
    }

    private void checkEmail(final String email) {
        Optional<EmailAuthentication> emailEntity = emailAuthenticationRepository.findByEmail(email);
        if (emailEntity.isEmpty()) {
            throw new RestApiException(EmailError.NULL_EMAIL);
        }
        if (!emailEntity.get().isAuthenticated()) {
            throw new RestApiException(EmailError.NOT_AUTHENTICATED_EMAIL);
        }
    }

    private Image saveImage(final MultipartFile profileImage) {
        if (profileImage == null || profileImage.isEmpty()) {
            return Image.from(new S3File(s3UploadProperties.getDefaultFileName(),
                    s3UploadProperties.getDefaultFileURL()));
        }

        S3File uploadedFile = uploader.upload(profileImage);
        return Image.from(uploadedFile);
    }

    private Member getMemberEntity(final SignUpRequest request, final Image image) {
        EncodedSignUp encodedSignUp = encodeForMember(request);
        return Member.of(encodedSignUp, image);
    }

    private EncodedSignUp encodeForMember(final SignUpRequest request) {
        return new EncodedSignUp(request.name(), request.email(), passwordEncoder.encrypt(request.password()));
    }

    private MemberInfo getMemberInfoEntity(final SignUpRequest request, final Member member) {
        EncodedMember encodedInfo = encodeForMemberInfo(request);
        return MemberInfo.of(encodedInfo, member);
    }

    private EncodedMember encodeForMemberInfo(final SignUpRequest request) {
        return new EncodedMember(
                symmetricKeyEncoder.encrypt(request.gender()),
                symmetricKeyEncoder.encrypt(request.mbti()),
                symmetricKeyEncoder.encrypt(request.birthday()),
                symmetricKeyEncoder.encrypt(request.nationality()),
                symmetricKeyEncoder.encrypt(request.aboutMe()));
    }

    @Transactional
    public String withdraw(final LoginMember loginMember) {
        Member member = getMember(loginMember);
        member.withdraw();
        return MemberResponse.WITHDRAW_SUCCESS.getMessage();
    }

    private Member getMember(final LoginMember loginMember) {
        return memberRepository.findById(loginMember.memberId()).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }

    @Transactional
    public String changePassword(final LoginMember loginMember, final String newPassword) {
        Member member = getMember(loginMember);
        member.changePassword(passwordEncoder.encrypt(newPassword));
        return MemberResponse.PASSWORD_CHANGE_SUCCESS.getMessage();
    }

    @Transactional
    public String changeProfileImage(final LoginMember loginMember, final MultipartFile newProfileImage) {
        Member member = getMember(loginMember);
        changeImageInDB(newProfileImage, member);
        return MemberResponse.PROFILE_IMAGE_CHANGE_SUCCESS.getMessage();
    }

    private void changeImageInDB(final MultipartFile newProfileImage, final Member member) {
        String savedProfileName = member.getProfileName();

        if (!savedProfileName.equals(s3UploadProperties.getDefaultFileName())) {
            uploader.delete(savedProfileName);
        }

        S3File newFile = uploader.upload(newProfileImage);
        member.changeProfileImage(newFile);
    }

    @Transactional
    public String changeAboutMe(final LoginMember loginMember, final String newAboutMe) {
        MemberInfo memberInfo = getMemberInfo(loginMember);
        memberInfo.changeAboutMe(symmetricKeyEncoder.encrypt(newAboutMe));
        return MemberResponse.ABOUT_ME_CHANGE_SUCCESS.getMessage();
    }

    @Transactional
    public String changeMBTI(final LoginMember loginMember, final String newMBTI) {
        MemberInfo memberInfo = getMemberInfo(loginMember);
        memberInfo.changeMBTI(symmetricKeyEncoder.encrypt(newMBTI));
        return MemberResponse.MBTI_CHANGE_SUCCESS.getMessage();
    }

    private MemberInfo getMemberInfo(final LoginMember loginMember) {
        return memberInfoRepository.findById(loginMember.memberId()).orElseThrow(()-> new RestApiException(MemberError.NULL_MEMBER));
    }

    @Transactional(readOnly = true)
    public String getStatus(final LoginMember loginMember) {
        Member member = getMember(loginMember);
        return member.getStatus();
    }

    @Transactional(readOnly = true)
    public MemberPageResponse getMemberPage(final LoginMember loginMember) {
        MemberInfo memberInfo = getMemberInfo(loginMember);
        EncodedMemberPage encodedMemberPage = memberInfo.getMemberPage();

        Member member = getMember(loginMember);
        MemberPage memberPage = member.getMemberPage();

        return new MemberPageResponse(memberPage.name(),
                symmetricKeyEncoder.decrypt(encodedMemberPage.mbti()),
                symmetricKeyEncoder.decrypt(encodedMemberPage.gender()),
                symmetricKeyEncoder.decrypt(encodedMemberPage.nationality()),
                symmetricKeyEncoder.decrypt(encodedMemberPage.birthday()),
                symmetricKeyEncoder.decrypt(encodedMemberPage.aboutMe()),
                memberPage.profileImageURL());
    }

    @Transactional
    public String generateTemporaryPassword(final TemporaryPasswordRequest request) {
        Member member = memberRepository.findByEmailAndName(request.email(), request.name()).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));

        String tmpPassword = getRandomPassword();
        member.changePassword(passwordEncoder.encrypt(tmpPassword));

        TemporaryPasswordEvent event = new TemporaryPasswordEvent(request.email(),tmpPassword);
        publisher.publishEvent(event);

        return MemberResponse.TEMPORARY_PASSWORD_GENERATED_SUCCESS.getMessage();
    }

    private String getRandomPassword() {
        Random random = new Random();
        List<Integer> password = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            int ranNum = random.nextInt(45) + 1;
            password.add(ranNum);
        }

        StringBuilder passwordBuilder = new StringBuilder();
        for (Integer num : password) {
            passwordBuilder.append(num);
        }

        return passwordBuilder.toString();
    }
}
