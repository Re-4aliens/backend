package com.aliens.backend.global.aspect.log.matching.pointcut;

import org.aspectj.lang.annotation.Pointcut;

public class MatchingPointcut {
    @Pointcut("execution(* com.aliens.backend.mathcing.controller.MatchingApplicationController.applyMatch(..))")
    public void applyMatch() {}

    @Pointcut("execution(* com.aliens.backend.mathcing.controller.MatchingApplicationController.getMatchingApplication(..))")
    public void getMatchingApplication() {}

    @Pointcut("execution(* com.aliens.backend.mathcing.controller.MatchingApplicationController.getMatchingBeginTime(..))")
    public void getMatchingBeginTime() {}

    @Pointcut("execution(* com.aliens.backend.mathcing.controller.MatchingApplicationController.modifyMatchingApplication(..))")
    public void modifyMatchingApplication() {}

    @Pointcut("execution(* com.aliens.backend.mathcing.controller.MatchingApplicationController.cancelMatchingApplication(..))")
    public void cancelMatchingApplication() {}

    @Pointcut("execution(* com.aliens.backend.mathcing.controller.MatchingProcessController.getMatchingPartners(..))")
    public void getMatchingPartners() {}

    @Pointcut("execution(* com.aliens.backend.mathcing.service.MatchingProcessService.expireMatching(..))")
    public void expireMatching() {}

    @Pointcut("execution(* com.aliens.backend.mathcing.service.MatchingProcessService.operateMatching(..))")
    public void operateMatching() {}

    @Pointcut("execution(* com.aliens.backend.mathcing.service.MatchingRoundService.saveMatchRound(..))")
    public void saveMatchRound() {}
}
