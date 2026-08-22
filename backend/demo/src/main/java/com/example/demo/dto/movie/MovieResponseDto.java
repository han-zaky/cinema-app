package com.example.demo.dto.movie;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MovieResponseDto {
    private Long id;
    private String title;
    private String description;
    private Integer durationMinutes;
    private String posterUrl;
}
