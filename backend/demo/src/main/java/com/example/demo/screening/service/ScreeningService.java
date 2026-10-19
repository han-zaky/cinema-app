package com.example.demo.screening.service;

import com.example.demo.screening.entity.Screening;
import com.example.demo.screening.repository.ScreeningRepository;
import com.example.demo.cinema.dto.SeatResponseDto;
import com.example.demo.cinema.entity.Hall;
import com.example.demo.cinema.repository.HallRepository;
import com.example.demo.cinema.service.SeatService;
import com.example.demo.movie.entity.Movie;
import com.example.demo.movie.repository.MovieRepository;
import com.example.demo.reservation.repository.ReservationSeatRepository;
import com.example.demo.screening.dto.ScreeningRequestDto;
import com.example.demo.screening.dto.ScreeningResponseDto;
import com.example.demo.screening.dto.TakenSeatsDto;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScreeningService {

    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;
    private final ScreeningRepository screeningRepository;
    private final ReservationSeatRepository reserSeatRepository;
    private final SeatService seatService;

    private ScreeningResponseDto mapToResponseDto(Screening screening) {
        Instant endTime = screening.getStartTime()
                .plus(Duration.ofMinutes(screening.getMovie().getDurationMinutes()));

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

    public Screening getScreeningById(Long id) {
        Screening screening = screeningRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Screening not found with id: " + id));

        return screening;
    }

    public ScreeningResponseDto createScreening(ScreeningRequestDto requestDto) {
        Movie movie = movieRepository.findById(requestDto.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found with id: " + requestDto.getMovieId()));

        Hall hall = hallRepository.findById(requestDto.getHallId())
                .orElseThrow(() -> new RuntimeException("Hall not found with id: " + requestDto.getHallId()));

        Instant newStart = requestDto.getStartTime();
        Instant newEnd = newStart.plus(Duration.ofMinutes(movie.getDurationMinutes()));

        validateNoOverlap(hall.getId(), newStart, newEnd, null);

        Screening screening = new Screening();
        screening.setMovie(movie);
        screening.setHall(hall);
        screening.setStartTime(newStart);
        screening.setBasePrice(requestDto.getBasePrice());

        Screening savedScreening = screeningRepository.save(screening);

        return mapToResponseDto(savedScreening);
    }

    private void validateNoOverlap(Long hallId, Instant newStart, Instant newEnd, Long excludeScreeningId) {
        List<Screening> existingScreenings = screeningRepository.findByHallId(hallId);

        for (Screening existing : existingScreenings) {
            if (excludeScreeningId != null && existing.getId().equals(excludeScreeningId)) {
                continue;
            }

            Instant existingStart = existing.getStartTime();
            Instant existingEnd = existingStart.plus(Duration.ofMinutes(existing.getMovie().getDurationMinutes()));

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

    public List<SeatResponseDto> getSeatsForScreening(Long screeningId) {
        Screening screening = getScreeningById(screeningId);
        return seatService.getSeatsByHall(screening.getHall().getId());
    }

    public TakenSeatsDto getTakenSeats(Long screeningId) {
        List<Long> seatIds = reserSeatRepository.findTakenSeatIds(screeningId, Instant.now());
        return new TakenSeatsDto(seatIds);
    }

}
