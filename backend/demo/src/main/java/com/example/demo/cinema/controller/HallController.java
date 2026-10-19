package com.example.demo.cinema.controller;


import com.example.demo.cinema.service.HallService;
import com.example.demo.cinema.dto.HallResponseDto;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
