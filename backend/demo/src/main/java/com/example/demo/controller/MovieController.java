package com.example.demo.controller;

import java.util.List;

import com.example.demo.dto.movie.MovieRequestDto;
import com.example.demo.dto.movie.MovieResponseDto;
import com.example.demo.service.MovieService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    @GetMapping
    public List<MovieResponseDto> getAllMovies() {
        return movieService.getAllMovies();
    }

    @PostMapping
    public MovieResponseDto createMovie(@Valid @RequestBody MovieRequestDto requestDto) {
        return movieService.createMovie(requestDto);
    }

    @PatchMapping("/{id}")
    public MovieResponseDto updateMovie(@PathVariable Long id, @RequestBody MovieRequestDto requestDto) {
        return movieService.updateMovie(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }

}
