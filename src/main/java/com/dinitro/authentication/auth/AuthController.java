package com.dinitro.authentication.auth;

import org.springframework.web.bind.annotation.RestController;

import com.dinitro.authentication.infra.security.TokenService;
import com.dinitro.authentication.user.User;
import com.dinitro.authentication.user.UserRepository;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private TokenService tokenService;

    public AuthController(UserRepository userRepository, TokenService tokenService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody @Valid RegisterDTO data) {
        if(this.userRepository.findByLogin(data.login()) != null) return ResponseEntity.badRequest().build();
        
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        User user = new User(data.login(), encryptedPassword, data.role());
        this.userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = authenticationManager.authenticate(usernamePassword);
        var accessToken = tokenService.generateToken((User) auth.getPrincipal(), TokenType.ACCESS);
        var refreshToken = tokenService.generateToken((User) auth.getPrincipal(), TokenType.REFRESH);

        return ResponseEntity.ok().body(new LoginResponseDTO(accessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestHeader("Authorization") String header) {
        String refreshToken = header.replace("Bearer ", "");
        
        var login = tokenService.extractUsername(refreshToken);
        UserDetails user = userRepository.findByLogin(login);

        if(!tokenService.isTokenValid(refreshToken, login)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String newAccessToken = tokenService.generateToken((User) user, TokenType.ACCESS);

        return ResponseEntity.ok().body(newAccessToken);
    }
}
