package com.example.demo.screening.service;


import com.example.demo.screening.entity.Screening;
import com.example.demo.cinema.entity.Seat;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class PricingService {
    private BigDecimal totalPrice;

    public BigDecimal calculateTotal(List<Seat> seats, Screening screening) {
        totalPrice = BigDecimal.ZERO;
        for (Seat seat : seats) {
            calculateSeatPrice(seat, screening);
        }
        return totalPrice;

    }

    public BigDecimal calculateSeatPrice(Seat seat, Screening screening) {
        return screening.getBasePrice()
                .add(seat.getHall().getHallType().getAdditionalValue()
                        .multiply(seat.getSeatType().getMultiplier()))
                .setScale(2);
    }

}
