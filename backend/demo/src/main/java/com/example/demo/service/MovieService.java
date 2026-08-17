package com.example.demo.service;

import com.example.demo.dto.MovieResponseDto;
import com.example.demo.entity.Movie;
import com.example.demo.repository.MovieRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    

    public List<MovieResponseDto> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();

        return movies.stream()
                     .map(movie -> 
                        new MovieResponseDto(
                            movie.getId(), 
                            movie.getTitle(), 
                            movie.getDescription(), 
                            movie.getDurationMinutes(), 
                            movie.getPosterUrl()
                        ))
                     .toList();
    }
    
}
