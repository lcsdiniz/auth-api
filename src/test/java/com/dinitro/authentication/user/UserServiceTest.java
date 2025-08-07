package com.dinitro.authentication.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dinitro.authentication.adapters.in.rest.dto.MeResponseDTO;
import com.dinitro.authentication.adapters.in.rest.dto.UserListItemResponseDTO;
import com.dinitro.authentication.application.user.UserService;
import com.dinitro.authentication.core.user.User;
import com.dinitro.authentication.core.user.UserRole;
import com.dinitro.authentication.infrastructure.persistence.user.UserRepositoryImpl;
import com.dinitro.authentication.infrastructure.security.TokenService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepositoryImpl userRepository;
    @Mock
    private TokenService tokenService;
    @InjectMocks
    private UserService userService;

    private static final String REFRESH_TOKEN = "refresh-token";
    private static final String BEARER_HEADER = "Bearer " + REFRESH_TOKEN;
    private static final User REGISTERED_USER = new User("User Name", "userLogin", "password", UserRole.USER);
    private static final User REGISTERED_ADMIN = new User("Admin Name", "adminLogin", "password", UserRole.ADMIN);

    @Test
    void shouldShowLoggedUserData() {
        when(tokenService.extractUsername(any())).thenReturn("login");
        when(userRepository.findByLogin("login")).thenReturn(REGISTERED_USER);
        MeResponseDTO dto = userService.me(BEARER_HEADER);

        assertEquals(dto.name(), REGISTERED_USER.getName());
        assertEquals(dto.login(), REGISTERED_USER.getLogin());
        assertEquals(dto.role(), REGISTERED_USER.getRole());
    }

    @Test
    void shouldShowAllRegisteredUsers() {
        List<User> allUsers = new ArrayList<User>();
        allUsers.add(REGISTERED_USER);
        allUsers.add(REGISTERED_ADMIN);

        List<UserListItemResponseDTO> expectedList = new ArrayList<UserListItemResponseDTO>();
        expectedList.add(new UserListItemResponseDTO(REGISTERED_USER.getName(), REGISTERED_USER.getLogin(), REGISTERED_USER.getRole()));
        expectedList.add(new UserListItemResponseDTO(REGISTERED_ADMIN.getName(), REGISTERED_ADMIN.getLogin(), REGISTERED_ADMIN.getRole()));

        when(userRepository.findAll()).thenReturn(allUsers);
        List<UserListItemResponseDTO> response = userService.list();

        assertEquals(expectedList, response);
    }
}
