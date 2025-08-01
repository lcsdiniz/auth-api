package com.dinitro.authentication.infra.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dinitro.authentication.user.User;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByValue(String value);
    void deleteByUser(User user);
}
