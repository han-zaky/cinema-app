package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.user.UserRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
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