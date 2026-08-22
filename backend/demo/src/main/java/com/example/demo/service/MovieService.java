package com.example.demo.service;

import com.example.demo.dto.movie.MovieRequestDto;
import com.example.demo.dto.movie.MovieResponseDto;
import com.example.demo.entity.Cinema;
import com.example.demo.entity.Movie;
import com.example.demo.repository.MovieRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    private MovieResponseDto mapToRespondeDto(Movie movie) {
        return new MovieResponseDto(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getDurationMinutes(),
                movie.getPosterUrl());
    }

    public List<MovieResponseDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(this::mapToRespondeDto)
                .toList();
    }

    public MovieResponseDto getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + id));

        return mapToRespondeDto(movie);
    }

    public MovieResponseDto createMovie(MovieRequestDto requestDto) {
        Movie movie = new Movie();
        movie.setTitle(requestDto.getTitle());
        movie.setDescription(requestDto.getDescription());
        movie.setDurationMinutes(requestDto.getDurationMinutes());
        movie.setPosterUrl(requestDto.getPosterUrl());

        Movie savedMovie = movieRepository.save(movie);

        return mapToRespondeDto(savedMovie);
    }

    // Alternative -> Mapstruct from mapping library
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

        return mapToRespondeDto(updatedMovie);
    }

    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new RuntimeException("Movies not found with id: " + id);
        }
        movieRepository.deleteById(id);
    }

}
