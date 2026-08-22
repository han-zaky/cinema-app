package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.cinema.CinemaRequestDto;
import com.example.demo.dto.cinema.CinemaResponseDto;
import com.example.demo.service.CinemaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cinemas")
public class CinemaController {

    private final CinemaService cinemaService;

    @GetMapping
    public List<CinemaResponseDto> getAllCinemas() {
        return cinemaService.getAllCinemas();
    }

    @GetMapping("*{id}")
    public CinemaResponseDto getCinemaById(@PathVariable Long id) {
        return cinemaService.getCinemaById(id);
    }

    @PostMapping
    public CinemaResponseDto createCinema(@Valid @RequestBody CinemaRequestDto requestDto) {
        return cinemaService.createCinema(requestDto);
    }

    @PatchMapping("/{id}")
    public CinemaResponseDto updateCinema(@PathVariable Long id, @RequestBody CinemaRequestDto requestDto) {
        return cinemaService.updateCinema(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteCinema(@PathVariable Long id) {
        cinemaService.deleteCinema(id);
    }

}
