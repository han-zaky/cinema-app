package com.example.demo.reservation.service;


import com.example.demo.reservation.entity.Reservation;
import com.example.demo.reservation.entity.ReservationSeat;
import com.example.demo.reservation.repository.ReservationSeatRepository;
import com.example.demo.cinema.entity.Seat;

import java.util.List;

import org.springframework.stereotype.Service;

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
