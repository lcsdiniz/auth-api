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
import com.dinitro.authentication.auth.TokenType;
import com.dinitro.authentication.user.User;

@Service
public class TokenService {
    private Algorithm algorithm;

    public TokenService(@Value("${api.security.token.secret}") String secret) {
        this.algorithm = Algorithm.HMAC256(secret);
    }

    public String generateToken(User user, TokenType tokenType) {
        try {
            return JWT.create()
                .withIssuer("authentication")
                .withSubject(user.getLogin())
                .withExpiresAt(generateExpirationDate(tokenType))
                .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    private Instant generateExpirationDate(TokenType tokenType) {
        var expirationDate = LocalDateTime.now();
        switch (tokenType) {
            case ACCESS:
                expirationDate = expirationDate.plusMinutes(3);
                break;
            case REFRESH:
                expirationDate = expirationDate.plusWeeks(1);
                break;
            default:
                expirationDate = expirationDate.plusDays(1);
                break;
        }

        return expirationDate.toInstant(ZoneOffset.of("-03:00"));
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
}
