package com.aliens.backend.auth.domain;

import com.aliens.backend.auth.controller.dto.LoginMember;
import com.aliens.backend.member.controller.dto.MemberPage;
import com.aliens.backend.member.domain.Image;
import com.aliens.backend.member.domain.MemberInfo;
import com.aliens.backend.member.domain.MemberStatus;
import com.aliens.backend.member.controller.dto.EncodedSignUp;
import com.aliens.backend.uploader.dto.S3File;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Member {

    @Id @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @Enumerated(value = EnumType.STRING)
    @Column
    private MemberRole role = MemberRole.MEMBER;

    @Column
    private MemberStatus status = MemberStatus.NOT_APPLIED_NOT_MATCHED;

    @Column
    private Boolean withdraw = false;

    @OneToMany(mappedBy = "member",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<Token> tokens = new ArrayList<>();

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @JoinColumn(name = "image_id")
    private Image image;

    @OneToOne(mappedBy = "member",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private MemberInfo memberInfo;

    protected Member() {
    }

    public static Member of(final EncodedSignUp request, final Image image) {
        Member member = new Member();
        member.name = request.name();
        member.email = request.email();
        member.password = request.password();
        member.image = image;
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
        return new MemberPage(name, image.getURL());
    }

    public boolean isWithdraw() {
        return withdraw;
    }

    public String getStatus() {
        return status.getMessage();
    }

    public String getProfileName() {
        return image.getName();
    }

    public void changeProfileImage(final S3File newFile) {
        image.change(newFile);
    }

    @Override
    public String toString() {
        return String.format("email:  %s, role : %s", this.email, this.role);
    }
}