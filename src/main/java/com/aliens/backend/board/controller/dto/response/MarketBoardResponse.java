package com.aliens.backend.board.controller.dto.response;

import com.aliens.backend.board.domain.enums.ProductQuality;
import com.aliens.backend.board.domain.enums.SaleStatus;

import java.time.Instant;
import java.util.List;

public record MarketBoardResponse(Long id,
                                  String title,
                                  SaleStatus saleStatus,
                                  String price,
                                  ProductQuality productQuality,
                                  String content,
                                  Long greatCount,
                                  Long commentCount,
                                  List<String>imageUrls,
                                  Instant createdAt,
                                  MemberProfileDto memberProfileDto) {
}
