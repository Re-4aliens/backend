package com.aliens.backend.chat.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.chat.controller.dto.request.ChatReportRequest;
import com.aliens.backend.chat.service.ChatReportService;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.ChatSuccess;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ChatReportController {

    private final ChatReportService chatReportService;

    public ChatReportController(ChatReportService chatReportService) {
        this.chatReportService = chatReportService;
    }

    @PostMapping("/chat/report")
    public SuccessResponse<String> reportPartner(@Login LoginMember loginMember,
                                         @RequestBody ChatReportRequest chatReportRequest) {

        return SuccessResponse.of(
                ChatSuccess.REPORT_SUCCESS,
                chatReportService.reportPartner(loginMember, chatReportRequest)
        );
    }
}