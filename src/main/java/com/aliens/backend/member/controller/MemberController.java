package com.aliens.backend.member.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.member.controller.dto.request.SignUpRequest;
import com.aliens.backend.member.controller.dto.request.TemporaryPasswordRequest;
import com.aliens.backend.member.controller.dto.response.MemberPageResponse;
import com.aliens.backend.member.sevice.MemberInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Map;

@RequestMapping("/members")
@RestController
public class MemberController {

    private final MemberInfoService memberInfoService;

    public MemberController(final MemberInfoService memberInfoService) {
        this.memberInfoService = memberInfoService;
    }

    @PostMapping()
    public ResponseEntity<Map<String, String>> signUp(@RequestPart SignUpRequest signUpRequest,
                                                      @RequestPart MultipartFile profileImage) {
        String result = memberInfoService.signUp(signUpRequest, profileImage);
        Map<String, String> response = Collections.singletonMap("response", result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/temporary-password")
    public ResponseEntity<Map<String, String>> temporaryPassword(@RequestBody TemporaryPasswordRequest request) {
        String result = memberInfoService.generateTemporaryPassword(request);
        Map<String, String> response = Collections.singletonMap("response", result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(@Login LoginMember loginMember,
                                                              @RequestBody String newPassword ) {
        String result = memberInfoService.changePassword(loginMember, newPassword);
        Map<String, String> response = Collections.singletonMap("response", result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/profile-image")
    public ResponseEntity<Map<String, String>> changeProfileImage(@Login LoginMember loginMember,
                                                                  @RequestPart MultipartFile newProfileImage) {
        String result = memberInfoService.changeProfileImage(loginMember, newProfileImage);
        Map<String, String> response = Collections.singletonMap("response", result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/about-me")
    public ResponseEntity<Map<String, String>> changeAboutMe(@Login LoginMember loginMember,
                                                             @RequestBody String newAboutMe) {
        String result = memberInfoService.changeAboutMe(loginMember, newAboutMe);
        Map<String, String> response = Collections.singletonMap("response", result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/mbti")
    public ResponseEntity<Map<String, String>> changeMBTI(@Login LoginMember loginMember,
                                                          @RequestBody String newMBTI) {
        String result = memberInfoService.changeMBTI(loginMember, newMBTI);
        Map<String, String> response = Collections.singletonMap("response", result);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/withdraw")
    public ResponseEntity<Map<String, String>> withdraw(@Login LoginMember loginMember) {
        String result = memberInfoService.withdraw(loginMember);
        Map<String, String> response = Collections.singletonMap("response", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getStatus(@Login LoginMember loginMember) {
        String result = memberInfoService.getStatus(loginMember);
        Map<String, String> response = Collections.singletonMap("response", result);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<MemberPageResponse> getMemberPage(@Login LoginMember loginMember) {
        MemberPageResponse result = memberInfoService.getMemberPage(loginMember);
        return ResponseEntity.ok(result);
    }
}
