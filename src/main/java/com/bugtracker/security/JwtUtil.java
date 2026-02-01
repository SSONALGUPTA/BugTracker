package com.bugtracker.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
  // Must be at least 256 bits (32 bytes)
  private final String secret = "bugtracker-secret-key-must-be-very-long-to-be-secure-and-safe-for-hs256";
  private final long exp = 86400000;

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(String email) {
    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + exp))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  public String extractEmail(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
}
