package com.mongs.play.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String JWT_KEY;
    @Value("${jwt.access-expiration}")
    private Long ACCESS_TOKEN_EXPIRED;
    @Value("${jwt.refresh-expiration}")
    private Long REFRESH_TOKEN_EXPIRED;

    public Long getRefreshTokenExpiration() {
        return REFRESH_TOKEN_EXPIRED;
    }

    public Boolean isTokenExpired(String token) {
        try {
            Date expiration = extractAllClaims(token).getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException | SignatureException e) {
            return true;
        }
    }
    public String generateAccessToken(Long memberId, String deviceId) {
        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);
        claims.put("deviceId", deviceId);
        return generateToken(claims, ACCESS_TOKEN_EXPIRED);
    }
    public String generateRefreshToken() {
        Claims claims = Jwts.claims();
        return generateToken(claims, REFRESH_TOKEN_EXPIRED);
    }
    public Optional<Long> getExpiredSeconds(String token) {
        if (isTokenExpired(token)) {
            return Optional.empty();
        }
        LocalDateTime expiration = extractAllClaims(token).getExpiration().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        long expired = Duration.between(LocalDateTime.now(), expiration).getSeconds();
        return Optional.of(Math.max(0, expired));
    }
    public Optional<Long> getMemberId(String token) {
        if (isTokenExpired(token)) {
            return Optional.empty();
        }
        return Optional.of(extractAllClaims(token).get("memberId", Long.class));
    }
    public Optional<String> getDeviceId(String token) {
        if (isTokenExpired(token)) {
            return Optional.empty();
        }
        return Optional.of(extractAllClaims(token).get("deviceId", String.class));
    }

    private Claims extractAllClaims(String token) {
        Key signingKey = getSigningKey();
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = JWT_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateToken(Claims claims, Long expired) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + expired * 1000);      // Millis Seconds
        Key signingKey = getSigningKey();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt)
                .setExpiration(expiration)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }
}