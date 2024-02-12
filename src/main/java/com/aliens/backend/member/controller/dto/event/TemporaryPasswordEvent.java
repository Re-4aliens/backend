package com.aliens.backend.member.controller.dto.event;

public record TemporaryPasswordEvent(String email, String tmpPassword){
}
