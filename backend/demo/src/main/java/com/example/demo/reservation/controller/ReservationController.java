package com.example.demo.reservation.controller;


import com.example.demo.reservation.service.ReservationService;
import com.example.demo.reservation.dto.ReservationRequestDto;
import com.example.demo.reservation.dto.ReservationResponseDto;
import com.example.demo.user.entity.User;
import com.example.demo.user.service.UserService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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