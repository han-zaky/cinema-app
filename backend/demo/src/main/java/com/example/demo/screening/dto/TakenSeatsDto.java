package com.example.demo.screening.dto;

import java.util.List;
import java.util.Set;

import com.example.demo.cinema.entity.Seat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TakenSeatsDto {
    private List<Long> seats;

}
