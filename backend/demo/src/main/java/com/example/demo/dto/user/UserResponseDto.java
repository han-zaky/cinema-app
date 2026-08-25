package com.example.demo.dto.user;

import com.example.demo.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
}