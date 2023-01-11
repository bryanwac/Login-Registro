package com.backlogingenerico.loginRegistro.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.backlogingenerico.loginRegistro.models.Usuario;
import com.backlogingenerico.loginRegistro.payload.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class TokenService {
    private final int expTime;
    private final String secret;
    @Autowired
    private UserService userService;

    public TokenService(@Value("${jwt.expiration}") int expTime, @Value("${jwt.secret}") String secret) {
        this.expTime = expTime;
        this.secret = secret;
    }

    public String generateToken(Authentication authentication, LoginRequest loginRequest) {
        Usuario user = userService.acharUsuarioPorEmail(loginRequest.getEmail()).orElseThrow();
        UserDetails principal = (UserDetails) user;

        return JWT.create().withIssuer("Bearer")
                .withSubject(userService.getUserByEmail(user.getEmail()).getEmail())
                .withExpiresAt(Instant.ofEpochMilli(ZonedDateTime.now(ZoneId.systemDefault()).toInstant().toEpochMilli() + expTime))
                .sign(Algorithm.HMAC256(secret));

    }
}
