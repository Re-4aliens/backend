package com.aliens.backend.global.aspect.log.matching;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.global.response.log.InfoLogResponse;
import com.aliens.backend.global.response.success.EmailSuccess;
import com.aliens.backend.global.response.success.MatchingSuccess;
import com.aliens.backend.mathcing.controller.dto.request.MatchingApplicationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Aspect
@Component
public class MatchingLogAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public MatchingLogAspect(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.matching.pointcut.MatchingPointcut.applyMatch()" +
            "&& args(loginMember, matchingApplicationRequest)", argNames = "loginMember,matchingApplicationRequest")
    public void logApplyMatch(LoginMember loginMember, MatchingApplicationRequest matchingApplicationRequest)
            throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("loginMember", loginMember);
        data.put("matchingApplicationRequest", matchingApplicationRequest);
        InfoLogResponse response = InfoLogResponse.from(MatchingSuccess.APPLY_MATCHING_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.matching.pointcut.MatchingPointcut.getMatchingApplication()" +
            "&& args(loginMember)")
    public void logGetMatchingApplication(LoginMember loginMember) throws JsonProcessingException {
        InfoLogResponse response =
                InfoLogResponse.from(MatchingSuccess.GET_MATCHING_APPLICATION_STATUS_SUCCESS, loginMember);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.matching.pointcut.MatchingPointcut.modifyMatchingApplication()" +
            "&& args(loginMember, matchingApplicationRequest)", argNames = "loginMember,matchingApplicationRequest")
    public void logModifyMatchingApplication(LoginMember loginMember, MatchingApplicationRequest matchingApplicationRequest)
            throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("loginMember", loginMember);
        data.put("matchingApplicationRequest", matchingApplicationRequest);
        InfoLogResponse response = InfoLogResponse.from(MatchingSuccess.MODIFY_MATCHING_APPLICATION_INFO_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.matching.pointcut.MatchingPointcut.cancelMatchingApplication()" +
            "&& args(loginMember)")
    public void logCancelMatchingApplication(LoginMember loginMember) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(MatchingSuccess.CANCEL_MATCHING_APPLICATION_SUCCESS, loginMember);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.matching.pointcut.MatchingPointcut.getMatchingPartners()" +
            "&& args(loginMember)")
    public void logGetMatchingPartners(LoginMember loginMember) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(MatchingSuccess.GET_MATCHING_PARTNERS_SUCCESS, loginMember);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.matching.pointcut.MatchingPointcut.expireMatching()")
    public void logExpireMatching() throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(MatchingSuccess.EXPIRE_MATCHING_SUCCESS);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.matching.pointcut.MatchingPointcut.operateMatching()")
    public void logOperateMatching() throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(MatchingSuccess.OPERATE_MATCHING_SUCCESS);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.matching.pointcut.MatchingPointcut.saveMatchRound()")
    public void logSaveMatchRound() throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(MatchingSuccess.CREATE_MATCHING_ROUND_SUCCESS);
        log.info(objectMapper.writeValueAsString(response));
    }
}
