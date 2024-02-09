package com.aliens.backend.chat.controller;

import com.aliens.backend.chat.controller.dto.request.ChatBlockRequest;
import com.aliens.backend.chat.service.ChatBlockService;
import com.aliens.backend.global.config.resolver.Login;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;


@RestController
public class ChatBlockController {

    private final ChatBlockService chatBlockService;

    public ChatBlockController(ChatBlockService chatBlockService) {
        this.chatBlockService = chatBlockService;
    }

    @PostMapping("/chat/block")
    public ResponseEntity<Map<String, String>> blockPartner(@Login Long memberId, @RequestBody ChatBlockRequest chatBlockRequest) {
        String result = chatBlockService.blockPartner(memberId, chatBlockRequest);
        Map<String, String> response = Collections.singletonMap("response", result);
        return ResponseEntity.ok(response);
    }
}