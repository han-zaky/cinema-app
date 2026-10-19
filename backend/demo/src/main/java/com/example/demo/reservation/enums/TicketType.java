package com.example.demo.reservation.enums;

import java.math.BigDecimal;

public enum TicketType {
    ADULT(new BigDecimal("1.00")),
    STUDENT(new BigDecimal("0.75")),
    CHILD(new BigDecimal("0.50")),
    SENIOR(new BigDecimal("0.75"));

    private final BigDecimal multiplier;

    TicketType(BigDecimal multiplier) {
        this.multiplier = multiplier;
    }

    public BigDecimal priceFrom(BigDecimal basePrice) {
        return basePrice.multiply(multiplier).setScale(2);
    }
}
