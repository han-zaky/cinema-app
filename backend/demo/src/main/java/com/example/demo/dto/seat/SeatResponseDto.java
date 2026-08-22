package com.example.demo.dto.seat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SeatResponseDto {
    private Long id;
    private String rowLetter;
    private Integer seatNumber;
    private Long hallId;
}
