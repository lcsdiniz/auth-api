package com.dinitro.authentication.adapters.in.rest.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthDTO(
    @NotBlank @NotNull String login,
    @NotBlank @NotNull @Length(min = 6) String password
) {}
