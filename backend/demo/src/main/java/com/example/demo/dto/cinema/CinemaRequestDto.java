package com.example.demo.dto.cinema;

import com.example.demo.dto.address.AddressDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CinemaRequestDto {
    @NotBlank(message = "Name is required")
    private String name;
    @NotNull
    private AddressDto address;
}
