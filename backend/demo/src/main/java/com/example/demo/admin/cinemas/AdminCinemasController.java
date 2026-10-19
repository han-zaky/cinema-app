package com.example.demo.admin.cinemas;

import com.example.demo.cinema.service.CinemaService;
import com.example.demo.cinema.dto.CinemaRequestDto;
import com.example.demo.cinema.dto.CinemaResponseDto;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/cinemas")
public class AdminCinemasController {

    private final CinemaService cinemaService;

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
