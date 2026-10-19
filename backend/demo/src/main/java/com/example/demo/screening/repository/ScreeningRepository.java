package com.example.demo.screening.repository;


import com.example.demo.screening.entity.Screening;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ScreeningRepository extends JpaRepository<Screening, Long> {
    List<Screening> findByHallId(Long hallId);
}
