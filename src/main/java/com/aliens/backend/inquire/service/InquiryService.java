package com.aliens.backend.inquire.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.inquire.controller.request.InquiryCreateRequest;
import com.aliens.backend.inquire.domain.Inquiry;
import com.aliens.backend.inquire.repository.InquiryRepository;
import org.springframework.stereotype.Service;

@Service
public class InquiryService {
    private final InquiryRepository inquiryRepository;

    public InquiryService(final InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    public void createInquiry(final InquiryCreateRequest request, final LoginMember loginMember) {
        inquiryRepository.save(new Inquiry(request.content(), loginMember.memberId()));
    }
}