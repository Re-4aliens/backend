package com.aliens.backend.inquire.service;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.global.BaseIntegrationTest;
import com.aliens.backend.global.DummyGenerator;
import com.aliens.backend.inquire.controller.request.InquiryCreateRequest;
import com.aliens.backend.inquire.domain.Inquiry;
import com.aliens.backend.inquire.repository.InquiryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class InquiryServiceTest extends BaseIntegrationTest {
    @Autowired
    InquiryService inquiryService;
    @Autowired
    DummyGenerator dummyGenerator;
    @Autowired
    InquiryRepository inquiryRepository;

    Member givenMember;
    LoginMember givenLoginMember;

    @BeforeEach
    void setUp() {
        givenMember = dummyGenerator.generateSingleMember();
        givenLoginMember = givenMember.getLoginMember();
    }

    @Test
    @DisplayName("문의 생성")
    void blockPartner() {
        //Given
        InquiryCreateRequest request = new InquiryCreateRequest("문의 내용");

        //When
        inquiryService.createInquiry(request, givenLoginMember);

        //Then
        Inquiry inquiry = inquiryRepository.findAll().get(0);
        Assertions.assertEquals(request.content(), inquiry.getContent());
    }
}