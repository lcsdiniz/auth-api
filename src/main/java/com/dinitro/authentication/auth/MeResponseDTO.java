package com.dinitro.authentication.auth;

import com.dinitro.authentication.user.UserRole;

public record MeResponseDTO(String name, String login, UserRole role) {

}
