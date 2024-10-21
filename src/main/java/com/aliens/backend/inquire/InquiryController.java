package com.aliens.backend.inquire;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.InquirySuccess;
import com.aliens.backend.inquire.controller.request.InquiryCreateRequest;
import com.aliens.backend.inquire.service.InquiryService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/inquiries")
@RestController
public class InquiryController {

    private final InquiryService inquiryService;

    public InquiryController(final InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping()
    public SuccessResponse<?> createInquiry(@Login final LoginMember loginMember,
                                            @RequestBody final InquiryCreateRequest request) {
        inquiryService.createInquiry(request, loginMember);
        return SuccessResponse.of(InquirySuccess.CREATE_INQUIRY_SUCCESS);
    }
}