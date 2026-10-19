package com.example.demo.movie.repository;


import com.example.demo.movie.entity.Movie;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    @EntityGraph(attributePaths = "genres")
    @Override
    List<Movie> findAll();

    @EntityGraph(attributePaths = "genres")
    Optional<Movie> findWithGenresById(Long id);

}
