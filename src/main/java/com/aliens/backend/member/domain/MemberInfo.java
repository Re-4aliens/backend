package com.aliens.backend.member.domain;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.member.controller.dto.EncodedMember;
import com.aliens.backend.member.controller.dto.EncodedMemberPage;
import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

@Entity
public class MemberInfo implements Persistable<Long> {

    @Id
    @Column(name = "memberInfoId")
    private Long id;

    @Column
    private String mbti;

    @Column
    private String gender;

    @Column
    private String birthday;

    @Column
    private String aboutMe;

    protected MemberInfo() {
    }

    public static MemberInfo of(EncodedMember request, Member member) {
        MemberInfo memberInfo = new MemberInfo();
        memberInfo.id = member.getId();
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

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return true;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
