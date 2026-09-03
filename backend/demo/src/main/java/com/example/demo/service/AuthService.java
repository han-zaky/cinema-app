package com.example.demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.dto.auth.AuthResponse;
import com.example.demo.dto.auth.LoginRequest;
import com.example.demo.dto.auth.RefreshTokenRequest;
import com.example.demo.dto.auth.RegisterRequest;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.repository.UserRepository;

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

    public AuthResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        // pokud řádek výše nehodí exception, heslo sedí

        User user = userRepository.findByEmail(request.email())
                .orElseThrow();

        String token = jwtService.generateToken(user);
        String deviceInfo = httpRequest.getHeader("User-Agent");
        String refreshToken = refreshTokenService.generateRefreshToken(user, deviceInfo);

        return new AuthResponse(token, refreshToken);
    }

    public AuthResponse refreshAccessToken(RefreshTokenRequest request) {
        RefreshToken storedToken = refreshTokenService.validateRefreshToken(request.refreshToken());
        User user = storedToken.getUser();

        String newAccessToken = jwtService.generateToken(user);

        // volitelné, viz sekce "rotace" níže
        return new AuthResponse(newAccessToken, request.refreshToken());
    }

    public void logout(RefreshTokenRequest request) {
        refreshTokenService.revokeToken(request.refreshToken());
    }

    public AuthResponse register(RegisterRequest request, HttpServletRequest httpRequest) {
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.USER);
        userRepository.save(user);

        String accessToken = jwtService.generateToken(user);
        String deviceInfo = httpRequest.getHeader("User-Agent");
        String refreshToken = refreshTokenService.generateRefreshToken(user, deviceInfo);

        return new AuthResponse(accessToken, refreshToken);
    }
}