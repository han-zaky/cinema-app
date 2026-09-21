package com.example.demo.auth.util;

import java.time.Duration;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class AuthCookieFactory {

    public ResponseCookie createAccessCookie(String token) {
        return ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(Duration.ofMinutes(15))
                .path("/")
                .build();
    }

    public ResponseCookie createRefreshCookie(String token) {
        return ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(Duration.ofDays(7))
                .path("/api/auth/refresh")
                .build();
    }

    public ResponseCookie expireAccessCookie() {
        return ResponseCookie.from("acces_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(0)
                .path("/")
                .build();
    }

    public ResponseCookie expireRefreshCookie() {
        return ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(0)
                .path("/api/auth/refresh")
                .build();
    }

}
