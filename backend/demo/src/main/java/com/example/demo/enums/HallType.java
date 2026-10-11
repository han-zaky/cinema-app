package com.example.demo.enums;

import java.math.BigDecimal;

public enum HallType {
    STANDART(new BigDecimal("0.00")),
    DELUXE(new BigDecimal("50.00"));

    private final BigDecimal additionalValue;

    HallType(BigDecimal value) {
        this.additionalValue = value;
    }

    public BigDecimal getAdditionalValue() {
        return additionalValue;
    }
}
