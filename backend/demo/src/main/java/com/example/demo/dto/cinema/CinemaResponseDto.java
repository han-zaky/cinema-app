package com.example.demo.dto.cinema;

import com.example.demo.dto.address.AddressDto;

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
