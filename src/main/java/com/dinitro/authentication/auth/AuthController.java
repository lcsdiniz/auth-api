package com.dinitro.authentication.auth;

import org.springframework.web.bind.annotation.RestController;

import com.dinitro.authentication.exception.LoginAlreadyExistsException;
import com.dinitro.authentication.infra.security.TokenService;
import com.dinitro.authentication.user.User;
import com.dinitro.authentication.user.UserRepository;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;


@Validated
@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid RegisterDTO data) {
        authService.register(data);
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody @Valid AuthDTO data) {
        return authService.login(data);
    }

    @PostMapping("/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String header) {
        return authService.refresh(header);
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String header) {
        authService.logout(header);
    }
}
