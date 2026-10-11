package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.reservation.ReservationRequestDto;
import com.example.demo.dto.reservation.ReservationResponseDto;
import com.example.demo.service.ReservationService;
import com.example.demo.service.UserService;

import com.example.demo.auth.entity.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserService userService;

    @PostMapping
    public ReservationResponseDto createReservation(@Valid @RequestBody ReservationRequestDto requestDto,
            Authentication authentication) {
        User user = null;
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {
            user = userService.findByEmail(authentication.getName()).orElse(null);
        }
        return reservationService.createReservation(requestDto, user);
    }

    @GetMapping("/{id}")
    public ReservationResponseDto getReservationById(@PathVariable Long id) {
        return reservationService.getReservationById(id);
    }
}