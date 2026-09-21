package com.example.demo.auth.dto;

import com.example.demo.auth.entity.RefreshToken;

public record RotationResult(RefreshToken newToken, String rawRefreshToken) {
}
