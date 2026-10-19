package com.example.demo.movie.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenreRequestDto {

    @NotBlank(message = "Name is required")
    private String name;
}