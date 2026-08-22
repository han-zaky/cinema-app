package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByHallId(Long id);
}
