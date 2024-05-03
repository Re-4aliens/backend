package com.aliens.backend.board.controller.dto.response;

import com.aliens.backend.board.domain.enums.ProductStatus;
import com.aliens.backend.board.domain.enums.SaleStatus;

import java.time.Instant;
import java.util.List;

public record MarketBoardResponse(Long id,
                                  String title,
                                  SaleStatus saleStatus,
                                  String price,
                                  ProductStatus productStatus,
                                  String content,
                                  Long greatCount,
                                  Long commentCount,
                                  List<String>imageUrls,
                                  Instant createdAt,
                                  MemberProfileDto memberProfileDto) {
}
