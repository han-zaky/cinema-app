package com.example.demo.cinema.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateSeatsRequestDto {

    @NotNull(message = "Hall ID is required")
    private Long hallId;

    @NotNull(message = "Row count is required")
    @Positive(message = "Row count must be positive")
    private Integer rowCount;

    @NotNull(message = "Seats per row is required")
    @Positive(message = "Seats per row must be positive")
    private Integer seatsPerRow;
}