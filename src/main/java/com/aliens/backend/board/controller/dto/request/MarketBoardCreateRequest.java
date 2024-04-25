package com.aliens.backend.board.controller.dto.request;

import com.aliens.backend.board.domain.enums.ProductStatus;
import com.aliens.backend.board.domain.enums.SaleStatus;

public record MarketBoardCreateRequest(String title,
                                       String content,
                                       SaleStatus saleStatus,
                                       String price,
                                       ProductStatus productStatus
                                       ) {
}
