package com.example.demo.cinema.dto;

import com.example.demo.common.dto.AddressDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CinemaResponseDto {
    private Long id;
    private String name;
    private AddressDto address;
}
