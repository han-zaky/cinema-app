package com.example.demo.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.dto.auth.AuthResponse;
import com.example.demo.dto.auth.LoginRequest;
import com.example.demo.dto.auth.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        // pokud řádek výše nehodí exception, heslo sedí

        UserDetails user = userRepository.findByEmail(request.email())
                .orElseThrow();

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    public AuthResponse register(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password())); // NIKDY neukládej heslo v plain textu
        user.setRole(Role.USER);
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}