package com.example.demo.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import com.example.demo.entity.Seat;
import com.example.demo.entity.Screening;

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
