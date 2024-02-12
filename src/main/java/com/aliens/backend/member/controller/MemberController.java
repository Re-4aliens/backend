package com.aliens.backend.member.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.response.success.MemberSuccess;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.member.controller.dto.request.SignUpRequest;
import com.aliens.backend.member.controller.dto.request.TemporaryPasswordRequest;
import com.aliens.backend.member.controller.dto.response.MemberPageResponse;
import com.aliens.backend.member.sevice.MemberInfoService;
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
    public SuccessResponse<String> signUp(@RequestPart SignUpRequest signUpRequest,
                                    @RequestPart MultipartFile profileImage) {

        return SuccessResponse.of(
                MemberSuccess.SIGN_UP_SUCCESS,
                memberInfoService.signUp(signUpRequest, profileImage)
        );
    }

    @PostMapping("/temporary-password")
    public SuccessResponse<String> temporaryPassword(@RequestBody TemporaryPasswordRequest request) {

        return SuccessResponse.of(
                MemberSuccess.TEMPORARY_PASSWORD_GENERATED_SUCCESS,
                memberInfoService.generateTemporaryPassword(request)
        );
    }

    @PatchMapping("/password")
    public SuccessResponse<String> changePassword(@Login LoginMember loginMember,
                                            @RequestBody String newPassword ) {

        return SuccessResponse.of(
                MemberSuccess.PASSWORD_CHANGE_SUCCESS,
                memberInfoService.changePassword(loginMember, newPassword)
        );
    }

    @PostMapping("/profile-image")
    public SuccessResponse<String> changeProfileImage(@Login LoginMember loginMember,
                                                @RequestPart MultipartFile newProfileImage) {

        return SuccessResponse.of(
                MemberSuccess.PROFILE_IMAGE_CHANGE_SUCCESS,
                memberInfoService.changeProfileImage(loginMember, newProfileImage)
        );
    }

    @PatchMapping("/about-me")
    public SuccessResponse<String> changeAboutMe(@Login LoginMember loginMember,
                                           @RequestBody String newAboutMe) {

        return SuccessResponse.of(
                MemberSuccess.ABOUT_ME_CHANGE_SUCCESS,
                memberInfoService.changeAboutMe(loginMember, newAboutMe)
        );
    }

    @PatchMapping("/mbti")
    public SuccessResponse<String> changeMBTI(@Login LoginMember loginMember,
                                        @RequestBody String newMBTI) {

        return SuccessResponse.of(
                MemberSuccess.MBTI_CHANGE_SUCCESS,
                memberInfoService.changeMBTI(loginMember, newMBTI)
        );
    }

    @PatchMapping("/withdraw")
    public SuccessResponse<String> withdraw(@Login LoginMember loginMember) {

        return SuccessResponse.of(
                MemberSuccess.WITHDRAW_SUCCESS,
                memberInfoService.withdraw(loginMember)
        );
    }

    @GetMapping("/status")
    public SuccessResponse<String> getStatus(@Login LoginMember loginMember) {

        return SuccessResponse.of(
                MemberSuccess.GET_MEMBER_MATCHING_STATUS_SUCCESS,
                memberInfoService.getStatus(loginMember)
        );
    }

    @GetMapping
    public SuccessResponse<MemberPageResponse> getMemberPage(@Login LoginMember loginMember) {

        return SuccessResponse.of(
                MemberSuccess.GET_MEMBER_PAGE_SUCCESS,
                memberInfoService.getMemberPage(loginMember)
        );
    }
}
