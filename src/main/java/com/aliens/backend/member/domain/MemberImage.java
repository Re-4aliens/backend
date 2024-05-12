package com.aliens.backend.member.domain;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.uploader.dto.S3File;
import jakarta.persistence.*;

@Entity
public class MemberImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberImageId")
    private Long id;

    @Column
    String name;

    @Column
    String url;

    @OneToOne
    @JoinColumn(name = "memberId")
    private Member member;

    protected MemberImage() {
    }

    public static MemberImage from(final S3File imageFile) {
        MemberImage memberImage = new MemberImage();
        memberImage.name = imageFile.fileName();
        memberImage.url = imageFile.fileURL();
        return memberImage;
    }

    public void change(final S3File newFile) {
        name = newFile.fileName();
        url = newFile.fileURL();
    }

    public String getName() {
        return name;
    }

    public String getURL() {
        return url;
    }
}
