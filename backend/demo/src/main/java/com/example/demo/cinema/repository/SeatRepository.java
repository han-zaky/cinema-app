package com.example.demo.cinema.repository;


import com.example.demo.cinema.entity.Seat;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByHallId(Long id);
}
