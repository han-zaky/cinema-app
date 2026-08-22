package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.hall.HallRequestDto;
import com.example.demo.dto.hall.HallResponseDto;
import com.example.demo.entity.Cinema;
import com.example.demo.entity.Hall;
import com.example.demo.repository.CinemaRepository;
import com.example.demo.repository.HallRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HallService {

    private final HallRepository hallRepository;
    private final CinemaRepository cinemaRepository;

    private HallResponseDto mapToResponseDto(Hall hall) {
        return new HallResponseDto(
                hall.getId(),
                hall.getName(),
                hall.getCinema().getId());
    }

    public List<HallResponseDto> getAllHalls() {
        List<Hall> halls = hallRepository.findAll();
        return halls.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public HallResponseDto getHallById(Long id) {
        Hall hall = hallRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hall not found with id: " + id));

        return mapToResponseDto(hall);
    }

    public HallResponseDto createHall(HallRequestDto requestDto) {
        Cinema cinema = cinemaRepository.findById(requestDto.getCinemaId())
                .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + requestDto.getCinemaId()));

        Hall hall = new Hall();
        hall.setName(requestDto.getName());
        hall.setCinema(cinema);

        Hall savedHall = hallRepository.save(hall);

        return mapToResponseDto(savedHall);
    }

    public HallResponseDto updateHall(Long id, HallRequestDto requestDto) {
        Hall hall = hallRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hall not found with id: " + id));

        if (requestDto.getName() != null) {
            hall.setName(requestDto.getName());
        }
        if (requestDto.getCinemaId() != null) {
            Cinema cinema = cinemaRepository.findById(requestDto.getCinemaId())
                    .orElseThrow(() -> new RuntimeException("Cinema not found with id: " + requestDto.getCinemaId()));
            hall.setCinema(cinema);
        }

        Hall updatedHall = hallRepository.save(hall);

        return mapToResponseDto(updatedHall);
    }

    public void deleteHall(Long id) {
        if (!hallRepository.existsById(id)) {
            throw new RuntimeException("Hall not found with id: " + id);
        }
        hallRepository.deleteById(id);
    }
}