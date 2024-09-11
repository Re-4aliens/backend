package com.aliens.backend.global.aspect.log.member;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.global.response.log.InfoLogResponse;
import com.aliens.backend.global.response.success.EmailSuccess;
import com.aliens.backend.global.response.success.MemberSuccess;
import com.aliens.backend.member.controller.dto.request.SignUpRequest;
import com.aliens.backend.member.controller.dto.request.TemporaryPasswordRequest;
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
public class MemberLogAspect {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper;

    public MemberLogAspect(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.member.pointcut.MemberPointcut.signUp() " +
            "&& args(request, ..)")
    public void logDuplicateCheck(SignUpRequest request) throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("email", request.email());
        data.put("name", request.name());
        data.put("nationality", request.nationality());
        data.put("birthday", request.birthday());
        InfoLogResponse response = InfoLogResponse.from(MemberSuccess.SIGN_UP_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.member.pointcut.MemberPointcut.temporaryPassword() " +
            "&& args(request)")
    public void logTemporaryPassword(TemporaryPasswordRequest request) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(MemberSuccess.TEMPORARY_PASSWORD_GENERATED_SUCCESS, request);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.member.pointcut.MemberPointcut.changePassword() " +
            "&& args(loginMember, ..)")
    public void logChangePassword(LoginMember loginMember) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(MemberSuccess.PASSWORD_CHANGE_SUCCESS, loginMember);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.member.pointcut.MemberPointcut.changeProfileImage() " +
            "&& args(loginMember, ..)")
    public void logChangeProfileImage(LoginMember loginMember) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(MemberSuccess.PROFILE_IMAGE_CHANGE_SUCCESS, loginMember);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.member.pointcut.MemberPointcut.changeAboutMe() " +
            "&& args(loginMember, newAboutMe)", argNames = "loginMember,newAboutMe")
    public void logChangeAboutMe(LoginMember loginMember, String newAboutMe) throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("loginMember", loginMember);
        data.put("newAboutMe", newAboutMe);
        InfoLogResponse response = InfoLogResponse.from(MemberSuccess.ABOUT_ME_CHANGE_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.member.pointcut.MemberPointcut.changeMBTI() " +
            "&& args(loginMember, newMBTI)", argNames = "loginMember,newMBTI")
    public void logChangeMBTI(LoginMember loginMember, String newMBTI) throws JsonProcessingException {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("loginMember", loginMember);
        data.put("newMBTI", newMBTI);
        InfoLogResponse response = InfoLogResponse.from(MemberSuccess.MBTI_CHANGE_SUCCESS, data);
        log.info(objectMapper.writeValueAsString(response));
    }

    @AfterReturning(value = "com.aliens.backend.global.aspect.log.member.pointcut.MemberPointcut.withdraw() " +
            "&& args(loginMember)")
    public void logWithdraw(LoginMember loginMember) throws JsonProcessingException {
        InfoLogResponse response = InfoLogResponse.from(MemberSuccess.WITHDRAW_SUCCESS, loginMember);
        log.info(objectMapper.writeValueAsString(response));
    }
}
