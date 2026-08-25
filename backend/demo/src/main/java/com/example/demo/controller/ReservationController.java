package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.reservation.ReservationRequestDto;
import com.example.demo.dto.reservation.ReservationResponseDto;
import com.example.demo.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ReservationResponseDto createReservation(@Valid @RequestBody ReservationRequestDto requestDto) {
        return reservationService.createReservation(requestDto);
    }

    @GetMapping("/{id}")
    public ReservationResponseDto getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }
}