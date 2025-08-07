package com.dinitro.authentication.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.dinitro.authentication.adapters.in.rest.dto.AuthDTO;
import com.dinitro.authentication.adapters.in.rest.dto.LoginResponseDTO;
import com.dinitro.authentication.adapters.in.rest.dto.RegisterDTO;
import com.dinitro.authentication.application.auth.AuthService;
import com.dinitro.authentication.core.exceptions.LoginAlreadyExistsException;
import com.dinitro.authentication.core.user.User;
import com.dinitro.authentication.core.user.UserRole;
import com.dinitro.authentication.infrastructure.persistence.user.UserRepositoryImpl;
import com.dinitro.authentication.infrastructure.security.TokenService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepositoryImpl userRepository;
    
    @Mock
    private AuthenticationManager authenticationManager;
    
    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthService authService;

    private static final String ACCESS_TOKEN = "access-token";
    private static final String REFRESH_TOKEN = "refresh-token";
    private static final String BEARER_HEADER = "Bearer " + REFRESH_TOKEN;
    private static final User REGISTERED_USER = new User("registeredUser", "registeredUser", "hashedPassword", UserRole.USER);
    
    @Test
    void shouldRegisterNewUser() {
        when(userRepository.findByLogin("newUser")).thenReturn(null);

        RegisterDTO dto = new RegisterDTO("New User", "newUser", "password", UserRole.USER);
        authService.register(dto);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserAlreadyExists() {
        when(userRepository.findByLogin("registeredUser")).thenReturn(REGISTERED_USER);

        RegisterDTO dto = new RegisterDTO("registeredUser", "registeredUser", "password", UserRole.USER);
        
        LoginAlreadyExistsException exception = assertThrows(LoginAlreadyExistsException.class, () -> {
            authService.register(dto);
        });
        
        assertTrue(exception.getMessage().contains("Login \'" + dto.login() + "\' already exists."));
    }

    @Test
    void registeredUserShouldLogin() {
        AuthDTO dto = new AuthDTO("registeredUser", "password");
        
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(REGISTERED_USER, null, REGISTERED_USER.getAuthorities());

        when(authenticationManager.authenticate(any())).thenReturn(authRequest);
        when(tokenService.generateAccessToken((User) authRequest.getPrincipal())).thenReturn(ACCESS_TOKEN);
        when(tokenService.generateRefreshToken((User) authRequest.getPrincipal())).thenReturn(REFRESH_TOKEN);

        LoginResponseDTO response = authService.login(dto);

        assertEquals(ACCESS_TOKEN, response.accessToken());
        assertEquals(REFRESH_TOKEN, response.refreshToken());
    }

    @Test
    void shouldReturnNewRefreshToken() {
        when(tokenService.extractUsername(any())).thenReturn("registeredUser");
        when(userRepository.findByLogin("registeredUser")).thenReturn(REGISTERED_USER);
        when(tokenService.isTokenValid(any(), any())).thenReturn(true);
        when(tokenService.generateAccessToken(any(User.class))).thenReturn("new-access-token");

        Map<String, Object> response = authService.refresh(BEARER_HEADER);

        assertEquals("new-access-token", response.get("accessToken"));
    }

    @Test
    void shouldCallDeleteTokenWithCorrectValue() {
        authService.logout(BEARER_HEADER);

        verify(tokenService).deleteToken(REFRESH_TOKEN);
    }
}
