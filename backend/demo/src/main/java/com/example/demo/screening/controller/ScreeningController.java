package com.example.demo.screening.controller;

import com.example.demo.screening.service.ScreeningService;
import com.example.demo.screening.dto.ScreeningResponseDto;
import com.example.demo.screening.dto.TakenSeatsDto;
import com.example.demo.cinema.dto.SeatResponseDto;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/screenings")
public class ScreeningController {

    private final ScreeningService screeningService;

    @GetMapping
    public List<ScreeningResponseDto> getAllScreenings() {
        return screeningService.getAllScreenings();
    }

    // all seats of the hall this screening runs in
    @GetMapping("/{id}/seats")
    public List<SeatResponseDto> getSeatsForScreening(@PathVariable Long id) {
        return screeningService.getSeatsForScreening(id);
    }

    @GetMapping("/{id}/taken-seats")
    public TakenSeatsDto getScreeningById(@PathVariable Long id) {
        return screeningService.getTakenSeats(id);
    }
}
