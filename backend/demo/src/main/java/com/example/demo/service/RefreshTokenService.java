package com.example.demo.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.exception.InvalidTokenException;
import com.example.demo.repository.RefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}") // např. 604800000 (7 dní v ms)
    private long refreshExpiration;

    public String generateRefreshToken(User user, String deviceInfo) {
        String rawToken = generateSecureRandomToken();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setTokenHash(hashToken(rawToken));
        refreshToken.setUser(user);
        refreshToken.setExpiresAt(Instant.now().plusMillis(refreshExpiration));
        refreshToken.setDeviceInfo(deviceInfo);

        refreshTokenRepository.save(refreshToken);

        return rawToken; // tohle (a NE hash) jde klientovi
    }

    public RefreshToken validateRefreshToken(String rawToken) {
        RefreshToken token = refreshTokenRepository.findByTokenHash(hashToken(rawToken))
                .orElseThrow(() -> new InvalidTokenException("Neplatný refresh token"));

        if (token.isRevoked()) {
            throw new InvalidTokenException("Token byl odvolán");
        }
        if (token.getExpiresAt().isBefore(Instant.now())) {
            throw new InvalidTokenException("Token vypršel");
        }
        return token;
    }

    public void revokeToken(String rawToken) {
        refreshTokenRepository.findByTokenHash(hashToken(rawToken))
                .ifPresent(token -> {
                    token.setRevoked(true);
                    refreshTokenRepository.save(token);
                });
    }

    public void revokeAllUserTokens(User user) {
        refreshTokenRepository.deleteAllByUserAndRevokedFalse(user);
    }

    private String generateSecureRandomToken() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private String hashToken(String rawToken) {
        return DigestUtils.sha256Hex(rawToken); // z org.apache.commons.codec.digest
    }
}