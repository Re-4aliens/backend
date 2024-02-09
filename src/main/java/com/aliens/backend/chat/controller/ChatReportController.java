package com.aliens.backend.chat.controller;

import com.aliens.backend.chat.controller.dto.request.ChatReportRequest;
import com.aliens.backend.chat.service.ChatReportService;
import com.aliens.backend.global.config.resolver.Login;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;


@RestController
public class ChatReportController {

    private final ChatReportService chatReportService;

    public ChatReportController(ChatReportService chatReportService) {
        this.chatReportService = chatReportService;
    }

    @PostMapping("/chat/report")
    public ResponseEntity<Map<String, String>> reportPartner(@Login Long memberId, @RequestBody ChatReportRequest chatReportRequest) {
        String result = chatReportService.reportPartner(memberId, chatReportRequest);
        Map<String, String> response = Collections.singletonMap("response", result);
        return ResponseEntity.ok(response);
    }
}