package com.dinitro.authentication.infrastructure.persistence.user;

import com.dinitro.authentication.core.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, String> {
    Optional<User> findByLogin(String login);
}
