package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.reservation.ReservationRequestDto;
import com.example.demo.dto.reservation.ReservationResponseDto;
import com.example.demo.entity.*;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.repository.*;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationSeatRepository reservationSeatRepository;
    private final UserRepository userRepository;
    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto) {
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + requestDto.getUserId()));

        Screening screening = screeningRepository.findById(requestDto.getScreeningId())
                .orElseThrow(() -> new RuntimeException("Screening not found with id: " + requestDto.getScreeningId()));

        List<Seat> seats = new ArrayList<>();
        for (Long seatId : requestDto.getSeatIds()) {
            Seat seat = seatRepository.findById(seatId)
                    .orElseThrow(() -> new RuntimeException("Seat not found with id: " + seatId));

            if (!seat.getHall().getId().equals(screening.getHall().getId())) {
                throw new IllegalArgumentException(
                        "Seat " + seatId + " does not belong to the hall of this screening");
            }

            boolean alreadyTaken = reservationSeatRepository
                    .existsByReservation_Screening_IdAndSeat_Id(screening.getId(), seatId);

            if (alreadyTaken) {
                throw new IllegalStateException(
                        "Seat " + seat.getRowLetter() + seat.getSeatNumber()
                                + " is already reserved for this screening");
            }

            seats.add(seat);
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setScreening(screening);
        reservation.setCreatedAt(LocalDateTime.now());

        Reservation savedReservation = reservationRepository.save(reservation);

        List<String> seatLabels = new ArrayList<>();
        for (Seat seat : seats) {
            ReservationSeat reservationSeat = new ReservationSeat();
            reservationSeat.setReservation(savedReservation);
            reservationSeat.setSeat(seat);
            reservationSeatRepository.save(reservationSeat);

            seatLabels.add(seat.getRowLetter() + String.valueOf(seat.getSeatNumber()));
        }

        BigDecimal totalPrice = screening.getBasePrice().multiply(BigDecimal.valueOf(seats.size()));

        Payment payment = new Payment();
        payment.setReservation(savedReservation);
        payment.setAmount(totalPrice);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setProvider("MOCK_GATEWAY");

        paymentRepository.save(payment);

        return new ReservationResponseDto(
                savedReservation.getId(),
                user.getId(),
                screening.getId(),
                seatLabels,
                totalPrice,
                PaymentStatus.PENDING.name(),
                savedReservation.getCreatedAt());
    }

    public ReservationResponseDto getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));

        List<ReservationSeat> reservationSeats = reservationSeatRepository
                .findByReservation_Screening_Id(reservation.getScreening().getId())
                .stream()
                .filter(rs -> rs.getReservation().getId().equals(id))
                .toList();

        List<String> seatLabels = reservationSeats.stream()
                .map(rs -> rs.getSeat().getRowLetter() + String.valueOf(rs.getSeat().getSeatNumber()))
                .toList();

        BigDecimal totalPrice = reservation.getScreening().getBasePrice()
                .multiply(BigDecimal.valueOf(seatLabels.size()));

        String paymentStatus = reservation.getPayment() != null
                ? reservation.getPayment().getStatus().name()
                : "UNKNOWN";

        return new ReservationResponseDto(
                reservation.getId(),
                reservation.getUser().getId(),
                reservation.getScreening().getId(),
                seatLabels,
                totalPrice,
                paymentStatus,
                reservation.getCreatedAt());
    }
}