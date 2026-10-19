package com.example.demo.admin.movies;

import com.example.demo.movie.service.MovieService;
import com.example.demo.movie.dto.MovieRequestDto;
import com.example.demo.movie.dto.MovieResponseDto;

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
@RequestMapping("api/admin/movies")

public class AdminMoviesController {

    private final MovieService movieService;

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
