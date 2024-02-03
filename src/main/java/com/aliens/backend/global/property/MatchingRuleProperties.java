package com.aliens.backend.global.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MatchingRuleProperties {
    @Value("${matching.rule.max-matches.partner}")
    private String maxPartners;

    @Value("${matching.rule.max-matches.normal-partner}")
    private String maxNormalPartners;

    @Value("${matching.rule.max-tries}")
    private String maxTries;

    public Integer getMaxNormalPartners() {
        return Integer.parseInt(maxNormalPartners);
    }

    public Integer getMaxTries() {
        return Integer.parseInt(maxTries);
    }

    public Integer getMaxPartners() {
        return Integer.parseInt(maxPartners);
    }
}
