package com.example.demo.reservation.repository;

import com.example.demo.reservation.entity.ReservationSeat;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationSeatRepository extends JpaRepository<ReservationSeat, Long> {
    List<ReservationSeat> findByReservation_Screening_Id(Long screeningId);

    boolean existsByReservation_Screening_IdAndSeat_Id(Long screeningId, Long seatId);

    @Query("""
            select rs.seat.id from ReservationSeat rs
            where rs.reservation.screening.id = :screeningId
              and (rs.reservation.status = com.example.demo.reservation.enums.ReservationStatus.PAID
                   or (rs.reservation.status = com.example.demo.reservation.enums.ReservationStatus.PENDING
                       and rs.reservation.expiresAt > :now))
            """)
    List<Long> findTakenSeatIds(@Param("screeningId") Long screeningId, @Param("now") Instant now);
}
