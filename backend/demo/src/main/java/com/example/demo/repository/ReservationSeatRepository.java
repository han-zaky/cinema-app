package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ReservationSeat;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {
    List<ReservationSeat> findByReservation_Screening_Id(Long screeningId);

    boolean existsByReservation_Screening_IdAndSeat_Id(Long screeningId, Long seatId);
}
