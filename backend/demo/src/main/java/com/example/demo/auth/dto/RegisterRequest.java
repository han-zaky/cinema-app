package com.example.demo.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(@Email String email,
        @NotBlank @Size(min = 8, max = 64) @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).*$", message = "Heslo musí obsahovat malé, velké písmeno, číslici a speciální znak") String password,
        @NotBlank String firstName,
        @NotBlank String lastName) {
}