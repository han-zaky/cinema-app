package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.screening.ScreeningRequestDto;
import com.example.demo.dto.screening.ScreeningResponseDto;
import com.example.demo.entity.Hall;
import com.example.demo.entity.Movie;
import com.example.demo.entity.Screening;
import com.example.demo.repository.HallRepository;
import com.example.demo.repository.MovieRepository;
import com.example.demo.repository.ScreeningRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScreeningService {

    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private final ScreeningRepository screeningRepository;

    private ScreeningResponseDto mapToResponseDto(Screening screening) {
        LocalDateTime endTime = screening.getStartTime()
                .plusMinutes(screening.getMovie().getDurationMinutes());

        return new ScreeningResponseDto(
                screening.getId(),
                screening.getMovie().getId(),
                screening.getMovie().getTitle(),
                screening.getHall().getId(),
                screening.getStartTime(),
                endTime,
                screening.getBasePrice());
    }

    public List<ScreeningResponseDto> getAllScreenings() {
        return screeningRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public ScreeningResponseDto getScreeningById(Long id) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Screening not found with id: " + id));

        return mapToResponseDto(screening);
    }

    public ScreeningResponseDto createScreening(ScreeningRequestDto requestDto) {
        Movie movie = movieRepository.findById(requestDto.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + requestDto.getMovieId()));

        Hall hall = hallRepository.findById(requestDto.getHallId())
                .orElseThrow(() -> new RuntimeException("Hall not found with id: " + requestDto.getHallId()));

        LocalDateTime newStart = requestDto.getStartTime();
        LocalDateTime newEnd = newStart.plusMinutes(movie.getDurationMinutes());

        validateNoOverlap(hall.getId(), newStart, newEnd, null);

        Screening screening = new Screening();
        screening.setMovie(movie);
        screening.setHall(hall);
        screening.setStartTime(newStart);
        screening.setBasePrice(requestDto.getBasePrice());

        Screening savedScreening = screeningRepository.save(screening);

        return mapToResponseDto(savedScreening);
    }

    private void validateNoOverlap(Long hallId, LocalDateTime newStart, LocalDateTime newEnd, Long excludeScreeningId) {
        List<Screening> existingScreenings = screeningRepository.findByHallId(hallId);

        for (Screening existing : existingScreenings) {
            if (excludeScreeningId != null && existing.getId().equals(excludeScreeningId)) {
                continue;
            }

            LocalDateTime existingStart = existing.getStartTime();
            LocalDateTime existingEnd = existingStart.plusMinutes(existing.getMovie().getDurationMinutes());

            boolean overlaps = newStart.isBefore(existingEnd) && existingStart.isBefore(newEnd);

            if (overlaps) {
                throw new IllegalArgumentException(
                        "Screening overlaps with an existing screening in this hall from "
                                + existingStart + " to " + existingEnd);
            }
        }
    }

    public void deleteScreening(Long id) {
        if (!screeningRepository.existsById(id)) {
            throw new RuntimeException("Screening not found with id: " + id);
        }
        screeningRepository.deleteById(id);
    }

}
