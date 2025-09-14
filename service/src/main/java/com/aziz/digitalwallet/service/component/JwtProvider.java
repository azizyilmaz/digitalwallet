package com.aziz.digitalwallet.service.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Component
public class JwtProvider {

    // use a secure key — for demo, generate from property; in prod use proper secret in vault
    private final Key key = Keys.hmacShaKeyFor(System.getenv().getOrDefault("JWT_SECRET", "changeitchangeitchangeitchangeit").getBytes());

    public String createToken(UUID userId, Set<String> roles) {
        Instant now = Instant.now();
        // 1 hour-> 3600L, 1 minute-> 60L
        long validitySeconds = 60L;
        Instant expiry = now.plusSeconds(validitySeconds);
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(userId.toString()) // subject=userId
                .claim("roles", new ArrayList<>(roles))
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    public Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    public Instant getExpiry(String token) {
        return parseClaims(token).getBody().getExpiration().toInstant();
    }
}
