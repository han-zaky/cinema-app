package com.example.demo.cinema.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HallRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Cinema ID is required")
    private Long cinemaId;
}