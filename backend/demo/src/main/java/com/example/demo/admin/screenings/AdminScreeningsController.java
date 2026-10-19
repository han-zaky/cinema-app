package com.example.demo.admin.screenings;

import com.example.demo.screening.service.ScreeningService;
import com.example.demo.screening.dto.ScreeningRequestDto;
import com.example.demo.screening.dto.ScreeningResponseDto;

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
@RequestMapping("api/admin/screenings")
public class AdminScreeningsController {

    private final ScreeningService screeningService;

    @PostMapping
    public ScreeningResponseDto createScreening(@Valid @RequestBody ScreeningRequestDto requestDto) {
        return screeningService.createScreening(requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteScreening(@PathVariable Long id) {
        screeningService.deleteScreening(id);
    }

}
