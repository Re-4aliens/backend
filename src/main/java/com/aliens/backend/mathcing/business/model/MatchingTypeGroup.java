package com.aliens.backend.mathcing.business.model;

import com.aliens.backend.global.property.MatchingRuleProperties;
import com.aliens.backend.mathcing.business.type.*;

import java.util.List;

public class MatchingTypeGroup {
    private List<MatchingType> matchingTypes;

    private MatchingTypeGroup(final List<MatchingType> matchingTypes) {
        this.matchingTypes = matchingTypes;
    }

    public static MatchingTypeGroup init(final MatchingRuleProperties matchingRuleProperties) {
        return new MatchingTypeGroup(
                List.of(new FirstPreferLanguageType(matchingRuleProperties),
                        new SecondPreferLanguageType(matchingRuleProperties),
                        new RandomType(matchingRuleProperties),
                        new SpecialType(matchingRuleProperties)));
    }

    public List<MatchingType> getMatchingTypes() {
        return matchingTypes;
    }
}
