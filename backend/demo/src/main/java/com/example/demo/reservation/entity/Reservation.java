package com.example.demo.reservation.entity;

import com.example.demo.reservation.enums.ReservationStatus;

import com.example.demo.payment.entity.Payment;
import com.example.demo.screening.entity.Screening;
import com.example.demo.user.entity.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(optional = true) // for guest user
    @JoinColumn(name = "user_id")
    private User user;

    // Snapshot of who the tickets were sold to (always filled)
    @Column(nullable = false)
    private String customerEmail;
    @Column(nullable = false)
    private String customerFirstName;
    @Column(nullable = false)
    private String customerLastName;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant expiresAt; // to hold the seats while selected

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status; // PENDING, PAID, EXPIRED, CANCELLED

    private BigDecimal totalPrice;

    @ManyToOne
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Payment payment;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationSeat> reservationSeats;

}