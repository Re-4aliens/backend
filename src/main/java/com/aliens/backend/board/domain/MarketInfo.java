package com.aliens.backend.board.domain;

import com.aliens.backend.board.controller.dto.request.MarketBoardCreateRequest;
import com.aliens.backend.board.domain.enums.ProductStatus;
import com.aliens.backend.board.domain.enums.SaleStatus;
import jakarta.persistence.*;

@Entity
public class MarketInfo {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column
    private String price;

    @Enumerated(EnumType.STRING)
    @Column
    private SaleStatus saleStatus;

    @Enumerated(EnumType.STRING)
    @Column
    private ProductStatus productStatus;

    protected MarketInfo() {
    }

    public MarketInfo(final String price,
                      final SaleStatus saleStatus,
                      final ProductStatus productStatus) {
        this.price = price;
        this.saleStatus = saleStatus;
        this.productStatus = productStatus;
    }

    public static MarketInfo from(final MarketBoardCreateRequest request) {
        return new MarketInfo(
                request.price(),
                request.saleStatus(),
                request.productStatus()
        );
    }

    public String getPrice() {
        return price;
    }

    public SaleStatus getSaleStatus() {
        return saleStatus;
    }

    public ProductStatus getProductStatus() {
        return productStatus;
    }

    public void changePrice(final String price) {
        this.price = price;
    }

    public void changeSaleStatus(final SaleStatus status) {
        this.saleStatus = status;
    }

    public void changeProductStatus(final ProductStatus status) {
        this.productStatus = status;
    }
}