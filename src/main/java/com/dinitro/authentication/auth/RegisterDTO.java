package com.dinitro.authentication.auth;

import com.dinitro.authentication.user.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
    @NotBlank @NotNull String name,
    @NotBlank @NotNull String login,
    @NotBlank @NotNull String password,
    @NotNull UserRole role
) {}
