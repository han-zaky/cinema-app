package com.example.demo.dto.movie;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovieRequestDto {

    @NotBlank (message = "Title is required")
    private String title;

    private String description;

    @NotNull (message = "Duration is reqired")
    @Positive (message = "Duration must be positive")
    private Integer durationMinutes;

    private String posterUrl;
}