package com.example.demo.cinema.service;


import com.example.demo.cinema.entity.Hall;
import com.example.demo.cinema.entity.Seat;
import com.example.demo.cinema.enums.SeatType;
import com.example.demo.cinema.repository.HallRepository;
import com.example.demo.cinema.repository.SeatRepository;
import com.example.demo.cinema.dto.GenerateSeatsRequestDto;
import com.example.demo.cinema.dto.SeatResponseDto;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;

    private SeatResponseDto mapToResponseDto(Seat seat) {
        return new SeatResponseDto(
                seat.getId(),
                seat.getRowLetter(),
                seat.getSeatNumber(),
                seat.getSeatType(),
                seat.getHall().getId(),
                seat.getGridRow(),
                seat.getGridCol());
    }

    public List<SeatResponseDto> getSeatsByHall(Long hallId) {
        List<Seat> seats = seatRepository.findByHallId(hallId);
        return seats.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    public Seat getSeatById(Long id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found with id: " + id));

        return seat;
    }

    public List<SeatResponseDto> generateSeats(GenerateSeatsRequestDto requestDto) {
        Hall hall = hallRepository.findById(requestDto.getHallId())
                .orElseThrow(() -> new RuntimeException("Hall not found with id: " + requestDto.getHallId()));

        List<Seat> seatsToCreate = new java.util.ArrayList<>();
        int totalRows = requestDto.getRowCount();

        for (int row = 0; row < totalRows; row++) {
            String rowLetter = generateRowLetter(row);
            boolean isDoubleRow = row >= totalRows - 2;
            SeatType seatType = isDoubleRow ? SeatType.DOUBLE : SeatType.SINGLE;

            for (int seatNumber = 1; seatNumber <= requestDto.getSeatsPerRow(); seatNumber++) {
                Seat seat = new Seat();
                seat.setHall(hall);
                seat.setRowLetter(rowLetter);
                seat.setSeatNumber(seatNumber);
                seat.setGridRow(row + 1); // grid positions are 1-based (CSS grid)
                seat.setGridCol(seatNumber);
                seat.setSeatType(seatType);
                seatsToCreate.add(seat);
            }
        }

        hall.setGridRows(totalRows);
        hall.setGridCols(requestDto.getSeatsPerRow());
        hallRepository.save(hall);

        List<Seat> savedSeats = seatRepository.saveAll(seatsToCreate);

        return savedSeats.stream()
                .map(this::mapToResponseDto)
                .toList();
    }

    // check this TODO
    private String generateRowLetter(int index) {
        StringBuilder sb = new StringBuilder();
        int n = index;
        do {
            sb.insert(0, (char) ('A' + (n % 26)));
            n = (n / 26) - 1;
        } while (n >= 0);
        return sb.toString();
    }

    public void deleteSeat(Long id) {
        if (!seatRepository.existsById(id)) {
            throw new RuntimeException("Seat not found with id: " + id);
        }
        seatRepository.deleteById(id);
    }
}