package com.dinitro.authentication.auth;

import com.dinitro.authentication.user.UserRole;

public record RegisterDTO(
    String name,
    String login,
    String password,
    UserRole role
) {}
