package com.dinitro.authentication.adapters.in.rest.mapper;

import com.dinitro.authentication.adapters.in.rest.dto.LoginResponseDTO;
import com.dinitro.authentication.adapters.in.rest.dto.RegisterDTO;
import com.dinitro.authentication.core.user.User;

public class AuthMapper {
    private AuthMapper() {

    }

    public static User toUser(RegisterDTO registerDTO, String encryptedPassword) {
        return new User(registerDTO.name(), registerDTO.login(), encryptedPassword, registerDTO.role());
    }

    public static LoginResponseDTO toLoginResponse(String accessToken, String refreshToken) {
        return new LoginResponseDTO(accessToken, refreshToken);
    }
}
