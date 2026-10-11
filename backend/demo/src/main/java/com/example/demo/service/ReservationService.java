package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.auth.entity.User;
import com.example.demo.dto.reservation.ReservationRequestDto;
import com.example.demo.dto.reservation.ReservationResponseDto;
import com.example.demo.entity.*;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.enums.ReservationStatus;
import com.example.demo.repository.*;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ReservationService {

        private final ReservationRepository reservationRepository;
        private final ReservationSeatService reservationSeatService;
        private final ScreeningService screeningService;
        private final SeatService seatService;
        private final PaymentService paymentService;

        @Transactional
        public ReservationResponseDto createReservation(ReservationRequestDto requestDto, User user) {
                // User user = userService.getUserById(requestDto.getUserId());

                Screening screening = screeningService.getScreeningById(requestDto.screeningId());

                // 1. build and save reservation
                Reservation r = buildReservation(user, requestDto, screening);
                Reservation savedReservation = reservationRepository.save(r);

                // 2. save reservation seats
                List<Seat> seats = getReservedSeats(requestDto.seatIds(), screening);

                for (Seat seat : seats) {
                        reservationSeatService.createReservationSeat(savedReservation, seat);
                }

                // 3. making seat labels and calculating total price
                List<String> seatLabels = getSeatLabels(reservationSeatService.getReserSeats(r));
                BigDecimal totalPrice = screening.getBasePrice().multiply(BigDecimal.valueOf(seats.size()));

                // 4. creating a payment
                paymentService.createPayment(savedReservation, totalPrice);

                return new ReservationResponseDto(
                                savedReservation.getId(),
                                user != null ? user.getId() : null,
                                screening.getId(),
                                seatLabels,
                                totalPrice,
                                PaymentStatus.PENDING.name(),
                                savedReservation.getCreatedAt());
        }

        public ReservationResponseDto getReservationById(Long id) {
                Reservation r = reservationRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Reservation not found with id: " + id));

                List<String> seatLabels = getSeatLabels(reservationSeatService.getReserSeats(r));

                BigDecimal totalPrice = calculateTotal(r, seatLabels);

                String paymentStatus = r.getPayment() != null
                                ? r.getPayment().getStatus().name()
                                : "UNKNOWN";

                return new ReservationResponseDto(
                                r.getId(),
                                r.getUser() != null ? r.getUser().getId() : null,
                                r.getScreening().getId(),
                                seatLabels,
                                totalPrice,
                                paymentStatus,
                                r.getCreatedAt());
        }

        // TODO -> flat price
        public BigDecimal calculateTotal(Reservation res, List<String> seatLabels) {
                return res.getScreening().getBasePrice().multiply(BigDecimal.valueOf(seatLabels.size()));
        }

        public List<String> getSeatLabels(List<ReservationSeat> resSeats) {
                return resSeats.stream()
                                .map(s -> s.getLabel())
                                .toList();
        }

        public Reservation buildReservation(User user, ReservationRequestDto req, Screening scr) {
                Reservation r = new Reservation();
                r.setReference(UUID.randomUUID().toString());
                r.setUser(user); // null for guests
                r.setScreening(scr);

                if (user != null) { // logged in: copy from the account
                        r.setCustomerEmail(user.getEmail());
                        r.setCustomerFirstName(user.getFirstName());
                        r.setCustomerLastName(user.getLastName());
                } else { // guest: take it from the request
                        r.setCustomerEmail(req.customerEmail());
                        r.setCustomerFirstName(req.customerFirstName());
                        r.setCustomerLastName(req.customerLastName());
                }
                r.setStatus(ReservationStatus.PENDING);
                r.setExpiresAt(LocalDateTime.now().plusMinutes(10));
                r.setCreatedAt(LocalDateTime.now());

                return r;
        }

        // todo -> list typing
        public List<Seat> getReservedSeats(List<Long> seatIds, Screening screening) {
                return seatIds.stream().map(seatId -> {
                        Seat seat = seatService.getSeatById(seatId);
                        if (!seat.getHall().getId().equals(screening.getHall().getId())) {
                                throw new IllegalArgumentException(
                                                "Seat " + seatId + " does not belong to the hall of this screening");
                        }
                        boolean alreadyTaken = reservationSeatService.reservationSeatExists(screening.getId(), seatId);
                        if (alreadyTaken) {
                                throw new IllegalStateException(
                                                "Seat " + seat.getRowLetter() + seat.getSeatNumber()
                                                                + " is already reserved for this screening");
                        }
                        return seat;
                }).toList();
        }

}