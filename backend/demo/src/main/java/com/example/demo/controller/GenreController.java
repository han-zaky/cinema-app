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

import com.example.demo.dto.genre.GenreRequestDto;
import com.example.demo.dto.genre.GenreResponseDto;
import com.example.demo.service.GenreService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    @GetMapping
    public List<GenreResponseDto> getAllGenres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/{id}")
    public GenreResponseDto getGenreById(@PathVariable Long id) {
        return genreService.getGenreById(id);
    }

    @PostMapping
    public GenreResponseDto createGenre(@Valid @RequestBody GenreRequestDto requestDto) {
        return genreService.createGenre(requestDto);
    }

    @PatchMapping("/{id}")
    public GenreResponseDto updateGenre(@PathVariable Long id, @RequestBody GenreRequestDto requestDto) {
        return genreService.updateGenre(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
    }
}