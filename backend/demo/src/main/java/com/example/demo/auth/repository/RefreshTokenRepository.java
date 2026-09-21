package com.example.demo.auth.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.example.demo.auth.entity.RefreshToken;
import com.example.demo.auth.entity.User;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    List<RefreshToken> findAllByUserAndRevokedFalse(User user);

    @Modifying
    @Query("UPDATE RefreshToken t SET t.revoked = true WHERE t.user = :user AND t.revoked = false")
    void revokeAllByUserAndRevokedFalse(@Param("user") User user);

    @Modifying
    @Query("UPDATE RefreshToken t SET t.revoked = true WHERE t.familyId = :familyId AND t.revoked = false")
    void revokeTokensByFamily(@Param("familyId") UUID familyId);
}