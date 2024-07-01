package com.aliens.backend.board.domain;

import com.aliens.backend.auth.domain.Member;
import com.aliens.backend.board.controller.dto.request.MarketChangeRequest;
import com.aliens.backend.board.controller.dto.request.BoardCreateRequest;
import com.aliens.backend.board.controller.dto.response.BoardResponse;
import com.aliens.backend.board.controller.dto.response.MarketBoardResponse;
import com.aliens.backend.board.domain.enums.BoardCategory;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "boardId")
    private Long id;

    @Column
    private BoardCategory category;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private Long commentCount = 0L;

    @Column
    private Long greatCount = 0L;

    @OneToMany(mappedBy = "board",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<BoardImage> boardImages = new ArrayList<>();

    @OneToMany(mappedBy = "board",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<Great> greats = new ArrayList<>();

    @OneToMany(mappedBy = "board",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE},
            orphanRemoval = true)
    @JoinColumn(name = "marketInfoId")
    private MarketInfo marketInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @Column(updatable = false)
    @CreatedDate
    private Instant createdAt;

    protected Board() {
    }

    private Board(
            final String title,
            final String content,
            final BoardCategory category,
            final Member member
    ) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.member = member;
    }

    public static Board normalOf(final BoardCreateRequest request,
                                 final Member member) {
        return new Board(
                request.title(),
                request.content(),
                request.boardCategory(),
                member
        );
    }

    public static Board marketOf(final BoardCreateRequest request,
                                 final Member member,
                                 final MarketInfo marketInfo) {
        return new Board(
                request.title(),
                request.content(),
                request.boardCategory(),
                member,
                marketInfo
        );
    }

    private Board(
            final String title,
            final String content,
            final BoardCategory category,
            final Member member,
            final MarketInfo marketInfo
    ) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.member = member;
        this.marketInfo = marketInfo;
    }

    public void update(
            final String title,
            final String content
    ) {
        this.title = title;
        this.content = content;
    }

    public boolean isSameWithRequest(final BoardCreateRequest request) {
        return this.title.equals(request.title())
                && this.content.equals(request.content());
    }

    public boolean isSameWithRequest(final MarketChangeRequest request) {
        return this.title.equals(request.title())
                && this.content.equals(request.content());
    }

    public List<BoardImage> getImages() {
        return boardImages;
    }

    public String getPrice() {
        return marketInfo.getPrice();
    }

    public Long getId() {
        return id;
    }

    public void setImages(final List<BoardImage> boardImageEntitys) {
        this.boardImages.addAll(boardImageEntitys);
    }

    public void addComment(final Comment comment) {
        commentCount++;
        comments.add(comment);
    }

    public void addGreat(final Great great) {
        greatCount++;
        greats.add(great);
    }

    public BoardResponse getBoardResponse() {
        return new BoardResponse(id,
                category,
                title,
                content,
                greatCount,
                commentCount,
                getImageURLs(),
                createdAt,
                member.getprofileDto());
    }

    private List<String> getImageURLs() {
        return boardImages.stream().map(BoardImage::getURL).toList();
    }

    public MarketBoardResponse getMarketBoardResponse() {
        return new MarketBoardResponse(
                id,
                title,
                marketInfo.getSaleStatus(),
                marketInfo.getPrice(),
                marketInfo.getProductStatus(),
                content,
                greatCount,
                commentCount,
                getImageURLs(),
                createdAt,
                member.getprofileDto());
    }

    public boolean isWriter(final Long memberId) {
        return member.isSameId(memberId);
    }

    public void changeByRequest(final MarketChangeRequest request) {
        title = request.title();
        content = request.content();
        marketInfo.changePrice(request.price());
        marketInfo.changeSaleStatus(request.saleStatus());
        marketInfo.changeProductStatus(request.productQuality());
    }

    public void minusGreatCount() {
        greatCount--;
    }

    public Long getGreatCount() {
        return greatCount;
    }

    public BoardCategory getCategory() {
        return category;
    }

    public Long getWriterId() {
        return member.getId();
    }

    public boolean isJustCreated() {
        Instant now = Instant.now();
        Instant fiveMinutesAgo = now.minusSeconds(300); // 5 분
        return createdAt.isAfter(fiveMinutesAgo);
    }
}
