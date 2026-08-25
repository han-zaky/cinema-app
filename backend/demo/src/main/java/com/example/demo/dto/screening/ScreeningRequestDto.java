package com.example.demo.dto.screening;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScreeningRequestDto {

    @NotNull(message = "Movie ID is required")
    private Long movieId;

    @NotNull(message = "Hall ID is required")
    private Long hallId;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "Base price is required")
    @Positive(message = "Base price must be positive")
    private BigDecimal basePrice;
}