package com.example.demo.movie.service;


import com.example.demo.movie.entity.Genre;
import com.example.demo.movie.entity.Movie;
import com.example.demo.movie.repository.MovieRepository;
import com.example.demo.movie.dto.MovieRequestDto;
import com.example.demo.movie.dto.MovieResponseDto;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    private MovieResponseDto mapToResponseDto(Movie movie) {
        return new MovieResponseDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getDurationMinutes(),
                movie.getPosterUrl(),
                movie.getGenres().stream().map(Genre::getName).sorted().toList());
    }

    public List<MovieResponseDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public MovieResponseDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));

        return mapToResponseDto(movie);
    }

    @Transactional
    public MovieResponseDto createMovie(MovieRequestDto requestDto) {
        Movie movie = new Movie();
        movie.setTitle(requestDto.getTitle());
        movie.setDescription(requestDto.getDescription());
        movie.setDurationMinutes(requestDto.getDurationMinutes());
        movie.setPosterUrl(requestDto.getPosterUrl());

        Movie savedMovie = movieRepository.save(movie);

        return mapToResponseDto(savedMovie);
    }

    // Alternative -> Mapstruct from mapping library
    @Transactional
    public MovieResponseDto updateMovie(Long id, MovieRequestDto requestDto) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));

        if (requestDto.getTitle() != null) {
            movie.setTitle(requestDto.getTitle());
        }
        if (requestDto.getDescription() != null) {
            movie.setDescription(requestDto.getDescription());
        }
        if (requestDto.getDurationMinutes() != null) {
            movie.setDurationMinutes(requestDto.getDurationMinutes());
        }
        if (requestDto.getPosterUrl() != null) {
            movie.setPosterUrl(requestDto.getPosterUrl());
        }

        Movie updatedMovie = movieRepository.save(movie);

        return mapToResponseDto(updatedMovie);
    }

    @Transactional
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Movies not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }

}
