package com.dinitro.authentication.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.dinitro.authentication.auth.MeResponseDTO;
import com.dinitro.authentication.infra.security.TokenService;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/user")

public class UserController {
    private UserRepository userRepository;
    private TokenService tokenService;

    public UserController(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponseDTO> postMethodName(@RequestHeader("Authorization") String header) {
        String accessToken = header.replace("Bearer ", "");
        UserDetails user = userRepository.findByLogin(tokenService.extractUsername(accessToken));
        User responseUser = (User) user;

        return ResponseEntity.ok().body(new MeResponseDTO(responseUser.getName(), responseUser.getLogin(), responseUser.getRole()));
    }

    @GetMapping("/list")
    public List<UserListItemResponseDTO> list() {
        List<User> usersList = userRepository.findAll();
        
        return usersList
            .stream()
            .map(user -> new UserListItemResponseDTO(user.getName(), user.getLogin(), user.getRole()))
            .toList();
    }
}
