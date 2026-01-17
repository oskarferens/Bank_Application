package com.bank.security.jwt;

import com.bank.security.auth.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY = "SUPER_SECRET_KEY_SUPER_SECRET_KEY_32_CHARS";
    private static final long EXPIRATION_TIME_MS = 1000 * 60 * 60; // 1h
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    /// Generates JWT for logged-in user
    public String generateToken(CustomUserDetails userDetails) {

        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // login
                .claim("userId", userDetails.getUserId())
                .claim("role", userDetails.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /// Extracts the login (username) from the token
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    /// Checks whether the token is valid and not expired
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
