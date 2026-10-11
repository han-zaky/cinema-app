package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Reservation;
import com.example.demo.entity.ReservationSeat;
import com.example.demo.entity.Seat;
import com.example.demo.repository.ReservationSeatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationSeatService {

    private final ReservationSeatRepository reservationSeatRepo;

    public void createReservationSeat(Reservation res, Seat seat) {
        ReservationSeat resSeat = new ReservationSeat();
        resSeat.setReservation(res);
        resSeat.setSeat(seat);

        reservationSeatRepo.save(resSeat);
    }

    public List<ReservationSeat> getReserSeats(Reservation res) {
        return reservationSeatRepo
                .findByReservation_Screening_Id(res.getScreening().getId())
                .stream()
                .filter(rs -> rs.getReservation().getId().equals(res.getId()))
                .toList();
    }

    public boolean reservationSeatExists(Long screenId, Long seatId) {
        return reservationSeatRepo.existsByReservation_Screening_IdAndSeat_Id(screenId, seatId);
    }

}
