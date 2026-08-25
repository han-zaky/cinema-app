package com.example.demo.dto.reservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReservationResponseDto {
    private Long id;
    private Long userId;
    private Long screeningId;
    private List<String> reservedSeats;
    private BigDecimal totalPrice;
    private String paymentStatus;
    private LocalDateTime createdAt;
}