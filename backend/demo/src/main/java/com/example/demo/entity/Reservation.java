package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.auth.entity.User;
import com.example.demo.enums.ReservationStatus;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String reference; // UUID, used in the email link - reservation num for user (never exposes id`)

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status; // PENDING, PAID, EXPIRED, CANCELLED

    private BigDecimal totalPrice;

    private LocalDateTime expiresAt; // to hold the seats while selected

    @ManyToOne
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    @OneToOne(mappedBy = "reservation", cascade = CascadeType.ALL)
    private Payment payment;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservationSeat> reservationSeats;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}