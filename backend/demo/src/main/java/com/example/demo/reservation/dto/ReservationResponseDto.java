package com.example.demo.reservation.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReservationResponseDto {
    private UUID id;
    private Long userId;
    private Long screeningId;
    private List<String> reservedSeats;
    private BigDecimal totalPrice;
    private String paymentStatus;
    private Instant createdAt;
}