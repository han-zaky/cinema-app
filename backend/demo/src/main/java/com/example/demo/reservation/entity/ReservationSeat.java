package com.example.demo.reservation.entity;

import com.example.demo.cinema.entity.Seat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "reservation_seats", uniqueConstraints = @UniqueConstraint(columnNames = { "seat_id", "reservation_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    public String getLabel() {
        return getSeat().getRowLetter() + String.valueOf(getSeat().getSeatNumber());
    }
}