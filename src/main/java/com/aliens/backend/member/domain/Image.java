package com.aliens.backend.member.domain;

import com.aliens.backend.uploader.dto.S3File;
import jakarta.persistence.*;

@Entity
public class Image {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    String name;

    @Column
    String url;

    protected Image() {
    }

    public static Image from(final S3File imageFile) {
        Image image = new Image();
        image.name = imageFile.fileName();
        image.url = imageFile.fileURL();
        return image;
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
