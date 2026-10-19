package com.example.demo.payment.service;


import com.example.demo.payment.entity.Payment;
import com.example.demo.payment.enums.PaymentStatus;
import com.example.demo.payment.repository.PaymentRepository;
import com.example.demo.reservation.entity.Reservation;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentService {

    private final PaymentRepository paymentRepo;

    public void createPayment(Reservation reservation, BigDecimal price) {
        Payment p = new Payment();
        p.setReservation(reservation);
        p.setAmount(price);
        p.setStatus(PaymentStatus.PENDING);
        p.setProvider("MOCK_GATEWAY");

        paymentRepo.save(p);
    }

}
