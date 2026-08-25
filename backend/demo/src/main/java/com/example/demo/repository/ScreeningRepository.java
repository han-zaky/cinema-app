package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Screening;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findByHallId(Long hallId);
}
