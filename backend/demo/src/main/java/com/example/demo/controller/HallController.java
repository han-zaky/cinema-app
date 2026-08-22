package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.hall.HallRequestDto;
import com.example.demo.dto.hall.HallResponseDto;
import com.example.demo.service.HallService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/halls")
public class HallController {

    private final HallService hallService;

    @GetMapping
    public List<HallResponseDto> getAllHalls() {
        return hallService.getAllHalls();
    }

    @GetMapping("/{id}")
    public HallResponseDto getHallById(@PathVariable Long id) {
        return hallService.getHallById(id);
    }

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