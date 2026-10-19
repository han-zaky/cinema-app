package com.example.demo.cinema.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HallResponseDto {
    private Long id;
    private String name;
    private Long cinemaId;
}