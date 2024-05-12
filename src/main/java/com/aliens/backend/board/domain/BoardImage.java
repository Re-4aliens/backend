package com.aliens.backend.board.domain;

import com.aliens.backend.uploader.dto.S3File;
import jakarta.persistence.*;

@Entity
public class BoardImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boardImageId")
    private Long id;

    @Column
    String name;

    @Column
    String url;

    public void setBoard(final Board board) {
        this.board = board;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardId")
    private Board board;

    protected BoardImage() {
    }

    public static BoardImage from(final S3File imageFile) {
        BoardImage boardImage = new BoardImage();
        boardImage.name = imageFile.fileName();
        boardImage.url = imageFile.fileURL();
        return boardImage;
    }

    public String getURL() {
        return url;
    }

    public String getName() {
        return name;
    }
}
