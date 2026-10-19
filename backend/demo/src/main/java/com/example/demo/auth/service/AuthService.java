package com.example.demo.auth.service;

import com.example.demo.auth.dto.LoginRequest;
import com.example.demo.auth.dto.LoginResponse;
import com.example.demo.user.repository.UserRepository;
import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.auth.dto.RegisterRequest;
import com.example.demo.auth.dto.RotationResult;
import com.example.demo.auth.entity.RefreshToken;
import com.example.demo.user.enums.Role;
import com.example.demo.user.entity.User;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        // pokud řádek výše nehodí exception, heslo sedí

        User user = userRepository.findByEmail(request.email())
                .orElseThrow();

        String accessToken = jwtService.generateToken(user);
        String deviceInfo = httpRequest.getHeader("User-Agent");
        String ipAddress = extractClientIp(httpRequest);
        String refreshToken = refreshTokenService.generateRefreshToken(user, deviceInfo, ipAddress);

        AuthResponse authResponse = new AuthResponse(user.getEmail(), user.getFirstName(), user.getLastName(),
                user.getRole());

        return new LoginResponse(authResponse, accessToken, refreshToken);
    }

    public LoginResponse refreshAccessToken(String rawRefreshToken, HttpServletRequest httpRequest) {
        String deviceInfo = httpRequest.getHeader("User-Agent");
        String ipAddress = extractClientIp(httpRequest);

        RefreshToken stored = refreshTokenService.validateRefreshToken(rawRefreshToken);
        User user = stored.getUser();

        String newAccessToken = jwtService.generateToken(user);

        RotationResult result = refreshTokenService.rotate(rawRefreshToken, deviceInfo, ipAddress);
        AuthResponse authResponse = new AuthResponse(user.getEmail(), user.getFirstName(), user.getLastName(),
                user.getRole());
        // nutná rotace -----
        return new LoginResponse(authResponse, newAccessToken, result.rawRefreshToken());
    }

    public void logout(String rawRefreshToken) {
        refreshTokenService.revokeToken(rawRefreshToken);
    }

    public LoginResponse register(RegisterRequest request, HttpServletRequest httpRequest) {
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setRole(Role.USER);
        userRepository.save(user);

        String accessToken = jwtService.generateToken(user);
        String ipAddress = extractClientIp(httpRequest);
        String deviceInfo = httpRequest.getHeader("User-Agent");
        String refreshToken = refreshTokenService.generateRefreshToken(user, deviceInfo, ipAddress);

        AuthResponse authResponse = new AuthResponse(user.getEmail(), user.getFirstName(), user.getLastName(),
                user.getRole());

        return new LoginResponse(authResponse, accessToken, refreshToken);
    }

    private String extractClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

}