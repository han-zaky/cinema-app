package com.example.demo.admin.genres;

import com.example.demo.movie.service.GenreService;
import com.example.demo.movie.dto.GenreRequestDto;
import com.example.demo.movie.dto.GenreResponseDto;

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
@RequestMapping("api/admin/genres")
public class AdminGenresController {

    private final GenreService genreService;

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
