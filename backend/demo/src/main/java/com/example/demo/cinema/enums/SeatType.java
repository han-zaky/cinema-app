package com.example.demo.cinema.enums;

import java.math.BigDecimal;

public enum SeatType {
    SINGLE(new BigDecimal("1.00")),
    DOUBLE(new BigDecimal("2.00")),
    COMFY(new BigDecimal("1.20"));

    private final BigDecimal multiplier;

    SeatType(BigDecimal value) {
        this.multiplier = value;
    }

    public BigDecimal getMultiplier() {
        return multiplier;
    }

}