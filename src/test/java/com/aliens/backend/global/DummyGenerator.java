package com.aliens.backend.global;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.service.PasswordEncoder;
import com.aliens.backend.auth.service.TokenProvider;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.property.MatchingTimeProperties;
import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.matching.util.time.MockClock;
import com.aliens.backend.matching.util.time.MockTime;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import com.aliens.backend.mathcing.business.model.Language;
import com.aliens.backend.mathcing.service.MatchingProcessService;
import com.aliens.backend.member.controller.dto.EncodedMember;
import com.aliens.backend.member.controller.dto.EncodedSignUp;
import com.aliens.backend.member.domain.Image;
import com.aliens.backend.member.domain.MemberInfo;
import com.aliens.backend.member.domain.repository.MemberInfoRepository;
import com.aliens.backend.member.sevice.SymmetricKeyEncoder;
import com.aliens.backend.uploader.dto.S3File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DummyGenerator {
    @Autowired MatchingApplicationService matchingApplicationService;
    @Autowired MemberInfoRepository memberInfoRepository;
    @Autowired MatchingRoundRepository matchingRoundRepository;
    @Autowired MatchingTimeProperties matchingTimeProperties;
    @Autowired MatchingProcessService matchingProcessService;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired SymmetricKeyEncoder encoder;
    @Autowired TokenProvider tokenProvider;
    @Autowired MockClock mockClock;

    public static final String GIVEN_EMAIL = "tmp@example.com";
    public static final String GIVEN_PASSWORD = "tmpPassword";
    public static final String GIVEN_NAME = "tmpName";
    public static final String GIVEN_MBTI = "tmpMbTI";
    public static final String GIVEN_GENDER = "MALE";
    public static final String GIVEN_NATIONALITY = "KOREA";
    public static final String GIVEN_BIRTHDAY = "1998-11-25";
    public static final String GIVEN_ABOUT_ME = "nice to meet you";
    public static final String GIVEN_FILE_NAME = "test";
    public static final String GIVEN_FILE_URL = "/test";

    // 다수 멤버 생성 메서드
    public List<Member> generateMultiMember(Integer memberCounts) {
        ArrayList<Member> result = new ArrayList<>();

        for (int i = 0; i < memberCounts; i++) {
            String tmpEmail = GIVEN_EMAIL + i;
            Image image = makeImage();
            Member member = makeMember(tmpEmail, image);
            result.add(member);
        }

        result.forEach(this::saveAsMemberInfo);
        return result;
    }

    // 단일 멤버 생성 메서드
    public Member generateSingleMember() {
        String tmpEmail = GIVEN_EMAIL;
        Image image = makeImage();
        Member member = makeMember(tmpEmail, image);
        saveAsMemberInfo(member);
        return member;
    }

    // 매칭 회차 생성 메서드 : 매칭 신청 전, 해당 메서드 호출로 회차 저장 필요 ... 매개변수 : TUESDAY 권장
    public MatchingRound generateMatchingRound(MockTime mockTime) {
        mockClock.mockTime(mockTime);
        MatchingRound matchingRound = MatchingRound.from(mockTime.getTime(), matchingTimeProperties);
        matchingRoundRepository.save(matchingRound);
        return matchingRound;
    }

    public MatchingRound getCurrentRound() {
        return matchingRoundRepository.findCurrentRound()
                .orElseThrow(() -> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    // 매칭 동작 메서드 : 매칭 동작 전, generateAppliersToMatch 메서드 선행 동작 필요 ... 매개변수 : TUESDAY 권장
    public void operateMatching(MockTime mockTime) {
        mockClock.mockTime(mockTime);
        matchingProcessService.expireMatching();
        matchingProcessService.operateMatching();
    }

    private Image makeImage() {
        return Image.from(new S3File(GIVEN_FILE_NAME, GIVEN_FILE_URL));
    }

    private Member makeMember(String email, Image image) {
        String encodedPassword = passwordEncoder.encrypt(GIVEN_PASSWORD);
        EncodedSignUp signUp = new EncodedSignUp(GIVEN_NAME, email, encodedPassword);
        return Member.of(signUp, image);
    }

    private void saveAsMemberInfo(final Member member) {
        EncodedMember encodedRequest = new EncodedMember(encoder.encrypt(GIVEN_GENDER),
                encoder.encrypt(GIVEN_MBTI),
                encoder.encrypt(GIVEN_BIRTHDAY),
                encoder.encrypt(GIVEN_NATIONALITY),
                encoder.encrypt(GIVEN_ABOUT_ME));

        MemberInfo memberInfo = MemberInfo.of(encodedRequest, member);
        memberInfoRepository.save(memberInfo);
    }

    // MultipartFile 생성 메서드
    public MultipartFile generateMultipartFile() {
        String fileName = "test";
        String path = "/test";
        String contentType = "image/png";
        byte[] content = fileName.getBytes();
        return new MockMultipartFile(fileName, path, contentType, content);
    }

    //AccessToken 생성 메서드
    public String generateAccessToken(Member member) {
        return tokenProvider.generateAccessToken(member.getLoginMember());
    }

    //지원자 생성 메서드
    public void generateAppliersToMatch(List<Member> members) {
        Random random = new Random();

        for (Member member : members) {
            Language firstPreferLanguage = getRandomLanguage(random);
            Language secondPreferLanguage;

            do {
                secondPreferLanguage = getRandomLanguage(random);
            } while (firstPreferLanguage == secondPreferLanguage);

            LoginMember loginMember = member.getLoginMember();
            MatchingApplicationRequest request = new MatchingApplicationRequest(firstPreferLanguage, secondPreferLanguage);
            matchingApplicationService.saveParticipant(loginMember, request);
        }
    }

    // 단일 멤버 매칭 신청 메서드
    public void applySingleMemberToMatch(Member member, MatchingApplicationRequest matchingApplicationRequest) {
        matchingApplicationService.saveParticipant(member.getLoginMember(), matchingApplicationRequest);
    }

    private Language getRandomLanguage(Random random) {
        Language[] languages = Language.values();
        return languages[random.nextInt(languages.length)];
    }
}
