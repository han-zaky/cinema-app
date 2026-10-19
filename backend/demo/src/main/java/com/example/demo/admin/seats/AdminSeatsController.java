package com.example.demo.admin.seats;

import com.example.demo.cinema.service.SeatService;
import com.example.demo.cinema.dto.GenerateSeatsRequestDto;
import com.example.demo.cinema.dto.SeatResponseDto;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/seats")
public class AdminSeatsController {

    private final SeatService seatService;

    @PostMapping("/generate")
    public List<SeatResponseDto> generateSeats(@Valid @RequestBody GenerateSeatsRequestDto requestDto) {
        return seatService.generateSeats(requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteSeat(@PathVariable Long id) {
        seatService.deleteSeat(id);
    }

}
