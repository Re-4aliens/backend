package com.aliens.backend.board.domain;

import com.aliens.backend.board.controller.dto.request.MarketBoardCreateRequest;
import com.aliens.backend.board.domain.enums.ProductQuality;
import com.aliens.backend.board.domain.enums.SaleStatus;
import jakarta.persistence.*;

@Entity
public class MarketInfo {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "marketInfoId")
    private Long id;

    @Column
    private String price;

    @Enumerated(EnumType.STRING)
    @Column
    private SaleStatus saleStatus;

    @Enumerated(EnumType.STRING)
    @Column
    private ProductQuality productQuality;

    protected MarketInfo() {
    }

    public MarketInfo(final String price,
                      final SaleStatus saleStatus,
                      final ProductQuality productQuality) {
        this.price = price;
        this.saleStatus = saleStatus;
        this.productQuality = productQuality;
    }

    public static MarketInfo from(final MarketBoardCreateRequest request) {
        return new MarketInfo(
                request.price(),
                request.saleStatus(),
                request.productQuality()
        );
    }

    public String getPrice() {
        return price;
    }

    public SaleStatus getSaleStatus() {
        return saleStatus;
    }

    public ProductQuality getProductStatus() {
        return productQuality;
    }

    public void changePrice(final String price) {
        this.price = price;
    }

    public void changeSaleStatus(final SaleStatus status) {
        this.saleStatus = status;
    }

    public void changeProductStatus(final ProductQuality status) {
        this.productQuality = status;
    }
}