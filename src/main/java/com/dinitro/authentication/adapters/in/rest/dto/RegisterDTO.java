package com.dinitro.authentication.adapters.in.rest.dto;


import org.hibernate.validator.constraints.Length;

import com.dinitro.authentication.core.user.UserRole;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDTO(
    @NotBlank @NotNull String name,
    @NotBlank @NotNull String login,
    @NotBlank @NotNull @Length(min = 6) String password,
    @NotNull UserRole role
) {}
