package com.sdcompany.callpop.config.security;

import com.sdcompany.callpop.config.security.properties.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenProvider {
    private final JwtConfig jwtConfig;
    public String createToken(String userIdentifier) {

        return Jwts.builder()
                .signWith(new SecretKeySpec(
                        jwtConfig.secretKey().getBytes(),
                        SignatureAlgorithm.HS512.getJcaName()))   // HS512 알고리즘을 사용하여 secretKey를 이용해 서명
                .setSubject(userIdentifier)  // JWT 토큰 제목
                .setIssuer("callpop")      // JWT 토큰 발급자
                .setIssuedAt(new Date())    // JWT 토큰 발급 시간
                .setExpiration(Date.from(
                        Instant.now().plus(
                                365,
                                ChronoUnit.DAYS)))    // JWT 토큰 만료 시간
                .compact(); // JWT 토큰 생성
    }

    public String validateTokenAndGetSubject(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(jwtConfig.secretKey().getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String parseBearerToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .filter(token -> token.startsWith("Bearer "))
                .map(token -> token.substring(7))
                .orElse(null);
    }

    public Instant getExpiryDate(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.secretKey().getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration().toInstant();
    }
}
