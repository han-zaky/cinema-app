package com.example.demo.user.controller;


import com.example.demo.user.entity.User;
import com.example.demo.user.service.UserService;
import com.example.demo.auth.dto.AuthResponse;
import com.example.demo.user.dto.UserResponseDto;
import com.example.demo.user.dto.UserRequestDto;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public AuthResponse me(Authentication authentication) {
        User user = userService.findByEmail(authentication.getName()).orElseThrow();
        return new AuthResponse(user.getEmail(), user.getFirstName(), user.getLastName(), user.getRole());
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    // TODO
    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return null; // userService.getUserById(id);
    }

    @PostMapping
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto requestDto) {
        return userService.createUser(requestDto);
    }

    @PatchMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody UserRequestDto requestDto) {
        return userService.updateUser(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}