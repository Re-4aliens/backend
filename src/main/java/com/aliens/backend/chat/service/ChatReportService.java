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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ChatReportService {

    private final ChatReportRepository chatReportRepository;
    private final MemberRepository memberRepository;

    public ChatReportService(ChatReportRepository chatReportRepository
            , final MemberRepository memberRepository) {
        this.chatReportRepository = chatReportRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public String reportPartner(LoginMember loginMember, ChatReportRequest request) {
        Member member = findMemberById(loginMember.memberId());
        Member partner = findMemberById(request.partnerId());
        saveReport(request, member, partner);
        return ChatSuccess.REPORT_SUCCESS.getMessage();
    }

    private Member findMemberById(final Long partnerId) {
        return memberRepository.findById(partnerId).orElseThrow(() -> new RestApiException(MemberError.NULL_MEMBER));
    }

    private void saveReport(ChatReportRequest request, Member member, Member partner) {
        ChatReport chatReport = ChatReport.of(
                member,
                partner,
                ChatReportCategory.fromString(request.category()),
                request.content());
        chatReportRepository.save(chatReport);
    }
}
