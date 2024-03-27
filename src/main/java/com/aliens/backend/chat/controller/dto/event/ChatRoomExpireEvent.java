package com.aliens.backend.chat.controller.dto.event;

import com.aliens.backend.chat.service.model.MemberPair;

import java.util.Set;

public record ChatRoomExpireEvent(Set<MemberPair> matchedPairs) {
}
