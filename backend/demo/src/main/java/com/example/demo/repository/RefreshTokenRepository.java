package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findAllByUserAndRevokedFalse(User user);

    void deleteAllByUserAndRevokedFalse(User user); // pro "odhlásit všechna zařízení"
}