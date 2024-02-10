package com.aliens.backend.matching.unit.business;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.MemberRole;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.aliens.backend.mathcing.service.MatchingApplicationService;
import com.aliens.backend.mathcing.service.model.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class MatchingApplicationGenerator {
    @Autowired MatchingApplicationService matchingApplicationService;



    public void applyToMatch(Long numberOfMember) {
        Random random = new Random();

        for (long i = 1L; i <= numberOfMember; i++) {
            Language firstPreferLanguage = getRandomLanguage(random);
            Language secondPreferLanguage;

            do {
                secondPreferLanguage = getRandomLanguage(random);
            } while (firstPreferLanguage == secondPreferLanguage);
            LoginMember loginMember = new LoginMember(i, MemberRole.MEMBER);
            MatchingApplicationRequest request = new MatchingApplicationRequest(firstPreferLanguage, secondPreferLanguage);
            matchingApplicationService.saveParticipant(loginMember, request);
        }
    }

    private Language getRandomLanguage(Random random) {
        Language[] languages = Language.values();
        return languages[random.nextInt(languages.length)];
    }
}
