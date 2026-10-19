package com.example.demo.screening.dto;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScreeningResponseDto {
    private Long id;
    private Long movieId;
    private String movieTitle;
    private Long hallId;
    private Instant startTime;
    private Instant endTime;
    private BigDecimal basePrice;
}