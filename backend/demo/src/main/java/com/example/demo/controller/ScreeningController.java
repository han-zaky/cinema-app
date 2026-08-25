package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.screening.ScreeningRequestDto;
import com.example.demo.dto.screening.ScreeningResponseDto;
import com.example.demo.service.ScreeningService;

import jakarta.validation.Valid;
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

    @GetMapping("/{id}")
    public ScreeningResponseDto getScreeningById(@PathVariable Long id) {
        return screeningService.getScreeningById(id);
    }

    @PostMapping
    public ScreeningResponseDto createScreening(@Valid @RequestBody ScreeningRequestDto requestDto) {
        return screeningService.createScreening(requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteScreening(@PathVariable Long id) {
        screeningService.deleteScreening(id);
    }
}