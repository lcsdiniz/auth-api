package com.dinitro.authentication.auth;

import com.dinitro.authentication.user.UserRole;

public record RegisterDTO(
    String login,
    String password,
    UserRole role
) {}
