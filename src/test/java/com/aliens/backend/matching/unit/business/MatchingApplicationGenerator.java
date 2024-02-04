package com.aliens.backend.matching.unit.business;

import com.aliens.backend.mathcing.service.MatchingApplicationService;
import com.aliens.backend.mathcing.service.model.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

import static com.aliens.backend.mathcing.controller.dto.request.MatchingRequest.*;

@Component
public class MatchingApplicationGenerator {
    @Autowired
    MatchingApplicationService matchingApplicationService;



    public void applyToMatch(Long numberOfMember) {
        Random random = new Random();

        for (long i = 1L; i <= numberOfMember; i++) {
            Language firstPreferLanguage = getRandomLanguage(random);
            Language secondPreferLanguage;

            do {
                secondPreferLanguage = getRandomLanguage(random);
            } while (firstPreferLanguage == secondPreferLanguage);

            MatchingApplicationRequest request = new MatchingApplicationRequest(i, firstPreferLanguage, secondPreferLanguage);
            matchingApplicationService.saveParticipant(request);
        }
    }

    private Language getRandomLanguage(Random random) {
        Language[] languages = Language.values();
        return languages[random.nextInt(languages.length)];
    }
}
