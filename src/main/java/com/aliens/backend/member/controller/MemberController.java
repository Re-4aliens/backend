package com.aliens.backend.member.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.success.MemberSuccessCode;
import com.aliens.backend.global.success.SuccessResponse;
import com.aliens.backend.global.success.SuccessResponseWithoutResult;
import com.aliens.backend.member.controller.dto.request.SignUpRequest;
import com.aliens.backend.member.controller.dto.request.TemporaryPasswordRequest;
import com.aliens.backend.member.controller.dto.response.MemberPageResponse;
import com.aliens.backend.member.sevice.MemberInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberInfoService memberInfoService;

    public MemberController(final MemberInfoService memberInfoService) {
        this.memberInfoService = memberInfoService;
    }

    @PostMapping()
    public ResponseEntity<?> signUp(@RequestPart SignUpRequest signUpRequest,
                                                      @RequestPart MultipartFile profileImage) {
        memberInfoService.signUp(signUpRequest, profileImage);
        return SuccessResponseWithoutResult.toResponseEntity(MemberSuccessCode.SIGN_UP_SUCCESS);
    }

    @PostMapping("/temporary-password")
    public ResponseEntity<?> temporaryPassword(@RequestBody TemporaryPasswordRequest request) {
        memberInfoService.generateTemporaryPassword(request);
        return SuccessResponseWithoutResult.toResponseEntity(MemberSuccessCode.TEMPORARY_PASSWORD_GENERATED_SUCCESS);
    }

    @PatchMapping("/password")
    public ResponseEntity<?> changePassword(@Login LoginMember loginMember,
                                                              @RequestBody String newPassword ) {
        memberInfoService.changePassword(loginMember, newPassword);
        return SuccessResponseWithoutResult.toResponseEntity(MemberSuccessCode.PASSWORD_CHANGE_SUCCESS);
    }

    @PostMapping("/profile-image")
    public ResponseEntity<?> changeProfileImage(@Login LoginMember loginMember,
                                                                  @RequestPart MultipartFile newProfileImage) {
        memberInfoService.changeProfileImage(loginMember, newProfileImage);
        return SuccessResponseWithoutResult.toResponseEntity(MemberSuccessCode.PROFILE_IMAGE_CHANGE_SUCCESS);
    }

    @PatchMapping("/about-me")
    public ResponseEntity<?> changeAboutMe(@Login LoginMember loginMember,
                                                             @RequestBody String newAboutMe) {
        memberInfoService.changeAboutMe(loginMember, newAboutMe);
        return SuccessResponseWithoutResult.toResponseEntity(MemberSuccessCode.ABOUT_ME_CHANGE_SUCCESS);
    }

    @PatchMapping("/mbti")
    public ResponseEntity<?> changeMBTI(@Login LoginMember loginMember,
                                                          @RequestBody String newMBTI) {
        memberInfoService.changeMBTI(loginMember, newMBTI);
        return SuccessResponseWithoutResult.toResponseEntity(MemberSuccessCode.MBTI_CHANGE_SUCCESS);
    }

    @PatchMapping("/withdraw")
    public ResponseEntity<?> withdraw(@Login LoginMember loginMember) {
        memberInfoService.withdraw(loginMember);
        return SuccessResponseWithoutResult.toResponseEntity(MemberSuccessCode.WITHDRAW_SUCCESS);
    }

    @GetMapping("/status")
    public ResponseEntity<?> getStatus(@Login LoginMember loginMember) {
        return SuccessResponse.toResponseEntity(MemberSuccessCode.GET_MEMBER_MATCHING_STATUS_SUCCESS,
                memberInfoService.getStatus(loginMember));
    }

    @GetMapping()
    public ResponseEntity<?> getMemberPage(@Login LoginMember loginMember) {
        return SuccessResponse.toResponseEntity(MemberSuccessCode.GET_MEMBER_PAGE_SUCCESS,
                memberInfoService.getMemberPage(loginMember));
    }
}
