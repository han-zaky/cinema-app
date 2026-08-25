package com.example.demo.dto.seat;

import com.example.demo.enums.SeatType;

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
    private SeatType seatType;
    private Long hallId;
}
