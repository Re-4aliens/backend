package com.aliens.backend.auth.domain;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.board.controller.dto.response.MemberProfileDto;
import com.aliens.backend.member.controller.dto.EncodedMemberPage;
import com.aliens.backend.member.controller.dto.MemberPage;
import com.aliens.backend.member.controller.dto.response.MemberPageResponse;
import com.aliens.backend.member.domain.MemberImage;
import com.aliens.backend.member.domain.MemberInfo;
import com.aliens.backend.member.domain.MatchingStatus;
import com.aliens.backend.member.controller.dto.EncodedSignUp;
import com.aliens.backend.member.sevice.SymmetricKeyEncoder;
import com.aliens.backend.notification.domain.FcmToken;
import com.aliens.backend.uploader.dto.S3File;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class Member {

    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String nationality;

    @Enumerated(value = EnumType.STRING)
    @Column
    private MemberRole role = MemberRole.MEMBER;

    @Column
    private MatchingStatus status = MatchingStatus.NOT_APPLIED_NOT_MATCHED;

    @Column
    private Boolean withdraw = false;

    @OneToMany(mappedBy = "member",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<Token> tokens = new ArrayList<>();

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "imageId")
    private MemberImage memberImage;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private MemberInfo memberInfo;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private FcmToken fcmToken;

    protected Member() {
    }

    public static Member of(final EncodedSignUp request, final MemberImage memberImage) {
        Member member = new Member();
        member.name = request.name();
        member.email = request.email();
        member.nationality = request.nationality();
        member.password = request.password();
        member.memberImage = memberImage;
        return member;
    }

    public void changePassword(final String password) {
        this.password = password;
    }

    public void putMemberInfo(final MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }

    public boolean isCorrectPassword(String password) {
        return this.password.equals(password);
    }

    public void withdraw() {
        withdraw = true;
    }

    public LoginMember getLoginMember() {
        return new LoginMember(id, role);
    }

    public MemberPage getMemberPage() {
        return new MemberPage(name, nationality, memberImage.getURL());
    }

    public boolean isWithdraw() {
        return withdraw;
    }

    public String getStatus() {
        return status.getMessage();
    }

    public String getProfileName() {
        return memberImage.getName();
    }

    public void changeProfileImage(final S3File newFile) {
        memberImage.change(newFile);
    }

    public Long getId() {
        return id;
    }

    public String getFcmToken() {
        return name;
    }

    public void applyMatch() {
        if (status == MatchingStatus.NOT_APPLIED_MATCHED) {
            status = MatchingStatus.APPLIED_MATCHED;
        }
        if (status == MatchingStatus.NOT_APPLIED_NOT_MATCHED) {
            status = MatchingStatus.APPLIED_NOT_MATCHED;
        }
    }

    public void matched() {
        status = MatchingStatus.NOT_APPLIED_MATCHED;
    }

    public void expireMatch() {
        if (status == MatchingStatus.APPLIED_MATCHED) {
            status = MatchingStatus.APPLIED_NOT_MATCHED;
        }
        if (status == MatchingStatus.NOT_APPLIED_MATCHED || status == MatchingStatus.APPLIED_NOT_MATCHED) {
            status = MatchingStatus.NOT_APPLIED_NOT_MATCHED;
        }
    }

    public void cancelApplication() {
        if (status == MatchingStatus.APPLIED_MATCHED) {
            status = MatchingStatus.NOT_APPLIED_MATCHED;
        }
        if (status == MatchingStatus.APPLIED_NOT_MATCHED) {
            status = MatchingStatus.NOT_APPLIED_NOT_MATCHED;
        }
    }

    @Override
    public String toString() {
        return String.format("email:  %s, role : %s", this.email, this.role);
    }

    public MemberProfileDto getprofileDto() {
        return new MemberProfileDto(name, memberImage.getURL(), nationality);
    }

    public boolean isSameId(final Long memberId) {
        return Objects.equals(id, memberId);
    }

    public MemberPageResponse getMemberPageResponse() {
        EncodedMemberPage encodedMemberPage = memberInfo.getMemberPage();

        return new MemberPageResponse(name,
                SymmetricKeyEncoder.decrypt(encodedMemberPage.mbti()),
                SymmetricKeyEncoder.decrypt(encodedMemberPage.gender()),
                nationality,
                SymmetricKeyEncoder.decrypt(encodedMemberPage.birthday()),
                SymmetricKeyEncoder.decrypt(encodedMemberPage.aboutMe()),
                memberImage.getURL());
    }
}