package com.aliens.backend.board.domain.enums;

public enum ProductQuality {
    BRAND_NEW("새 것"),
    ALMOST_NEW("거의 새 것"),
    SLIGHT_DEFECT("약간의 하자"),
    USED("사용감 있음");

    private final String value;

    ProductQuality(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ProductQuality of(String value) {
        for (ProductQuality status : ProductQuality.values()) {
            if (status.value.equals(value)) {
                return status;
            }
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }

        throw new IllegalArgumentException("일치하는 상품상태 없음");
    }
}