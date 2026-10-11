package com.example.demo.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Payment;
import com.example.demo.entity.Reservation;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.enums.PaymentStatus;

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
