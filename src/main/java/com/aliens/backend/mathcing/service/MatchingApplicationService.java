package com.aliens.backend.mathcing.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.global.response.error.MatchingError;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.MemberError;
import com.aliens.backend.global.response.success.MatchingSuccess;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.aliens.backend.mathcing.controller.dto.response.MatchingApplicationResponse;
import com.aliens.backend.mathcing.controller.dto.response.MatchingBeginTimeResponse;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingRound;
import com.aliens.backend.mathcing.domain.repository.MatchingApplicationRepository;
import com.aliens.backend.mathcing.domain.repository.MatchingRoundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class MatchingApplicationService {
    private final MatchingApplicationRepository matchingApplicationRepository;
    private final MatchingRoundRepository matchingRoundRepository;
    private final MemberRepository memberRepository;
    private final Clock clock;

    public MatchingApplicationService(final MatchingApplicationRepository matchingApplicationRepository,
                                      final MatchingRoundRepository matchingRoundRepository,
                                      final MemberRepository memberRepository,
                                      final Clock clock) {
        this.matchingApplicationRepository = matchingApplicationRepository;
        this.matchingRoundRepository = matchingRoundRepository;
        this.memberRepository = memberRepository;
        this.clock = clock;
    }

    @Transactional
    public String saveParticipant(final LoginMember loginMember,
                                  final MatchingApplicationRequest matchingApplicationRequest) {
        MatchingRound currentRound = getCurrentRound();
        checkReceptionTime(currentRound);
        Member member = getMember(loginMember);
        MatchingApplication matchingApplication = MatchingApplication.from(currentRound, member, matchingApplicationRequest);
        applyForMatching(matchingApplication);
        return MatchingSuccess.APPLY_MATCHING_SUCCESS.getMessage();
    }

    @Transactional(readOnly = true)
    public MatchingApplicationResponse findMatchingApplication(final LoginMember loginMember) {
        MatchingRound currentRound = getCurrentRound();
        MatchingApplication matchingApplication = getMatchingApplication(currentRound, loginMember);
        return MatchingApplicationResponse.from(matchingApplication);
    }

    @Transactional
    public String cancelMatchingApplication(final LoginMember loginMember) {
        MatchingRound currentRound = getCurrentRound();
        checkReceptionTime(currentRound);
        MatchingApplication matchingApplication = getMatchingApplication(currentRound, loginMember);
        cancelForMatching(matchingApplication);
        return MatchingSuccess.CANCEL_MATCHING_APPLICATION_SUCCESS.getMessage();
    }

    @Transactional(readOnly = true)
    public MatchingBeginTimeResponse findMatchingBeginTime() {
        MatchingRound currentRound = getCurrentRound();
        return MatchingBeginTimeResponse.from(currentRound);
    }

    @Transactional
    public void modifyMatchingApplication(final LoginMember loginMember,
                                          final MatchingApplicationRequest matchingApplicationRequest) {
        MatchingRound currentRound = getCurrentRound();
        checkReceptionTime(currentRound);
        MatchingApplication matchingApplication = getMatchingApplication(currentRound, loginMember);
        matchingApplication.modifyTo(matchingApplicationRequest);
    }

    private MatchingRound getCurrentRound() {
        return matchingRoundRepository.findCurrentRound()
                .orElseThrow(()-> new RestApiException(MatchingError.NOT_FOUND_MATCHING_ROUND));
    }

    private Member getMember(LoginMember loginMember) {
        return memberRepository.findById(loginMember.memberId())
                .orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }

    private MatchingApplication getMatchingApplication(MatchingRound matchingRound, LoginMember loginMember) {
        return matchingApplicationRepository.findByMatchingRoundAndMemberId(matchingRound, loginMember.memberId())
                .orElseThrow(()->new RestApiException(MatchingError.NOT_FOUND_MATCHING_APPLICATION_INFO));
    }

    private void applyForMatching(MatchingApplication matchingApplication) {
        checkDuplicateApply(matchingApplication);
        matchingApplicationRepository.save(matchingApplication);
        Member member = matchingApplication.getMember();
        member.applyMatch();
    }

    private void cancelForMatching(MatchingApplication matchingApplication) {
        matchingApplicationRepository.delete(matchingApplication);
        Member member = matchingApplication.getMember();
        member.cancelApplication();
    }

    private void checkReceptionTime(MatchingRound matchingRound) {
        if (!matchingRound.isReceptionTime(LocalDateTime.now(clock))) {
            throw new RestApiException(MatchingError.NOT_VALID_MATCHING_RECEPTION_TIME);
        }
    }

    private void checkDuplicateApply(MatchingApplication matchingApplication) {
        if (matchingApplicationRepository.existsById(matchingApplication.getId())) {
            throw new RestApiException(MatchingError.DUPLICATE_MATCHING_APPLICATION);
        }
    }
}
