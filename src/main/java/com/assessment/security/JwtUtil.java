package com.assessment.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.Base64;

/**
 * JWT Utility Class
 * Provides methods for generating, validating, and extracting information from JWT tokens.
 */
@Component
public class JwtUtil {
    
    private final Key signingKey;

    /**
     * Constructor to initialize the signing key from the provided secret.
     *
     * @param secret The secret key for signing JWT tokens.
     */
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Base64.getEncoder().encode(secret.getBytes());
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    @Value("${jwt.expirationMs}") 
    private int expirationMs; // Token expiration time in milliseconds

    /**
     * Generates a JWT token for the given username.
     *
     * @param username The subject (typically the user's email) for which the token is generated.
     * @return A signed JWT token.
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the username from a given JWT token.
     *
     * @param token The JWT token.
     * @return The username (email) associated with the token.
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates a given JWT token.
     *
     * @param token The JWT token to validate.
     * @return true if the token is valid, false otherwise.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false; // Token is invalid or expired
        }
    }
}
