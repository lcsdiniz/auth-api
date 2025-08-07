package com.dinitro.authentication.adapters.in.rest.mapper;

import com.dinitro.authentication.adapters.in.rest.dto.MeResponseDTO;
import com.dinitro.authentication.adapters.in.rest.dto.UserListItemResponseDTO;
import com.dinitro.authentication.core.user.User;

public class UserMapper {
    private UserMapper() {
    }

    public static MeResponseDTO toMeResponse(User user) {
        return new MeResponseDTO(user.getName(), user.getLogin(), user.getRole());
    }

    public static UserListItemResponseDTO toListItem(User user) {
        return new UserListItemResponseDTO(user.getName(), user.getLogin(), user.getRole());
    }
}
