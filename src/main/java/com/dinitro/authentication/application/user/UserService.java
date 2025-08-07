package com.dinitro.authentication.application.user;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dinitro.authentication.adapters.in.rest.dto.MeResponseDTO;
import com.dinitro.authentication.adapters.in.rest.dto.UserListItemResponseDTO;
import com.dinitro.authentication.core.user.User;
import com.dinitro.authentication.core.user.UserRepository;
import com.dinitro.authentication.infrastructure.security.TokenService;

@Service
public class UserService implements UserDetailsService {
    private UserRepository userRepository;
    private TokenService tokenService;

    public UserService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username);
    }

    public MeResponseDTO me(String header) {
        String accessToken = header.replace("Bearer ", "");
        UserDetails user = userRepository.findByLogin(tokenService.extractUsername(accessToken));
        User responseUser = (User) user;

        return new MeResponseDTO(responseUser.getName(), responseUser.getLogin(), responseUser.getRole());
    }

    public List<UserListItemResponseDTO> list() {
        List<User> usersList = userRepository.findAll();
        
        return usersList
            .stream()
            .map(user -> new UserListItemResponseDTO(user.getName(), user.getLogin(), user.getRole()))
            .toList();
    }
}
