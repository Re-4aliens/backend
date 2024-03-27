package com.aliens.backend.chat.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.auth.domain.repository.MemberRepository;
import com.aliens.backend.chat.controller.dto.request.ChatReportRequest;
import com.aliens.backend.chat.domain.ChatReport;
import com.aliens.backend.chat.domain.ChatReportCategory;
import com.aliens.backend.chat.domain.repository.ChatReportRepository;
import com.aliens.backend.global.exception.RestApiException;
import com.aliens.backend.global.response.error.MemberError;
import com.aliens.backend.global.response.success.ChatSuccess;
import org.springframework.stereotype.Service;

@Service
public class ChatReportService {

    private final ChatReportRepository chatReportRepository;
    private final MemberRepository memberRepository;

    public ChatReportService(ChatReportRepository chatReportRepository, final MemberRepository memberRepository) {
        this.chatReportRepository = chatReportRepository;
        this.memberRepository = memberRepository;
    }

    public String reportPartner(LoginMember loginMember, ChatReportRequest chatReportRequest) {
        Member member = getMember(loginMember);
        Member partner = findMemberById(chatReportRequest.partnerId());

        ChatReport chatReport = ChatReport.of(
                member,
                partner,
                ChatReportCategory.fromString(chatReportRequest.category()),
                chatReportRequest.content());

        chatReportRepository.save(chatReport);

        return ChatSuccess.REPORT_SUCCESS.getMessage();
    }

    private Member findMemberById(final Long partnerId) {
        return memberRepository.findById(partnerId).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }

    private Member getMember(final LoginMember loginMember) {
        return memberRepository.findById(loginMember.memberId()).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }
}
