package com.dinitro.authentication.adapters.in.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.dinitro.authentication.adapters.in.rest.dto.MeResponseDTO;
import com.dinitro.authentication.adapters.in.rest.dto.UserListItemResponseDTO;
import com.dinitro.authentication.application.user.UserService;

import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/user")

public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public MeResponseDTO me(@RequestHeader("Authorization") String header) {
        return userService.me(header);
    }

    @GetMapping("/list")
    public List<UserListItemResponseDTO> list() {
        return userService.list();
    }
}
