package com.example.demo.cinema.controller;


import com.example.demo.cinema.service.SeatService;
import com.example.demo.cinema.dto.SeatResponseDto;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatService seatService;

    @GetMapping
    public List<SeatResponseDto> getSeatsByHall(@RequestParam Long hallId) {
        return seatService.getSeatsByHall(hallId);
    }

    // TODO
    @GetMapping("/{id}")
    public SeatResponseDto getSeatById(@PathVariable Long id) {
        return null; // seatService.getSeatById(id);
    }
}
