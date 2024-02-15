package com.aliens.backend.global.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MatchingRuleProperties {
    @Value("${matching.rule.max-matches.partner}")
    private Integer maxPartners;

    @Value("${matching.rule.max-matches.normal-partner}")
    private Integer maxNormalPartners;

    @Value("${matching.rule.max-tries}")
    private Integer maxTries;

    public Integer getMaxNormalPartners() {
        return maxNormalPartners;
    }

    public Integer getMaxTries() {
        return maxTries;
    }

    public Integer getMaxPartners() {
        return maxPartners;
    }
}
