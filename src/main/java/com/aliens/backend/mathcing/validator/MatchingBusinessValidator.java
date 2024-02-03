package com.aliens.backend.mathcing.validator;

import com.aliens.backend.global.property.MatchingRuleProperties;
import com.aliens.backend.mathcing.service.model.Participant;
import org.springframework.stereotype.Component;

@Component
public class MatchingBusinessValidator {
    private final MatchingRuleProperties matchingRuleProperties;

    public MatchingBusinessValidator(final MatchingRuleProperties matchingRuleProperties) {
        this.matchingRuleProperties = matchingRuleProperties;
    }

    public boolean isValidMatching(Participant participant, Participant partner) {
        return participant != partner &&
                !participant.isPartnerWith(partner) &&
                !partner.isPartnerWith(participant) &&
                isExceededMaxNormalPartners(partner);
    }

    public boolean isExceededMaxNormalPartners(Participant participant) {
        return participant.getNumberOfPartners() > matchingRuleProperties.getMaxNormalPartners();
    }

    public boolean isExceedMaxTries(int tries) {
        return tries > matchingRuleProperties.getMaxTries();
    }

}
