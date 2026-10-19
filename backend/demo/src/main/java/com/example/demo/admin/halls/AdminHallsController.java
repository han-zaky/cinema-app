package com.example.demo.admin.halls;

import com.example.demo.cinema.service.HallService;
import com.example.demo.cinema.dto.HallRequestDto;
import com.example.demo.cinema.dto.HallResponseDto;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/admin/halls")
public class AdminHallsController {

    private final HallService hallService;

    @PostMapping
    public HallResponseDto createHall(@Valid @RequestBody HallRequestDto requestDto) {
        return hallService.createHall(requestDto);
    }

    @PatchMapping("/{id}")
    public HallResponseDto updateHall(@PathVariable Long id, @RequestBody HallRequestDto requestDto) {
        return hallService.updateHall(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
    }

}
