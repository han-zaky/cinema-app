package com.example.demo.reservation.service;

import com.example.demo.reservation.entity.Reservation;
import com.example.demo.reservation.entity.ReservationSeat;
import com.example.demo.reservation.enums.ReservationStatus;


import com.example.demo.reservation.repository.ReservationRepository;
import com.example.demo.cinema.service.SeatService;
import com.example.demo.payment.service.PaymentService;
import com.example.demo.reservation.dto.ReservationRequestDto;
import com.example.demo.reservation.dto.ReservationResponseDto;
import com.example.demo.screening.service.ScreeningService;
import com.example.demo.user.entity.User;
import com.example.demo.cinema.entity.Seat;
import com.example.demo.payment.enums.PaymentStatus;
import com.example.demo.screening.entity.Screening;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

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
                r.setExpiresAt(Instant.now().plus(Duration.ofMinutes(10)));
                r.setCreatedAt(Instant.now());

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