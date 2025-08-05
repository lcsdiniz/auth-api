package com.dinitro.authentication.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dinitro.authentication.user.User;

import jakarta.transaction.Transactional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;
    private Algorithm algorithm;

    public TokenService(@Value("${api.security.token.secret}") String secret, TokenRepository tokenRepository) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.tokenRepository = tokenRepository;
    }

    public String generateAccessToken(User user) {
        try {
            return JWT.create()
                .withIssuer("authentication")
                .withSubject(user.getLogin())
                .withExpiresAt(LocalDateTime.now().plusWeeks(1).toInstant(ZoneOffset.of("-03:00")))
                .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    public String generateRefreshToken(User user) {
        try {
            Instant expirationDate = LocalDateTime.now().plusWeeks(1).toInstant(ZoneOffset.of("-03:00"));
            String token = JWT.create()
                .withIssuer("authentication")
                .withSubject(user.getLogin())
                .withExpiresAt(LocalDateTime.now().plusWeeks(1).toInstant(ZoneOffset.of("-03:00")))
                .sign(algorithm);

            tokenRepository.save(new Token(user, token, expirationDate));

            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    public String extractUsername(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);
        
        return jwt.getSubject();
    }

    public boolean isTokenValid(String token, String expectedUsername) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);

            return jwt.getSubject().equals(expectedUsername) && !jwt.getExpiresAt().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
    
    @Transactional
    public void deleteToken(String refreshToken) {
        var token = tokenRepository.findByValue(refreshToken).orElseThrow(null);
        tokenRepository.deleteByUser(token.getUser());
    }
}
