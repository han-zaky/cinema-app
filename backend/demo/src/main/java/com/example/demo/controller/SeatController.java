package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.seat.GenerateSeatsRequestDto;
import com.example.demo.dto.seat.SeatResponseDto;
import com.example.demo.service.SeatService;

import jakarta.validation.Valid;
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

    @GetMapping("/{id}")
    public SeatResponseDto getSeatById(@PathVariable Long id) {
        return seatService.getSeatById(id);
    }

    @PostMapping("/generate")
    public List<SeatResponseDto> generateSeats(@Valid @RequestBody GenerateSeatsRequestDto requestDto) {
        return seatService.generateSeats(requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
    }
}