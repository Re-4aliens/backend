package com.aliens.backend.global;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.Token;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.auth.domain.repository.TokenRepository;
import com.aliens.backend.auth.service.PasswordEncoder;
import com.aliens.backend.auth.service.TokenProvider;
import com.aliens.backend.board.controller.dto.request.BoardCreateRequest;
import com.aliens.backend.board.controller.dto.request.MarketBoardCreateRequest;
import com.aliens.backend.board.domain.*;
import com.aliens.backend.board.domain.enums.BoardCategory;
import com.aliens.backend.board.domain.enums.ProductQuality;
import com.aliens.backend.board.domain.enums.SaleStatus;
import com.aliens.backend.board.domain.repository.BoardRepository;
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
import com.aliens.backend.member.domain.MemberImage;
import com.aliens.backend.member.domain.MemberInfo;
import com.aliens.backend.member.domain.repository.MemberInfoRepository;
import com.aliens.backend.member.sevice.SymmetricKeyEncoder;
import com.aliens.backend.notification.controller.dto.NotificationRequest;
import com.aliens.backend.notification.domain.FcmToken;
import com.aliens.backend.notification.domain.repository.FcmTokenRepository;
import com.aliens.backend.notification.domain.Notification;
import com.aliens.backend.notification.domain.repository.NotificationRepository;
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
    @Autowired MemberRepository memberRepository;
    @Autowired MatchingApplicationService matchingApplicationService;
    @Autowired MemberInfoRepository memberInfoRepository;
    @Autowired MatchingRoundRepository matchingRoundRepository;
    @Autowired MatchingTimeProperties matchingTimeProperties;
    @Autowired MatchingProcessService matchingProcessService;
    @Autowired BoardRepository boardRepository;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired SymmetricKeyEncoder encoder;
    @Autowired TokenProvider tokenProvider;
    @Autowired MockClock mockClock;
    @Autowired NotificationRepository notificationRepository;
    @Autowired FcmTokenRepository fcmTokenRepository;
    @Autowired TokenRepository tokenRepository;

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
    public static final String GIVEN_BOARD_TITLE = "게시글 제목";
    public static final String GIVEN_BOARD_CONTENT = "게시글 내용";
    public static final String GIVEN_COMMENT_CONTENT = "댓글 내용";
    public static final SaleStatus GIVEN_SALE_STATUS = SaleStatus.SELL;
    public static final String GIVEN_PRICE = "10000";
    public static final ProductQuality GIVEN_PRODUCT_STATUS = ProductQuality.ALMOST_NEW;
    public static final String GIVEN_FCM_TOKEN = "GIVEN_FCM_TOKEN";

    // 다수 멤버 생성 메서드
    public List<Member> generateMultiMember(Integer memberCounts) {
        ArrayList<Member> result = new ArrayList<>();

        for (int i = 0; i < memberCounts; i++) {
            String tmpEmail = GIVEN_EMAIL + i;
            MemberImage memberImage = makeImage();
            Member member = makeMember(tmpEmail, memberImage);
            result.add(member);
        }

        result.forEach(this::saveAsMemberInfo);
        return result;
    }

    // 단일 멤버 생성 메서드
    public Member generateSingleMember() {
        MemberImage memberImage = makeImage();
        Member member = makeMember(GIVEN_EMAIL, memberImage);
        saveAsMemberInfo(member);
        saveFcmToken(member);
        return member;
    }

    private void saveFcmToken(final Member member) {
        FcmToken fcmToken = FcmToken.of(member, GIVEN_FCM_TOKEN);
        fcmTokenRepository.save(fcmToken);
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

    // 매칭 동작 메서드 : 매칭 동작 전, 매칭 신청자들 필요
    public void operateMatching() {
        matchingProcessService.expireMatching();
        matchingProcessService.operateMatching();
    }

    private MemberImage makeImage() {
        return MemberImage.from(new S3File(GIVEN_FILE_NAME, GIVEN_FILE_URL));
    }

    private Member makeMember(String email, MemberImage memberImage) {
        String encodedPassword = passwordEncoder.encrypt(GIVEN_PASSWORD);
        EncodedSignUp signUp = new EncodedSignUp(GIVEN_NAME, email, encodedPassword, GIVEN_NATIONALITY);
        return memberRepository.save(Member.of(signUp, memberImage));
    }

    private void saveAsMemberInfo(final Member member) {
        EncodedMember encodedRequest = new EncodedMember(SymmetricKeyEncoder.encrypt(GIVEN_GENDER),
                SymmetricKeyEncoder.encrypt(GIVEN_MBTI),
                SymmetricKeyEncoder.encrypt(GIVEN_BIRTHDAY),
                SymmetricKeyEncoder.encrypt(GIVEN_ABOUT_ME));

        MemberInfo memberInfo = MemberInfo.of(encodedRequest, member);
        memberInfoRepository.save(memberInfo);

        member.putMemberInfo(memberInfo);
        memberRepository.save(member);
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
        String accessToken = tokenProvider.generateAccessToken(member.getLoginMember());
        Token refreshToken = new Token(member);
        refreshToken.putRefreshToken(accessToken);
        tokenRepository.save(refreshToken);
        return accessToken;
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

    // 일반 카테고리 게시글 생성
    public Board generateSingleNormalBoard(final Member member, final BoardCategory category) {
        BoardCreateRequest request = new BoardCreateRequest(GIVEN_BOARD_TITLE, GIVEN_BOARD_CONTENT, category);

        Board board = Board.normalOf(request, member);

        BoardImage givenBoardImage1 = makeBoardImage(board);
        BoardImage givenBoardImage2 = makeBoardImage(board);
        board.setImages(List.of(givenBoardImage1, givenBoardImage2));

        Comment comment = Comment.parentOf(GIVEN_COMMENT_CONTENT, board, member);
        board.addComment(comment);

        Great great = Great.of(board, member);
        board.addGreat(great);

        return boardRepository.save(board);
    }

    // 일반 카테고리 게시글 생성(내용 기입 가능)
    public Board generateSingleNormalBoard(final Member member, final BoardCategory category, final String content) {
        BoardCreateRequest request = new BoardCreateRequest(GIVEN_BOARD_TITLE, content, category);

        Board board = Board.normalOf(request, member);

        BoardImage givenBoardImage1 = makeBoardImage(board);
        BoardImage givenBoardImage2 = makeBoardImage(board);
        board.setImages(List.of(givenBoardImage1, givenBoardImage2));

        Comment comment = Comment.parentOf(GIVEN_COMMENT_CONTENT, board, member);
        board.addComment(comment);

        Great great = Great.of(board, member);
        board.addGreat(great);

        return boardRepository.save(board);
    }

    // 장터 게시글 생성
    public Board generateSingleMarketBoard(final Member member) {
        MarketBoardCreateRequest marketRequest = new MarketBoardCreateRequest(GIVEN_BOARD_TITLE,
                GIVEN_BOARD_CONTENT, GIVEN_SALE_STATUS, GIVEN_PRICE, GIVEN_PRODUCT_STATUS);
        MarketInfo givenMarketInfo = MarketInfo.from(marketRequest);
        BoardCreateRequest boardRequest = BoardCreateRequest.from(marketRequest);

        Board board = Board.marketOf(boardRequest, member, givenMarketInfo);

        BoardImage givenBoardImage1 = makeBoardImage(board);
        BoardImage givenBoardImage2 = makeBoardImage(board);
        board.setImages(List.of(givenBoardImage1, givenBoardImage2));

        Comment comment = Comment.parentOf(GIVEN_COMMENT_CONTENT, board, member);
        board.addComment(comment);

        Great great = Great.of(board, member);
        board.addGreat(great);

        return boardRepository.save(board);
    }

    // 장터 게시글 생성(내용 기입 가능)
    public Board generateSingleMarketBoard(final Member member, final String content) {
        MarketBoardCreateRequest marketRequest = new MarketBoardCreateRequest(GIVEN_BOARD_TITLE,
                content, GIVEN_SALE_STATUS, GIVEN_PRICE, GIVEN_PRODUCT_STATUS);
        MarketInfo givenMarketInfo = MarketInfo.from(marketRequest);
        BoardCreateRequest boardRequest = BoardCreateRequest.from(marketRequest);

        Board board = Board.marketOf(boardRequest, member, givenMarketInfo);

        BoardImage givenBoardImage1 = makeBoardImage(board);
        BoardImage givenBoardImage2 = makeBoardImage(board);
        board.setImages(List.of(givenBoardImage1, givenBoardImage2));

        Comment comment = Comment.parentOf(GIVEN_COMMENT_CONTENT, board, member);
        board.addComment(comment);

        Great great = Great.of(board, member);
        board.addGreat(great);

        return boardRepository.save(board);
    }

    private BoardImage makeBoardImage(Board board) {
        BoardImage boardImage = BoardImage.from(new S3File(GIVEN_FILE_NAME, GIVEN_FILE_URL));
        boardImage.setBoard(board);
        return boardImage;
    }

    public void generateNotificationWithCount(final Member member, final int count) {
        for(int i = 0; i < count; i++) {
            NotificationRequest request = new NotificationRequest(BoardCategory.ALL, 1L, GIVEN_COMMENT_CONTENT,List.of(1L));
            Notification notification = Notification.of(request,member);
            notificationRepository.save(notification);
        }
    }

    public Notification generateSingleNotification(final Member member) {
        NotificationRequest request = new NotificationRequest(BoardCategory.ALL, 1L, GIVEN_COMMENT_CONTENT,List.of(1L));
        Notification notification = Notification.of(request,member);
        return notificationRepository.save(notification);
    }
}
