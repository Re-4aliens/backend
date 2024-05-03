package com.aliens.backend.board.domain.enums;


public enum SaleStatus {
    SELL("판매 중"),
    END("판매 완료");

    private final String value;

    SaleStatus(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SaleStatus of(String value) {
        for (SaleStatus status : SaleStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }

        throw new IllegalArgumentException("일치하는 판매상태 없음");
    }
}