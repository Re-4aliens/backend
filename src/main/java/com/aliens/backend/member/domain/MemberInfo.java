package com.aliens.backend.member.domain;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.member.controller.dto.EncodedMember;
import com.aliens.backend.member.controller.dto.EncodedMemberPage;
import jakarta.persistence.*;

@Entity
public class MemberInfo {

    @Id
    private Long id;

    @MapsId
    @OneToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "id")
    private Member member;

    @Column
    private String mbti;

    @Column
    private String gender;

    @Column
    private String birthday;

    @Column(name = "about_me")
    private String aboutMe;

    protected MemberInfo() {
    }

    public static MemberInfo of(EncodedMember request, Member member) {
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.member =member;
        memberInfo.gender = request.gender();
        memberInfo.mbti = request.mbti();
        memberInfo.birthday = request.birthday();
        memberInfo.aboutMe = request.aboutMe();
        return memberInfo;
    }

    public void changeAboutMe(final String newAboutMe) {
        aboutMe = newAboutMe;
    }

    public void changeMBTI(final String newMBTI) {
        mbti = newMBTI;
    }

    public EncodedMemberPage getMemberPage() {
        return new EncodedMemberPage(mbti,gender,birthday,aboutMe);
    }
}
