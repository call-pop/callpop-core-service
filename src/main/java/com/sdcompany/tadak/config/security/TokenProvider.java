package com.sdcompany.tadak.config.security;

import com.sdcompany.tadak.config.security.properties.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenProvider {
    private final JwtConfig jwtConfig;

    private SecretKey key() {
        return Keys.hmacShaKeyFor(jwtConfig.secretKey().getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(Long userId, String userIdentifier) {
        return Jwts.builder()
                .signWith(key(), SignatureAlgorithm.HS256)
                .setSubject(String.valueOf(userId))
                .setIssuer("tadak")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plus(365, ChronoUnit.DAYS)))
                .claim("userIdentifier", userIdentifier)
                .claim("roles", List.of("ROLE_USER"))
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String validateTokenAndGetSubject(String token) {
        return parseClaims(token).getSubject();
    }

    public Instant getExpiryDate(String token) {
        return parseClaims(token).getExpiration().toInstant();
    }

    public String parseBearerToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(h -> h.startsWith("Bearer "))
                .map(h -> h.substring(7))
                .orElse(null);
    }
}
