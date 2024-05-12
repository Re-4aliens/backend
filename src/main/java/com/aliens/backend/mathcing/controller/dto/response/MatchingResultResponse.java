package com.aliens.backend.mathcing.controller.dto.response;

import com.aliens.backend.chat.domain.ChatRoom;
import com.aliens.backend.chat.domain.ChatRoomStatus;
import com.aliens.backend.mathcing.business.model.Language;
import com.aliens.backend.mathcing.domain.MatchingApplication;
import com.aliens.backend.mathcing.domain.MatchingResult;
import com.aliens.backend.mathcing.business.model.Relationship;
import com.aliens.backend.member.controller.dto.response.MemberPageResponse;

public record MatchingResultResponse(
        ChatRoomStatus roomStatus,
        Long chatRoomId,
        Long partnerMemberId,
        String name,
        String mbti,
        String gender,
        String nationality,
        String profileImageUrl,
        String aboutMe,
        Language firstPreferLanguage,
        Language secondPreferLanguage,
        Relationship relation
) {
    public static MatchingResultResponse of(MatchingResult matchingResult,
                                            ChatRoom chatRoom,
                                            MemberPageResponse matchedMember,
                                            MatchingApplication matchedMemberApplication) {
        return new MatchingResultResponse(
                chatRoom.getStatus(),
                chatRoom.getId(),
                matchingResult.getMatchedMemberId(),
                matchedMember.name(),
                matchedMember.mbti(),
                matchedMember.gender(),
                matchedMember.nationality(),
                matchedMember.profileImageURL(),
                matchedMember.selfIntroduction(),
                matchedMemberApplication.getFirstPreferLanguage(),
                matchedMemberApplication.getSecondPreferLanguage(),
                matchingResult.getRelationship()
        );
    }
}
