package com.example.demo.auth.dto;

import com.example.demo.user.enums.Role;

public record AuthResponse(String email, String firstName, String lastName, Role role) {
}