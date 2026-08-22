package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.genre.GenreRequestDto;
import com.example.demo.dto.genre.GenreResponseDto;
import com.example.demo.entity.Genre;
import com.example.demo.repository.GenreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    private GenreResponseDto mapToResponseDto(Genre genre) {
        return new GenreResponseDto(
                genre.getId(),
                genre.getName());
    }

    public List<GenreResponseDto> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        return genres.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public GenreResponseDto getGenreById(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found with id: " + id));

        return mapToResponseDto(genre);
    }

    public GenreResponseDto createGenre(GenreRequestDto requestDto) {
        Genre genre = new Genre();
        genre.setName(requestDto.getName());

        Genre savedGenre = genreRepository.save(genre);

        return mapToResponseDto(savedGenre);
    }

    public GenreResponseDto updateGenre(Long id, GenreRequestDto requestDto) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Genre not found with id: " + id));

        if (requestDto.getName() != null) {
            genre.setName(requestDto.getName());
        }

        Genre updatedGenre = genreRepository.save(genre);

        return mapToResponseDto(updatedGenre);
    }

    public void deleteGenre(Long id) {
        if (!genreRepository.existsById(id)) {
            throw new RuntimeException("Genre not found with id: " + id);
        }
        genreRepository.deleteById(id);
    }
}