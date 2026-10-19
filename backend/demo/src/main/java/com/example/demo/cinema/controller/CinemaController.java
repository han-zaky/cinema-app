package com.example.demo.cinema.controller;


import com.example.demo.cinema.service.CinemaService;
import com.example.demo.cinema.dto.CinemaResponseDto;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{id}")
    public CinemaResponseDto getCinemaById(@PathVariable Long id) {
        return cinemaService.getCinemaById(id);
    }

}
