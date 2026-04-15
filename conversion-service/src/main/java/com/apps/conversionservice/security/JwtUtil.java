package com.apps.conversionservice.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

@Component
public class JwtUtil {

    @Value("${jwt.secret:MySuperSecretKeyForJwt}")
    private String secret;

    @Value("${jwt.expiration-ms:86400000}") // 24 hours
    private long expirationMs;

    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return JWT.create()
                .withSubject(username)
                .withClaim("role", role)
                .withIssuedAt(now)
                .withExpiresAt(expiry)
                .sign(Algorithm.HMAC256(secret));
    }

    public boolean validateToken(String token) {
        try {
            getVerifier().verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        DecodedJWT jwt = getVerifier().verify(token);
        return jwt.getSubject();
    }

    public String getRoleFromToken(String token) {
        DecodedJWT jwt = getVerifier().verify(token);
        return jwt.getClaim("role").asString();
    }

    private JWTVerifier getVerifier() {
        return JWT.require(Algorithm.HMAC256(secret)).build();
    }
}
