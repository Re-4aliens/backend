package com.aliens.backend.board.controller.dto.request;

import com.aliens.backend.board.domain.enums.ProductQuality;
import com.aliens.backend.board.domain.enums.SaleStatus;

public record MarketChangeRequest(
        String title,
        String content,
        SaleStatus saleStatus,
        String price,
        ProductQuality productQuality) {
}
