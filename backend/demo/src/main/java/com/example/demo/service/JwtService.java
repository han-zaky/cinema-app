package com.example.demo.service;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}") // např. 86400000 (24h v ms)
    private long jwtExpiration;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder() // builder vytváří token
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey())
                .compact();
    }

    // zjištění vlastníka tokenu
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // ověří existujícího Usera a expiraci tokenu
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // porovnání data + času expirace
    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // rozbalení klíče a porovnání se SecretKey a pak získání payloadu
    private Claims extractClaims(String token) {
        return Jwts.parser() // parser rozkládá token
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // vytvoření objektu klíče -> k podepsání nebo ověření
    // existujícího klíče
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}