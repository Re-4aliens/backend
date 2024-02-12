package com.aliens.backend.block.controller;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.block.controller.dto.BlockRequest;
import com.aliens.backend.block.service.BlockService;
import com.aliens.backend.global.config.resolver.Login;
import com.aliens.backend.global.response.SuccessResponse;
import com.aliens.backend.global.response.success.ChatSuccess;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlockController {

    private final BlockService blockService;

    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @PostMapping("/chat/block")
    public SuccessResponse<String> blockPartner(@Login LoginMember loginMember,
                                                @RequestBody BlockRequest request) {
        return SuccessResponse.of(
                ChatSuccess.BLOCK_SUCCESS,
                blockService.blockPartner(loginMember, request)
        );
    }
}