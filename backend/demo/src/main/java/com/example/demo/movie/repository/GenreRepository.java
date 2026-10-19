package com.example.demo.movie.repository;


import com.example.demo.movie.entity.Genre;
import java.util.Collection;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Set<Genre> findByIdIn(Collection<Long> ids);
}