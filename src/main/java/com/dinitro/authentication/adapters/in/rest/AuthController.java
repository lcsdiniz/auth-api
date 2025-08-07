package com.dinitro.authentication.adapters.in.rest;

import org.springframework.web.bind.annotation.RestController;

import com.dinitro.authentication.adapters.in.rest.dto.AuthDTO;
import com.dinitro.authentication.adapters.in.rest.dto.LoginResponseDTO;
import com.dinitro.authentication.adapters.in.rest.dto.RegisterDTO;
import com.dinitro.authentication.application.auth.AuthService;

import jakarta.validation.Valid;

import java.util.Map;

import org.springframework.http.HttpStatus;
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
