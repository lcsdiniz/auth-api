package com.dinitro.authentication.core.user;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository {
    UserDetails findByLogin(String login);
    List<User> findAll();
    void save(User user);
}
