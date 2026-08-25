package com.example.demo.dto.reservation;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationRequestDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Screening ID is required")
    private Long screeningId;

    @NotEmpty(message = "At least one seat must be selected")
    private List<Long> seatIds;
}