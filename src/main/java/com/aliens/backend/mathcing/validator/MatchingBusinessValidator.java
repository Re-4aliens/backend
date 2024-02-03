package com.aliens.backend.mathcing.validator;

import com.aliens.backend.global.property.MatchingRuleProperties;
import com.aliens.backend.mathcing.service.model.Participant;
import com.aliens.backend.mathcing.service.model.Relationship;
import org.springframework.stereotype.Component;

@Component
public class MatchingBusinessValidator {
    private final MatchingRuleProperties matchingRuleProperties;

    public MatchingBusinessValidator(final MatchingRuleProperties matchingRuleProperties) {
        this.matchingRuleProperties = matchingRuleProperties;
    }

    public boolean isValidMatching(Relationship relationship, Participant participant, Participant partner) {
        return participant != partner &&
                !participant.isPartnerWith(partner) &&
                !partner.isPartnerWith(participant) &&
                isExceededMaxPartners(relationship, partner);
    }

    public boolean isExceededMaxPartners(Relationship relationship, Participant participant) {
        if (relationship.equals(Relationship.NORMAL)) {
            return participant.getNumberOfPartners() > matchingRuleProperties.getMaxNormalPartners();
        }
        return participant.getNumberOfPartners() > matchingRuleProperties.getMaxPartners();
    }

    public boolean isExceedMaxTries(int tries) {
        return tries > matchingRuleProperties.getMaxTries();
    }

}
