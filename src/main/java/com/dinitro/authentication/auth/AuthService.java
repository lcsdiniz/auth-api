package com.dinitro.authentication.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dinitro.authentication.exception.ExpiredTokenException;
import com.dinitro.authentication.exception.LoginAlreadyExistsException;
import com.dinitro.authentication.infra.security.TokenService;
import com.dinitro.authentication.user.User;
import com.dinitro.authentication.user.UserRepository;

@Service
public class AuthService {
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;

    AuthService(UserRepository userRepository, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public void register(RegisterDTO data) {
        if(this.userRepository.findByLogin(data.login()) != null) throw new LoginAlreadyExistsException(data.login());
        
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User user = new User(data.name(), data.login(), encryptedPassword, data.role());
        this.userRepository.save(user);
    }

    public LoginResponseDTO login(AuthDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        var accessToken = tokenService.generateAccessToken((User) auth.getPrincipal());
        var refreshToken = tokenService.generateRefreshToken((User) auth.getPrincipal());

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    public Map<String, Object> refresh(String header) {
        String refreshToken = header.replace("Bearer ", "");

        var login = tokenService.extractUsername(refreshToken);
        UserDetails user = userRepository.findByLogin(login);

        if(!tokenService.isTokenValid(refreshToken, login)) {
            throw new ExpiredTokenException();
        }

        String newAccessToken = tokenService.generateAccessToken((User) user);
        Map<String, Object> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        
        return response;
    }

    public void logout(String header) {
        String refreshToken = header.replace("Bearer ", "");
        tokenService.deleteToken(refreshToken);
    }
}
