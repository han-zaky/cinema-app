package com.example.demo.cinema.dto;

import com.example.demo.cinema.enums.SeatType;

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
    private Integer gridRow;
    private Integer gridCol;
}
