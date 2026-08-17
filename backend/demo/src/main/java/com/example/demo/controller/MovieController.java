package com.example.demo.controller;

import java.util.List;

import com.example.demo.dto.MovieResponseDto;
import com.example.demo.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/movies")
public class MovieController {

    private final MovieService movieService;
    
    @GetMapping 
    public List<MovieResponseDto> getAllMovies() {
        return movieService.getAllMovies();
    }

    

}
