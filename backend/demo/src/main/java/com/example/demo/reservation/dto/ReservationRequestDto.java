package com.example.demo.reservation.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReservationRequestDto(
        @NotNull Long screeningId,
        @NotEmpty @Size(max = 10) List<@NotNull Long> seatIds,
        @Email @Size(max = 255) String customerEmail,
        @Size(max = 100) String customerFirstName,
        @Size(max = 100) String customerLastName) {
}