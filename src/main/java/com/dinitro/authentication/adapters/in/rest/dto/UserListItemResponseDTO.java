package com.dinitro.authentication.adapters.in.rest.dto;

import com.dinitro.authentication.core.user.UserRole;

public record UserListItemResponseDTO(String name, String login, UserRole role) {}
