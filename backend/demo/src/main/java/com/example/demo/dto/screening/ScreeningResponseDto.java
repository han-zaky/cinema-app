package com.example.demo.dto.screening;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal basePrice;
}