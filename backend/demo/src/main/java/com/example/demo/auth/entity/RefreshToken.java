package com.example.demo.auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import com.example.demo.auth.dto.RotationResult;

import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

@Entity
@Getter
@Setter
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String tokenHash; // nikdy token v plaintextu -> hash

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant expiresAt; // přesnost na milisekundy

    @Column(nullable = false)
    private boolean revoked = false; // při změně hesla nebo odhlášení

    private UUID replacedByToken; // null = je aktuální/platný
    private UUID familyId; // pro zneplatnění všech klíčů -> 1 session -> třeba laptop

    private String deviceInfo; // volitelné: "Chrome on Windows", User-Agent apod.

    private String ipAddress;

    public boolean isValid() {
        return !revoked && replacedByToken == null && expiresAt.isAfter(Instant.now());
    }

    public boolean isReused() {
        return replacedByToken != null;
    }

    public boolean isRevoked() {
        return this.revoked;
    }

    public void revoke() {
        this.revoked = true;
    }

    public RotationResult rotate(String deviceInfo, String ipAddress) {

        // 1. nový hash
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
        String hash = DigestUtils.sha256Hex(rawToken);

        // 2. nový token
        RefreshToken next = new RefreshToken();
        next.setId(UUID.randomUUID());
        next.setTokenHash(hash);
        next.setUser(this.user);
        next.setFamilyId(this.familyId);
        next.setExpiresAt(this.expiresAt);
        next.setDeviceInfo(deviceInfo);
        next.setIpAddress(ipAddress);

        // 3. starý token je replaced
        this.replacedByToken = next.getId();

        return new RotationResult(next, rawToken);
    }

}