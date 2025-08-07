package com.dinitro.authentication.infrastructure.persistence.user;

import org.springframework.stereotype.Repository;

import com.dinitro.authentication.core.user.User;
import com.dinitro.authentication.core.user.UserRepository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User findByLogin(String login) {
        return jpaUserRepository.findByLogin(login).orElse(null);
    }

    @Override
    public void save(User user) {
        jpaUserRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll();
    }
}