package com.example.demo.auth.service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.auth.dto.RotationResult;
import com.example.demo.auth.entity.RefreshToken;
import com.example.demo.auth.entity.User;
import com.example.demo.auth.repository.RefreshTokenRepository;
import com.example.demo.exception.InvalidTokenException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh-expiration}") // např. 604800000 (7 dní v ms)
    private long refreshExpiration;

    public String generateRefreshToken(User user, String deviceInfo, String ipAddress) {
        String rawToken = generateSecureRandomToken();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setTokenHash(hashToken(rawToken));
        refreshToken.setUser(user);
        refreshToken.setFamilyId(UUID.randomUUID());
        refreshToken.setExpiresAt(Instant.now().plusMillis(refreshExpiration));
        refreshToken.setDeviceInfo(deviceInfo);
        refreshToken.setIpAddress(ipAddress);

        refreshTokenRepository.save(refreshToken);

        return rawToken; // tohle (a NE hash) jde klientovi
    }

    public RotationResult rotate(String rawToken, String deviceInfo, String ipAddress) {
        RefreshToken stored = validateRefreshToken(rawToken);

        RotationResult result = stored.rotate(deviceInfo, ipAddress);

        refreshTokenRepository.save(stored); // změna familyId
        refreshTokenRepository.save(result.newToken()); // nový refresh token

        return result;
    }

    public RefreshToken validateRefreshToken(String rawToken) {
        RefreshToken token = refreshTokenRepository.findByTokenHash(hashToken(rawToken))
                .orElseThrow(() -> new InvalidTokenException("Neplatný refresh token"));

        if (token.isReused()) {
            invalidateFamily(token.getFamilyId());
            throw new InvalidTokenException("Token byl znovu použit - rodina zneplatněna!");
        }
        if (token.isRevoked()) {
            throw new InvalidTokenException("Token byl odvolán!");
        }
        if (!token.isValid()) {
            throw new InvalidTokenException("Token je neplatný nebo vypršel!");
        }
        return token;
    }

    // LOGOUT -> zneplatní session
    @Transactional
    public void revokeToken(String rawToken) {
        refreshTokenRepository.findByTokenHash(hashToken(rawToken))
                .ifPresent(token -> {
                    invalidateFamily(token.getFamilyId());
                });
    }

    // třeba změna hesla
    @Transactional
    public void revokeAllUserTokens(User user) {
        refreshTokenRepository.revokeAllByUserAndRevokedFalse(user);
    }

    private String generateSecureRandomToken() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private String hashToken(String rawToken) {
        return DigestUtils.sha256Hex(rawToken); // z org.apache.commons.codec.digest
    }

    private void invalidateFamily(UUID familyId) {
        refreshTokenRepository.revokeTokensByFamily(familyId);
    }

}