package com.example.demo.cinema.repository;


import com.example.demo.cinema.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HallRepository extends JpaRepository<Hall, Long> {
}