package com.example.demo.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.LoginResponse;

import com.example.demo.auth.dto.RegisterRequest;
import com.example.demo.auth.service.AuthService;
import com.example.demo.auth.util.AuthCookieFactory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthCookieFactory cookieFactory;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        LoginResponse loginRes = authService.login(request, httpRequest);
        return withAuthCookies(loginRes);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest) {
        LoginResponse registerRes = authService.register(request, httpRequest);
        return withAuthCookies(registerRes);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@CookieValue(name = "refresh_token") String refreshToken,
            HttpServletRequest httpRequest) {
        LoginResponse refreshRes = authService.refreshAccessToken(refreshToken, httpRequest);
        return withAuthCookies(refreshRes);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        System.out.print("logout!");
        if (refreshToken != null)
            authService.logout(refreshToken);

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, cookieFactory.expireAccessCookie().toString())
                .header(HttpHeaders.SET_COOKIE, cookieFactory.expireRefreshCookie().toString())
                .build();
    }

    private ResponseEntity<AuthResponse> withAuthCookies(LoginResponse response) {
        ResponseCookie accessCookie = cookieFactory.createAccessCookie(response.accessToken());
        ResponseCookie refreshCookie = cookieFactory.createRefreshCookie(response.refreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(response.body());
    }
}